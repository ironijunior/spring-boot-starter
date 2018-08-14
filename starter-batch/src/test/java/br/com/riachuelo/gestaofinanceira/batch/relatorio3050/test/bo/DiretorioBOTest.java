package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.bo;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.DiretorioBO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.DiretorioDAO;

@RunWith(MockitoJUnitRunner.class)
public class DiretorioBOTest {
	
	private static final String ALIAS = "caminho";
	private final static String CAMINHO_DIRETORIO = "C:\\diretorio";
	
	@InjectMocks
	private DiretorioBO bo;

	@Mock
	private DiretorioDAO dao;
	
	@Test
	public void buscarDiretorio() {
		Mockito.when(dao.buscar(ALIAS)).thenReturn(CAMINHO_DIRETORIO);
		Assert.assertEquals(CAMINHO_DIRETORIO, bo.buscar(ALIAS));
	}
}
