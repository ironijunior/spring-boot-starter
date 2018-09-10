package br.com.ironimedina.batch.test.validators;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ironimedina.batch.exceptions.ParametroDataReferenciaInvalidaException;
import br.com.ironimedina.batch.validators.DataReferenciaValidator;
import br.com.ironimedina.batch.relatorio3050.types.JobExpectedParametersType;

@RunWith(MockitoJUnitRunner.class)
public class DataReferenciaValidadorTest {
	
	private static final String MSG_DATA_PERIODO_INVALIDO = "'%s' deve ser inferior a semana atual.";
	private static final String MSG_DIA_DE_SEMANA = "'%s' deve ser entre segunda e sexta.";
	private static final String FORMATO_DATA_ESPERADA = "dd/MM/yyyy";
	private static final String MSG_VALOR_DATA_INVALIDO = "Valor '%s' é inválido para o parâmetro '%s'. Formato esperado: '%s'.";

	@InjectMocks
	private DataReferenciaValidator validator;
	
	@Rule
	public ExpectedException ex = ExpectedException.none();
	
	@Test
	public void dataReferenciaInvalida() {
		String dataReferencia = "198608";
		ex.expect(ParametroDataReferenciaInvalidaException.class);
		ex.expectMessage(montarMensagemParamInvalido(dataReferencia));
		
		validator.validarDataReferencia(dataReferencia);
	}
	
	@Test
	public void dataReferenciaValida() {
		String dataReferencia = "03/01/2000";
		validator.validarDataReferencia(dataReferencia);
	}
	
	@Test
	public void dataReferenciaDeveSerMenorQueSemanaAtual() {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMATO_DATA_ESPERADA);
		
		String dataReferencia = formatter.format(new Date());
		ex.expect(ParametroDataReferenciaInvalidaException.class);
		ex.expectMessage(formatarMensagem(MSG_DATA_PERIODO_INVALIDO));
		
		validator.validarDataReferencia(dataReferencia);
	}
	
	@Test
	public void dataReferenciaNaoDeveSerSabado() {
		String dataReferencia = "04/08/2018";
		ex.expect(ParametroDataReferenciaInvalidaException.class);
		ex.expectMessage(formatarMensagem(MSG_DIA_DE_SEMANA));
		
		validator.validarDataReferencia(dataReferencia);
	}
	
	@Test
	public void dataReferenciaNaoDeveSerDomingo() {
		String dataReferencia = "05/08/2018";
		ex.expect(ParametroDataReferenciaInvalidaException.class);
		ex.expectMessage(formatarMensagem(MSG_DIA_DE_SEMANA));
		
		validator.validarDataReferencia(dataReferencia);
	}
	
	
	private String formatarMensagem(String msg) {
		return String.format(msg, JobExpectedParametersType.DATA_REFERENCIA.getKey());
	}
	
	protected String montarMensagemParamInvalido(String dataReferencia) {
		return String.format(
				MSG_VALOR_DATA_INVALIDO,
				dataReferencia,
				JobExpectedParametersType.DATA_REFERENCIA.getKey(),
				FORMATO_DATA_ESPERADA);
	}
}
