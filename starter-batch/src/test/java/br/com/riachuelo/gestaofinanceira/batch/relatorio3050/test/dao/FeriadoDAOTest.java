package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.dao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.api.mensagem.corporativo.v1.FeriadoResponse;
import br.com.riachuelo.api.modelo.corporativo.v1.CorporativoWService;
import br.com.riachuelo.api.modelo.corporativo.v1.Feriado;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.FeriadoDAO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.mapper.FeriadoMapper;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.factory.ServicoCorporativoFactory;

@RunWith(MockitoJUnitRunner.class)
public class FeriadoDAOTest {

	@InjectMocks
	private FeriadoDAO feriadoDAO;
	
	@Mock
	private ServicoCorporativoFactory corporativoFactory;
	
	@Mock
	private FeriadoMapper feriadoMapper;
	
	@Mock
	private CorporativoWService corporativoWService;
	
	@Mock
	private FeriadoResponse feriadoResponse;
	
	@Before
	public void init() throws DatatypeConfigurationException {
		List<Feriado> feriadosWservice = criarFeriados();
		List<LocalDate> feriadosEsperados = criarFeriadosEsperados();
		Mockito.when(corporativoFactory.obterServicoCorporativo()).thenReturn(corporativoWService);
		Mockito.when(corporativoWService.listarFeriados(null)).thenReturn(feriadoResponse);
		Mockito.when(feriadoResponse.getFeriado()).thenReturn(feriadosWservice);
		Mockito.when(feriadoMapper.apply(feriadosWservice.get(0))).thenReturn(feriadosEsperados.get(0));
		Mockito.when(feriadoMapper.apply(feriadosWservice.get(1))).thenReturn(feriadosEsperados.get(1));
	}
	
	@Test
	public void obterFeriadosMes() {
		List<LocalDate> feriados = feriadoDAO.listarDatasFeriadoDoMes(LocalDate.now().getMonthValue());
		List<LocalDate> feriadosEsperados = criarFeriadosEsperados();
		
		Assert.assertEquals(feriadosEsperados.get(0), feriados.get(0));
		Assert.assertEquals(feriadosEsperados.get(1), feriados.get(1));
		Assert.assertEquals(feriadosEsperados.size(), feriados.size());
	}
	
	private List<Feriado> criarFeriados() throws DatatypeConfigurationException {
		Feriado f1 = new Feriado();
		f1.setDataFeriado(criarXmlGregorianCalendar(0));
		Feriado f2 = new Feriado();
		f2.setDataFeriado(criarXmlGregorianCalendar(1));
		
		return Arrays.asList(f1, f2);
	}
	
	private List<LocalDate> criarFeriadosEsperados() {
		return Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1));
	}

	private XMLGregorianCalendar criarXmlGregorianCalendar(int dias) throws DatatypeConfigurationException {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(new Date(LocalDateTime.now().plusDays(dias).toInstant(ZoneOffset.UTC).getEpochSecond()));
	    
		return DatatypeFactory.newInstance()
	    		.newXMLGregorianCalendar(cal);
	}

}
