package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.batch.core.JobParameters;

import br.com.ironimedina.batch.validators.DataReferenciaValidator;
import br.com.ironimedina.batch.validators.DiretorioValidador;
import br.com.ironimedina.batch.validators.ParametrosValidador;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.types.JobExpectedParametersType;

@RunWith(MockitoJUnitRunner.class)
public class ParametrosValidadorTest {
	
	@InjectMocks
	private ParametrosValidador parametrosValidador;
	
	@Mock
	private DataReferenciaValidator validadorDataReferencia;

	@Mock
	private DiretorioValidador diretorioValidator;

	@Test
	public void validarComDataReferencia() {
		JobParameters params = Mockito.mock(JobParameters.class);
		Mockito.when(params.getString(JobExpectedParametersType.DATA_REFERENCIA.getKey()))
			.thenReturn("01/01/2000");
		parametrosValidador.validate(params);
	}
	
	@Test
	public void validarSemDataReferencia() {
		JobParameters params = Mockito.mock(JobParameters.class);
		Mockito.when(params.getString(JobExpectedParametersType.DATA_REFERENCIA.getKey()))
		.thenReturn(null);
		parametrosValidador.validate(params);
	}

}
