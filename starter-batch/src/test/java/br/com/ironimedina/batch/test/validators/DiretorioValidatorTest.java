package br.com.ironimedina.batch.test.validators;

import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.EmptyResultDataAccessException;

import br.com.ironimedina.batch.exceptions.ParametroDiretorioException;
import br.com.ironimedina.batch.validators.DiretorioValidador;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.DiretorioBO;

@RunWith(MockitoJUnitRunner.class)
public class DiretorioValidatorTest {

	private static final String NOME_DIRETORIO = "NOME_DIRETORIO";

	@InjectMocks
	private DiretorioValidador validator;
	
	@Mock
	private DiretorioBO diretorioBO;
	
	@Rule
	public ExpectedException ex = ExpectedException.none();
	
	@Test
	public void validarNomeDiretorio() {
		when(diretorioBO.buscar(NOME_DIRETORIO))
			.thenReturn("X:/um/caminho");
		
		validator.validar(NOME_DIRETORIO);
		assert true;
	}
	
	@Test
	public void validarNomeDiretorioNaoCadastrado() {
		ex.expect(ParametroDiretorioException.class);
		validator.validar(null);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void validarDiretorioNaoCadastrado() {
		ex.expect(ParametroDiretorioException.class);
		when(diretorioBO.buscar(NOME_DIRETORIO))
			.thenThrow(EmptyResultDataAccessException.class);
		
		validator.validar(NOME_DIRETORIO);
	}
	
}
