package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.RiachueloRuntimeException;

public class ParametroNaoInformadoException extends RiachueloRuntimeException {
	private static final int CODIGO_EXCECAO = 13;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3668446266295575412L;

	public ParametroNaoInformadoException(String nomeParametro) {
		super("Um parâmetro obrigatório não foi informado! Nome parâmetro: " + nomeParametro, 
				CODIGO_EXCECAO);
	}

}
