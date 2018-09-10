package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.BatchRuntimeException;

public class QueryNaoEncontradaException extends BatchRuntimeException {
	
	private static final long serialVersionUID = -709120878955141853L;

	private static final String QUERY_NAO_ENCONTRADA = "Query não encontrada ";

	private static final int CODIGO_EXCECAO = 15;

	public QueryNaoEncontradaException(String query) {
		super(QUERY_NAO_ENCONTRADA + query, CODIGO_EXCECAO);
	}

	public QueryNaoEncontradaException(String query, Throwable cause) {
		super(QUERY_NAO_ENCONTRADA + query, cause, CODIGO_EXCECAO);
	}

}
