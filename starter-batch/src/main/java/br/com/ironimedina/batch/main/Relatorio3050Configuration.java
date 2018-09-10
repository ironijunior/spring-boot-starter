package br.com.ironimedina.batch.main;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import br.com.ironimedina.batch.exceptions.resolver.BatchExceptionHandler;
import br.com.ironimedina.batch.tasklets.GeracaoXmlTasklet;
import br.com.ironimedina.batch.tasklets.listeners.JobListener;
import br.com.ironimedina.batch.validators.ParametrosValidador;

@EnableAutoConfiguration(exclude = { HibernateJpaAutoConfiguration.class })
@EnableBatchProcessing
@ComponentScan("br.com.ironimedina.batch")
public class Relatorio3050Configuration {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private ParametrosValidador validadorParametros;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private GeracaoXmlTasklet geracaoXmlTasklet;

	@Autowired
	private JobListener jobListener;
	
	@Autowired
	private BatchExceptionHandler batchHandler;

	@Bean
	public Job job() {
		return jobBuilderFactory
				.get("JOB_STARTER")
				.validator(validadorParametros)
				.incrementer(new RunIdIncrementer())
				.start(geracaoXmlTask())
				.listener(jobListener)
				.build();
	}

	@Bean
	public Step geracaoXmlTask() {
		return stepBuilderFactory
				.get("TASKLET_XML")
				.tasklet(geracaoXmlTasklet)
				.exceptionHandler(batchHandler)
				.build();
	}
}
