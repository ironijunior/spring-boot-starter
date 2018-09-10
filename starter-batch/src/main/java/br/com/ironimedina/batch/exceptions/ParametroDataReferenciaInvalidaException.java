package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.BatchRuntimeException;

public class ParametroDataReferenciaInvalidaException extends BatchRuntimeException {

	private static final long serialVersionUID = 3668446266295575412L;

	private static final int CODE = 17;

	public ParametroDataReferenciaInvalidaException(String message) {
		super(message, CODE);
	}
}
