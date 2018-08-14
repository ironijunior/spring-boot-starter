package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.dao;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.AtualizacaoControleRelatoriosDAO;

@RunWith(MockitoJUnitRunner.class)
public class AtualizacaoControleRelatoriosDAOTest {
	private static final String QUERY;
	
	private static final int IDX_CG_TIPO_RLRO = 1;
	private static final int IDX_AM_CNDE = 2;
	private static final int IDX_ALIAS_BFILE_FOLDER = 3;
	private static final int IDX_FILENAME = 4;
	private static final int IDX_CG_TIPO_RLRO_INSERT = 5;
	private static final int IDX_AM_CNDE_INSERT = 6;
	private static final int IDX_ALIAS_BFILE_FOLDER_INSERT = 7;
	private static final int IDX_FILENAME_INSERT = 8;
	
	private static final Integer TIPO_RELATORIO = 1;
	private static final String DATA_REFERENCIA = "198608";
	private static final String BFILE_ALIAS = "BFILE_FOLDER";
	private static final String NOME_RELATORIO = "Relatorio.XLSX";
	
	static {
		QUERY = "MERGE INTO TSGF_CNLE_RLRO CNLE USING dual ON ("
				+ " CNLE.CG_TIPO_RLRO = ? AND CNLE.AM_CNDE  = ? ) "
				+ " WHEN MATCHED THEN UPDATE "
				+ " SET CNLE.DT_PRNO_RLRO = SYSDATE,"
				+ " CNLE.NO_LCAL_ARZT_RLRO_VSAO = BFILENAME( ?, ? ) "
				+ " WHEN NOT MATCHED THEN INSERT "
				+ " ( CNLE.CG_TIPO_RLRO,  CNLE.AM_CNDE,  CNLE.DT_PRNO_RLRO,  CNLE.NO_LCAL_ARZT_RLRO_VSAO )"
				+ " VALUES (  ?,  ?,  SYSDATE,  BFILENAME( ?, ? ) ) ";
	}
	
	@InjectMocks
	private AtualizacaoControleRelatoriosDAO dao;
	
	@Mock
	private JdbcTemplate jdbc;
	
	@Mock
	private PreparedStatement preparedStatement;
	
	@Test
	public void atualizarPeriodos() throws SQLException {
		when(jdbc.update(eq(QUERY), any(PreparedStatementSetter.class)))
				.then(invocator -> {
					invocator.getArgumentAt(1, PreparedStatementSetter.class)
							.setValues(preparedStatement);
					return 0;
				});
		
		dao.atualizarPeriodos(TIPO_RELATORIO, DATA_REFERENCIA, BFILE_ALIAS, NOME_RELATORIO);
		
		verify(jdbc).update(eq(QUERY), any(PreparedStatementSetter.class));
		verify(preparedStatement).setInt(IDX_CG_TIPO_RLRO, TIPO_RELATORIO);
		verify(preparedStatement).setInt(IDX_AM_CNDE, Integer.valueOf(DATA_REFERENCIA));
		verify(preparedStatement).setString(IDX_ALIAS_BFILE_FOLDER, BFILE_ALIAS);
		verify(preparedStatement).setString(IDX_FILENAME, NOME_RELATORIO);
		verify(preparedStatement).setInt(IDX_CG_TIPO_RLRO_INSERT, TIPO_RELATORIO);
		verify(preparedStatement).setInt(IDX_AM_CNDE_INSERT, Integer.valueOf(DATA_REFERENCIA));
		verify(preparedStatement).setString(IDX_ALIAS_BFILE_FOLDER_INSERT, BFILE_ALIAS);
		verify(preparedStatement).setString(IDX_FILENAME_INSERT, NOME_RELATORIO);
	}
}
