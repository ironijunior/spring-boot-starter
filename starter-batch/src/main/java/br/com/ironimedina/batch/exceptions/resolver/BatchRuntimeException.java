package br.com.ironimedina.batch.exceptions.resolver;

public class BatchRuntimeException extends RuntimeException implements IBatchException {

	private static final long serialVersionUID = -996214797654483539L;

	private int code;
	
	public BatchRuntimeException(int exitCode) {
		this.code = exitCode;
	}
	
	public BatchRuntimeException(String message, int exitCode) {
		super(message);
		this.code = exitCode;
	}
	
	public BatchRuntimeException(String message, Throwable cause, int exitCode) {
		super(message, cause);
		this.code = exitCode;
	}
	
	@Override
	public int getCode() {
		return this.code;
	}
}
