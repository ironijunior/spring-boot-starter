package br.com.ironimedina.batch.test.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.ironimedina.batch.relatorio3050.dao.DiretorioDAO;

@RunWith(MockitoJUnitRunner.class)
public class DiretorioDAOTest {

	@InjectMocks
	private DiretorioDAO dao;
	
	@Mock
	private JdbcTemplate template;
	
	@Test
	public void test() {
		String esperado = "C:\\";
		String alias = "DIR_RCH";
		String query = "select directory_path from all_directories where directory_name = ? ";
		
		Mockito.when(
				template.queryForObject(query, new Object[] { alias }, String.class))
			.thenReturn(esperado);
		
		String atual = dao.buscar(alias);
		
		Assert.assertEquals(atual, esperado);
	}
}
