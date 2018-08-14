package br.com.ironimedina.batch.test.validators;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.ironimedina.batch.exceptions.ParametroDiretorioException;
import br.com.ironimedina.batch.validators.DiretorioValidador;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.DiretorioBO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.DiretorioDAO;

@RunWith(MockitoJUnitRunner.class)
public class ValidadorNomeDiretorioTest {

	private static final String NOME_DIRETORIO = "NOME_DIRETORIO";

	@InjectMocks
	private DiretorioValidador validator;
	
	@Mock
	private DiretorioDAO buscaCaminhoDiretorioDAO;
	
	@Mock
	private DiretorioBO diretorioBO;
	
	@Test
	public void validarNomeDiretorio() {
		when(buscaCaminhoDiretorioDAO.buscar(NOME_DIRETORIO))
			.thenReturn("X:/um/caminho");
		
		validator.validar(NOME_DIRETORIO);
		assert true;
	}
	
	@Test(expected = ParametroDiretorioException.class)
	public void validarNomeDiretorioNaoCadastrado() {
		when(buscaCaminhoDiretorioDAO.buscar(NOME_DIRETORIO))
		.thenReturn(null);
		
		validator.validar(null);
		assert false;
	}
}
