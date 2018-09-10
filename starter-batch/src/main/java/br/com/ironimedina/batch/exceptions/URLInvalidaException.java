package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.BatchRuntimeException;

public class URLInvalidaException extends BatchRuntimeException {
	private static final String URL_INFORMADA_INVALIDA = "A URL informada é inválida: ";

	private static final int CODIGO_EXCECAO = 10;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3668446266295575412L;

	public URLInvalidaException(String urlWS) {
		super(URL_INFORMADA_INVALIDA + urlWS, CODIGO_EXCECAO);
	}

	public URLInvalidaException(String urlWS, Throwable cause) {
		super(URL_INFORMADA_INVALIDA + urlWS, cause, CODIGO_EXCECAO);
	}

}
