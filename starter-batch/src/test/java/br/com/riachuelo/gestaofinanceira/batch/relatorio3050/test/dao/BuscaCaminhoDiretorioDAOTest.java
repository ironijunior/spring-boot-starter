package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.dao;

import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.DiretorioDAO;

@RunWith(MockitoJUnitRunner.class)
public class BuscaCaminhoDiretorioDAOTest {
	private static String QUERY = "select directory_path " +
			"from all_directories " +
			"where directory_name = ? ";
	
	@InjectMocks
	private DiretorioDAO dao;
	
	@Mock
	private JdbcTemplate jdbc;
	
	@Test
	public void buscarCaminhoDiretorio() {
		Object[] params = new Object[] { "BFILE_DIR" };
		
		dao.buscar("BFILE_DIR");
		verify(jdbc).queryForObject(QUERY, params, String.class);
	}
}
