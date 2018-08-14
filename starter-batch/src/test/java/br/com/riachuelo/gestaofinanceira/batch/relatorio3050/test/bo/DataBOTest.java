package br.com.riachuelo.gestaofinanceira.batch.relatorio3050.test.bo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.DataBO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.bo.FeriadoBO;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.factory.DateFactory;

@RunWith(MockitoJUnitRunner.class)
public class DataBOTest {

	private static final String FORMATO_DATA = "dd/MM/yyyy";

	@InjectMocks
	private DataBO dataBO;
	
	@Mock
	private FeriadoBO feriadoBO;
	
	@Mock
	private DateFactory dateFactory;
	
	@Before
	public void init() {
		Mockito.when(feriadoBO.obterFeriadosMes(9)).thenReturn(Arrays.asList(LocalDate.of(2017, 9, 7)));
		Mockito.when(feriadoBO.obterFeriadosMes(12)).thenReturn(Arrays.asList(LocalDate.of(2017, 12, 25)));
	}
	
	@Test
	public void obterDataReferenciaPassandoDataSegunda() {
		String dataTexto = "02/01/2017";
		LocalDate dataFormatada = LocalDate.parse(dataTexto, DateTimeFormatter.ofPattern(FORMATO_DATA));
		
		Assert.assertEquals(dataFormatada, dataBO.getDataReferencia(dataTexto));
	}
	
	@Test
	public void obterDataReferenciaPassandoDataQuarta() {
		String dataTextoSegunda = "02/01/2017";
		String dataTextoQuarta = "04/01/2017";
		LocalDate dataFormatada = LocalDate.parse(dataTextoSegunda, DateTimeFormatter.ofPattern(FORMATO_DATA));
		
		Assert.assertEquals(dataFormatada, dataBO.getDataReferencia(dataTextoQuarta));
	}
	
	@Test
	public void obterDataReferenciaPassandoDataSegundaComFeriado() {
		String dataTextoSegundaFeriado = "25/12/2017";
		String dataTextoTerca = "26/12/2017";
		LocalDate dataFormatada = LocalDate.parse(dataTextoTerca, DateTimeFormatter.ofPattern(FORMATO_DATA));
		
		Assert.assertEquals(dataFormatada, dataBO.getDataReferencia(dataTextoSegundaFeriado));
	}
	
	@Test
	public void obterDataReferenciaPassandoDataQuartaComFeriado() {
		String dataTextoQuarta = "27/12/2017";
		String dataTextoTerca = "26/12/2017";
		LocalDate dataFormatada = LocalDate.parse(dataTextoTerca, DateTimeFormatter.ofPattern(FORMATO_DATA));
		
		Assert.assertEquals(dataFormatada, dataBO.getDataReferencia(dataTextoQuarta));
	}
	
	@Test
	public void obterDataReferenciaSemDataExecutandoNaSegunda() {
		LocalDate data = LocalDate.of(2017, 1, 9);
		LocalDate dataEsperada = LocalDate.of(2017, 1, 2);
		
		Mockito.when(dateFactory.dataHoje()).thenReturn(data);
		
		Assert.assertEquals(dataEsperada, dataBO.getDataReferencia(null));
	}
	
	@Test
	public void obterDataReferenciaSemDataExecutandoNaQuarta() {
		LocalDate data = LocalDate.of(2017, 1, 11);
		LocalDate dataEsperada = LocalDate.of(2017, 1, 2);
		
		Mockito.when(dateFactory.dataHoje()).thenReturn(data);
		
		Assert.assertEquals(dataEsperada, dataBO.getDataReferencia(null));
	}
	
	@Test
	public void obterDataReferenciaSemDataExecutandoNaSegundaComFeriado() {
		LocalDate data = LocalDate.of(2018, 1, 1);
		LocalDate dataEsperada = LocalDate.of(2017, 12, 26);
		
		Mockito.when(dateFactory.dataHoje()).thenReturn(data);
		
		Assert.assertEquals(dataEsperada, dataBO.getDataReferencia(null));
	}
	
	@Test
	public void obterDataReferenciaSemDataExecutandoNaQuartaComFeriado() {
		LocalDate data = LocalDate.of(2018, 1, 3);
		LocalDate dataEsperada = LocalDate.of(2017, 12, 26);
		
		Mockito.when(dateFactory.dataHoje()).thenReturn(data);
		
		Assert.assertEquals(dataEsperada, dataBO.getDataReferencia(null));
	}
}
