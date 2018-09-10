package br.com.ironimedina.batch.exceptions.resolver;

import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component("exceptionHandler")
public class BatchExceptionHandler implements ExceptionHandler, ExitCodeGenerator {

	private int exitCode = 0;

	@Override
	public void handleException(RepeatContext context, Throwable throwable) throws Throwable {
		Throwable err = null;
		if(throwable instanceof IBatchException) {
			err = throwable;
		} else if(throwable.getCause() instanceof IBatchException) {
			err = throwable.getCause();
		}
		
		if(err == null) {
			exitCode = 1;
		} else {
			exitCode = ((IBatchException) err).getCode();
		}
		
		throw throwable;
	}
	
	@Override
	public int getExitCode() {
		return exitCode;
	}
	
}
