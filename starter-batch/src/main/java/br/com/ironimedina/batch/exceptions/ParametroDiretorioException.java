package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.RiachueloRuntimeException;

public class ParametroDiretorioException extends RiachueloRuntimeException {
	private static final int CODIGO_EXCECAO = 12;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6825798992471043598L;

	public ParametroDiretorioException(String message) {
		super(message, CODIGO_EXCECAO);
	}
}
