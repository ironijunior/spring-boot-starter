package br.com.ironimedina.batch.tasklets;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ironimedina.batch.relatorio3050.bo.DataBO;
import br.com.ironimedina.batch.relatorio3050.bo.DiariaBO;
import br.com.ironimedina.batch.relatorio3050.bo.EscritorXmlBO;
import br.com.ironimedina.batch.relatorio3050.bo.FeriadoBO;
import br.com.ironimedina.batch.relatorio3050.bo.MensalBO;
import br.com.ironimedina.batch.relatorio3050.dto.AtributosDiariosDTO;
import br.com.ironimedina.batch.relatorio3050.dto.AtributosMensaisDTO;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagCrdLivre;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagDiario;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagMensal;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagPesFisica;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagPreDiario;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagPreMensal;
import br.com.ironimedina.batch.relatorio3050.dto.estrutura.xml.TagReferencia;
import br.com.ironimedina.batch.relatorio3050.util.DateUtil;

@Component
public class GeracaoXmlTasklet implements Tasklet {

	private static final Logger LOG = LoggerFactory.getLogger(GeracaoXmlTasklet.class);
	
	private static final int ULTIMO_DIA_SEMANA = 5;
	
	@Autowired
	private FeriadoBO feriadoBO;
	
	@Autowired
	private EscritorXmlBO escritorXmlBO;
	
	@Autowired
	private DiariaBO diariaBO;
	
	@Autowired
	private DataBO dataBO;
	
	@Autowired
	private MensalBO mensalBO;
	
	@Autowired
	private DateUtil dateUtil;
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		StepContext step = chunkContext.getStepContext();
		LocalDate dataReferencia =  dataBO.getDataReferencia((String)step.getJobParameters().get("dataReferencia"));
		LocalDate ultimoDiaMes = dataReferencia.withDayOfMonth(dataReferencia.lengthOfMonth());
		List<LocalDate> datasExecucao = obterDatasSemana(dataReferencia);
		
		escritorXmlBO.criarArquivo(step.getStepExecution(), dataReferencia);
		
		for(LocalDate data : datasExecucao) {
			TagReferencia tagReferencia = new TagReferencia(dateUtil.formatarParaReferencia(data));
			
			tagReferencia.setDiario(criarEstruturaDia());
			preencherEstruturaTagsDiarias(tagReferencia, data);
			
			if(data.equals(ultimoDiaMes)) {
				tagReferencia.setMensal(criarEstruturaMensal());
				preencherEstruturaTagsMensais(tagReferencia, data);
			}
			
			escritorXmlBO.escrever(tagReferencia);
		}
		escritorXmlBO.fecharArquivo(step.getStepExecution());
		
		return RepeatStatus.FINISHED;
	}

	private void preencherEstruturaTagsDiarias(TagReferencia tagReferencia, LocalDate data)  {
		TagPreDiario pre = tagReferencia.getTagDiario().getCrdLivre().getPesFisica().getTagPreDiario();
		Map<String, AtributosDiariosDTO> tagsDoDia = diariaBO.processar(data);
		
		for(Entry<String, AtributosDiariosDTO> entry: tagsDoDia.entrySet()) {
			String nomeMetodo = "set"+StringUtils.capitalize(entry.getKey());
			
			try {
				Method method = TagPreDiario.class.getDeclaredMethod(nomeMetodo, AtributosDiariosDTO.class);
				method.setAccessible(true);
				method.invoke(pre, entry.getValue());
				method.setAccessible(false);
			} catch(Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	private void preencherEstruturaTagsMensais(TagReferencia tagReferencia, LocalDate data)  {
		TagPreMensal pre = tagReferencia.getMensal().getCrdLivre().getPesFisica().getTagPreMensal();
		Map<String, AtributosMensaisDTO> tagsDoMes = mensalBO.processar(data);
		
		for(Entry<String, AtributosMensaisDTO> entry: tagsDoMes.entrySet()) {
			String nomeMetodo = "set"+StringUtils.capitalize(entry.getKey());
			try {
				Method method = TagPreMensal.class.getDeclaredMethod(nomeMetodo, AtributosMensaisDTO.class);
				method.setAccessible(true);
				method.invoke(pre, entry.getValue());
				method.setAccessible(false);
			} catch(Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}
	
	private TagDiario criarEstruturaDia() {
		TagPreDiario tagPreDiario = new TagPreDiario();
		
		TagPesFisica tagPesFisica = new TagPesFisica();
		tagPesFisica.setTagPreDiario(tagPreDiario);
		
		TagCrdLivre tagCrdLivre = new TagCrdLivre();
		tagCrdLivre.setPesFisica(tagPesFisica);
		
		TagDiario tagDiario = new TagDiario();
		tagDiario.setCrdLivre(tagCrdLivre);
		
		return tagDiario;
	}
	
	private TagMensal criarEstruturaMensal() {
		TagPreMensal tagPreMensal = new TagPreMensal();
		
		TagPesFisica tagPesFisica = new TagPesFisica();
		tagPesFisica.setTagPreMensal(tagPreMensal);
		
		TagCrdLivre tagCrdLivre = new TagCrdLivre();
		tagCrdLivre.setPesFisica(tagPesFisica);
		
		TagMensal tagMensal = new TagMensal();
		tagMensal.setCrdLivre(tagCrdLivre);
		
		return tagMensal;
	}

	private List<LocalDate> obterDatasSemana(LocalDate data) {
		List<LocalDate> feriados;
		List<LocalDate> datasSemana = new LinkedList<>();
		LocalDate diaSemana = data.minusDays(data.getDayOfWeek().getValue());
		
		do {
			diaSemana = diaSemana.plusDays(1);
			feriados = feriadoBO.obterFeriadosMes(diaSemana.getMonthValue());
			if(!feriados.contains(diaSemana)) {
				datasSemana.add(diaSemana);
			}
		} while(diaSemana.getDayOfWeek().getValue() < ULTIMO_DIA_SEMANA);
		
		return datasSemana;
	}
}
