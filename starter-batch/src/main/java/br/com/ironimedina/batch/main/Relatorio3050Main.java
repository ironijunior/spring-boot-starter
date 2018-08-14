package br.com.ironimedina.batch.main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableCaching
public class Relatorio3050Main {
	private static final Logger LOG = LoggerFactory.getLogger(Relatorio3050Main.class);
	
	public static void main(final String[] args) {
		new Relatorio3050Main().run(args, Relatorio3050Configuration.class);
	}
	
	protected void run(String[] args, Class<?> configuration) {
		LocalDateTime horaInicial = LocalDateTime.now();
		LocalDateTime horaFinal;
		try {
			LOG.info("Processo batch invocado com os seguintes parâmetros: {}", (Object) args);
			
			ConfigurableApplicationContext context = SpringApplication.run(configuration, args);
			int codeExit = SpringApplication.exit(context);
			horaFinal = LocalDateTime.now();
			
			LOG.info("Finalizado processo batch.");
			LOG.info("Tempo total: {} Segundos", ChronoUnit.SECONDS.between(horaInicial, horaFinal));
			exit(codeExit);
		} catch(Exception e) {
			horaFinal = LocalDateTime.now();
			LOG.info("Tempo total: {} Segundos", ChronoUnit.SECONDS.between(horaInicial, horaFinal));
			exit(1);
		}
	}
	
	private void exit(int exitCode) {
		LOG.info("ExitCode {}", exitCode);
		Runtime run = Runtime.getRuntime();
		run.exit(exitCode);
	}
	
}
