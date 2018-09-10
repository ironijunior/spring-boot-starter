package br.com.ironimedina.batch.validators;

import java.util.Objects;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ironimedina.batch.relatorio3050.types.JobExpectedParametersType;

@Component
public class ParametrosValidador implements JobParametersValidator {
	
	@Autowired
	private DataReferenciaValidator validadorDataReferencia;

	@Autowired
	private DiretorioValidador diretorioValidator;
	
	public void validate(JobParameters params) {
		validarDataReferencia(params);
		validarDiretorio(params);
	}

	private void validarDataReferencia(JobParameters params) {
		String dataReferencia = params.getString(JobExpectedParametersType.DATA_REFERENCIA.getKey());
		if (Objects.nonNull(dataReferencia)) {
			validadorDataReferencia.validarDataReferencia(dataReferencia);
		}
	}

	private void validarDiretorio(JobParameters params) {
		String nomeDiretorio = params.getString(JobExpectedParametersType.NOME_DIRETORIO.getKey());
		diretorioValidator.validar(nomeDiretorio);
	}

}