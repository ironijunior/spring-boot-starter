package br.com.ironimedina.batch.tasklets.listeners;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.ironimedina.batch.relatorio3050.bo.TagProdutoRelatorioBO;

@Component
public class JobListener implements JobExecutionListener {

	@Autowired
	private TagProdutoRelatorioBO produtoBO;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		produtoBO.carregarProdutosNoRepository();
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		// Not-implemented

	}

}
