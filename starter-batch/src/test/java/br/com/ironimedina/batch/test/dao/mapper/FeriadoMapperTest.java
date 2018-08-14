package br.com.ironimedina.batch.test.dao.mapper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.GregorianCalendar;

import javax.xml.datatype.XMLGregorianCalendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.riachuelo.api.modelo.corporativo.v1.Feriado;
import br.com.riachuelo.gestaofinanceira.batch.relatorio3050.dao.mapper.FeriadoMapper;

@RunWith(MockitoJUnitRunner.class)
public class FeriadoMapperTest {
	
	@InjectMocks
	private FeriadoMapper feriadoMapper;
	
	@Mock
	private Feriado feriado;
	
	@Mock
	private GregorianCalendar gregorianCalendar;
	
	@Mock
	private XMLGregorianCalendar xmlGregCalendar;
	
	private ZonedDateTime zonedDateTime = ZonedDateTime.now();
	private LocalDate localDate = LocalDate.now();
	
	@Before
	public void setUp() throws Exception {
		when(feriado.getDataFeriado())
		.thenReturn(xmlGregCalendar);
		
		when(xmlGregCalendar.toGregorianCalendar())
		.thenReturn(gregorianCalendar);
		
		when(gregorianCalendar.toZonedDateTime())
		.thenReturn(zonedDateTime);
		;
	}

	@Test
	public void testApply() {
		assertEquals(localDate, feriadoMapper.apply(feriado));
	}

}
