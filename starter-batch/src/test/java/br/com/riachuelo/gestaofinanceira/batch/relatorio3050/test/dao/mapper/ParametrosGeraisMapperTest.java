package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.dao.mapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.mapper.ParametrosGeraisMapper;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dto.ParametrosGeraisDTO;

@RunWith(MockitoJUnitRunner.class)
public class ParametrosGeraisMapperTest {

	private static final int UM = 1;
	private static final String NOME_CONTATO = "Eduardo";
	private static final String TEL_CONTATO = "40028922";
	private static final String CNPJ = "1112";

	@InjectMocks
	private ParametrosGeraisMapper mapper;
	
	@Mock
	private ResultSet rs;
	
	@Test
	public void mapRow() throws SQLException {
		when(rs.getString("NomeContato")).thenReturn(NOME_CONTATO);
		when(rs.getString("TelefoneContato")).thenReturn(TEL_CONTATO);
		when(rs.getString("Cnpj")).thenReturn(CNPJ);
		
		ParametrosGeraisDTO dto = mapper.mapRow(rs, UM);
		
		assertEquals(NOME_CONTATO, dto.getNomeContato());
		assertEquals(TEL_CONTATO, dto.getTelefoneContato());
		assertEquals(CNPJ, dto.getCnpj());
	}
}
