package br.com.ironimedina.batch.test.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.ParametroGeralDAO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dto.ParametrosGeraisDTO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.util.EnumQueries;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.util.LoadQueries;

@RunWith(MockitoJUnitRunner.class)
public class ParametroGeralDAOTest {
	
	private static final String QUERY;
	
	private static final String NOME;
	private static final String CNPJ;
	private static final String FONE;
	
	static {
		QUERY = "SELECT (SELECT G.VL_PRMT_GRAL FROM TSGF_PRMT_GRAL_DTHE G WHERE G.DS_PRMT_GRAL = 'NomeResp' and G.DT_TRNI_VGCA_PRMT_GRAL_DTHE is null) AS nomeContato , "
				+ " nvl((SELECT G.VL_PRMT_GRAL FROM TSGF_PRMT_GRAL_DTHE G WHERE G.DS_PRMT_GRAL = 'TelResp' and G.DT_TRNI_VGCA_PRMT_GRAL_DTHE is null),'00-00000000') AS telefoneContato , "
				+ " (SELECT G.VL_PRMT_GRAL FROM TSGF_PRMT_GRAL_DTHE G WHERE G.DS_PRMT_GRAL = 'Bacen_CNPJ' and G.DT_TRNI_VGCA_PRMT_GRAL_DTHE is null) AS Cnpj "
				+ " FROM DUAL";
		NOME = "EDUARDO";
		CNPJ = "0000000";
		FONE = "0000000";
	}
	
	@InjectMocks
	private ParametroGeralDAO dao;
	
	@Mock
	private JdbcTemplate jdbc;
	
	@Mock
	private LoadQueries queries;
	
	@SuppressWarnings("unchecked")
	@Test
	public void getParametrosGerais() {
		Mockito.when(queries.getQuery(EnumQueries.PARAMETROS)).thenReturn(QUERY);
		ParametrosGeraisDTO dto = fabricarDTO();
		
		Mockito.when(jdbc.queryForObject(
				Mockito.eq(QUERY), Mockito.any(RowMapper.class)))
			.thenReturn(dto);
		
		Assert.assertEquals(NOME, dao.getParametrosGerais().getNomeContato());
		Assert.assertEquals(CNPJ, dao.getParametrosGerais().getCnpj());
		Assert.assertEquals(FONE, dao.getParametrosGerais().getTelefoneContato());
	}

	private ParametrosGeraisDTO fabricarDTO() {
		ParametrosGeraisDTO dto = new ParametrosGeraisDTO();
		dto.setNomeContato(NOME);
		dto.setCnpj(CNPJ);
		dto.setTelefoneContato(FONE);
		return dto; 
	}
}
