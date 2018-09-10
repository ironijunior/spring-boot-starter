package br.com.ironimedina.batch.validators;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Component;

import br.com.ironimedina.batch.exceptions.ParametroDataReferenciaInvalidaException;
import br.com.ironimedina.batch.relatorio3050.types.JobExpectedParametersType;

@Component
public class DataReferenciaValidator {
	private static final String MSG_DATA_PERIODO_INVALIDO = "'%s' deve ser inferior a semana atual.";
	private static final String MSG_DIA_DE_SEMANA = "'%s' deve ser entre segunda e sexta.";
	private static final String FORMATO_DATA_ESPERADA = "dd/MM/yyyy";
	private static final String MSG_VALOR_DATA_INVALIDO = "Valor '%s' é inválido para o parâmetro '%s'. Formato esperado: '%s'.";

	public void validarDataReferencia(String dataReferencia) {
		LocalDate data = formatar(dataReferencia);
		validarDiaDaSemana(data);
		validarDataMenorQueSemanaAtual(data);
	}
	
	private LocalDate formatar(String dataReferencia) {
		try {
			return LocalDate.parse(dataReferencia, DateTimeFormatter.ofPattern(FORMATO_DATA_ESPERADA));
		} catch (DateTimeParseException e) {
			throw new ParametroDataReferenciaInvalidaException(montarMensagemParamInvalido(dataReferencia));
		}
	}

	private void validarDataMenorQueSemanaAtual(LocalDate data) {
		LocalDate diaAtual = LocalDate.now();
		LocalDate domingoSemanaAtual = diaAtual.minusDays(diaAtual.getDayOfWeek().getValue());
		
		if (data.isAfter(domingoSemanaAtual)) {
			throw new ParametroDataReferenciaInvalidaException(formatarMensagem(MSG_DATA_PERIODO_INVALIDO));
		}
	}

	private void validarDiaDaSemana(LocalDate data) {
		if (data.getDayOfWeek() == DayOfWeek.SUNDAY || data.getDayOfWeek() == DayOfWeek.SATURDAY) {
			throw new ParametroDataReferenciaInvalidaException(formatarMensagem(MSG_DIA_DE_SEMANA));
		}
	}

	private String formatarMensagem(String msg) {
		return String.format(msg, JobExpectedParametersType.DATA_REFERENCIA.getKey());
	}
	
	protected String montarMensagemParamInvalido(String dataReferencia) {
		return String.format(
				MSG_VALOR_DATA_INVALIDO,
				dataReferencia,
				JobExpectedParametersType.DATA_REFERENCIA.getKey(),
				FORMATO_DATA_ESPERADA);
	}
}