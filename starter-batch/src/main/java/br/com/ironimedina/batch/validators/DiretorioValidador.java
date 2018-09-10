package br.com.ironimedina.batch.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import br.com.ironimedina.batch.exceptions.ParametroDiretorioException;
import br.com.ironimedina.batch.relatorio3050.bo.DiretorioBO;
import br.com.ironimedina.batch.relatorio3050.types.JobExpectedParametersType;

@Component
public class DiretorioValidador {

	private static final String CAMINHO_NAO_CADASTRADO_NO_DIRETORIO = "Caminho não cadastrado no diretório '%s'.";
	private static final String MSG_VALOR_CAMINHO_INVALIDO = "O Valor '%s' é inválido para o parâmetro '%s'.";

	@Autowired
	private DiretorioBO diretorioBO;

	public void validar(String nomeDiretorio) {
		if (nomeDiretorio == null) {
			throw new ParametroDiretorioException(
					montarMensagemParamInvalido(nomeDiretorio));
		}
		
		try {
			diretorioBO.buscar(nomeDiretorio);
		} catch (EmptyResultDataAccessException e) {
			throw new ParametroDiretorioException(getMensagemErroCaminho(nomeDiretorio));
		}
	}

	private String getMensagemErroCaminho(String nomeDiretorio) {
		return String.format(CAMINHO_NAO_CADASTRADO_NO_DIRETORIO, nomeDiretorio);
	}

	protected String montarMensagemParamInvalido(String nomeDiretorio) {
		return String.format(
				MSG_VALOR_CAMINHO_INVALIDO,
				nomeDiretorio,
				JobExpectedParametersType.NOME_DIRETORIO.getKey());
	}
}