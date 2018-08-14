package br.com.ironimedina.batch.exceptions;

import br.com.ironimedina.batch.exceptions.resolver.RiachueloRuntimeException;

public class ParametrosGeraisNaoEncontradosException extends RiachueloRuntimeException {
	private static final long serialVersionUID = 5753172932964692617L;

	private static final int CODE = 11;
	
	public ParametrosGeraisNaoEncontradosException() {
		super("Um ou mais dos seguintes parâmetros gerais não foram encontrados: " +
				"'Bacen_TpCli', 'Bacen_Localiz', 'Bacen_OrigemRec', 'Bacen_NatOp', " +
				"'Bacen_VincME' e 'Bacen_CNPJ'.", CODE);
	}
}
