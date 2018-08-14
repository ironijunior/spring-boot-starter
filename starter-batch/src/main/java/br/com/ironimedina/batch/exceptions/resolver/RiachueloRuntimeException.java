package br.com.ironimedina.batch.exceptions.resolver;

public class RiachueloRuntimeException extends RuntimeException implements IRiachueloException {

	private static final long serialVersionUID = -996214797654483539L;

	private int code;
	
	public RiachueloRuntimeException(int exitCode) {
		this.code = exitCode;
	}
	
	public RiachueloRuntimeException(String message, int exitCode) {
		super(message);
		this.code = exitCode;
	}
	
	public RiachueloRuntimeException(String message, Throwable cause, int exitCode) {
		super(message, cause);
		this.code = exitCode;
	}
	
	@Override
	public int getCode() {
		return this.code;
	}
}
