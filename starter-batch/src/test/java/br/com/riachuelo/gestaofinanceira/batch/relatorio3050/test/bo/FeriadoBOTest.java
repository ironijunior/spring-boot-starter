package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.bo;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.FeriadoBO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.FeriadoDAO;

@RunWith(MockitoJUnitRunner.class)
public class FeriadoBOTest {
	
	@InjectMocks
	private FeriadoBO bo;

	@Mock
	private FeriadoDAO dao;
	
	@Test
	public void obterFeriadosMes() {
		int mesAtual = LocalDate.now().getMonthValue();
		List<LocalDate> feriadosEsperados = criarFeriadosEsperados();
		
		Mockito.when(dao.listarDatasFeriadoDoMes(mesAtual)).thenReturn(criarFeriadosEsperados());
		List<LocalDate> feriados = bo.obterFeriadosMes(mesAtual);
		
		Assert.assertEquals(feriadosEsperados.get(0), feriados.get(0));
		Assert.assertEquals(feriadosEsperados.get(1), feriados.get(1));
		Assert.assertEquals(feriadosEsperados.size(), feriados.size());
	}
	
	private List<LocalDate> criarFeriadosEsperados() {
		return Arrays.asList(LocalDate.now(), LocalDate.now().plusDays(1));
	}
}
