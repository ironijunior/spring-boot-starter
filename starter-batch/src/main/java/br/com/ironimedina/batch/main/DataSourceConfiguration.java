package br.com.ironimedina.batch.main;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.ironimedina.batch.utils.LoadQueries;

@Configuration
@Order(1)
public class DataSourceConfiguration extends DefaultBatchConfigurer {

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String user;

	@Value("${jdbc.password}")
	private String pass;

	@Value("${jdbc.driver-class-name}")
	private String driverClassName;

	@Bean(name = "sgfDS")
	public DataSource dataSource() {
		return DataSourceBuilder.create()
				.driverClassName(driverClassName)
				.url(url)
				.username(user)
				.password(pass).build();
	}

	@Bean
	public JdbcTemplate jdbcTemplate(final DataSource sgfDS) {
		return new JdbcTemplate(sgfDS);
	}
	
	@Bean(name = "namedParameterJdbcTemplate")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(final DataSource dataSource) {
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
	@Bean
	public JobLauncher jobLauncher(JobRepository jobRepository) {
		return getJobLauncher();
	}

	@Bean
	public LoadQueries loadQueries() {
		return new LoadQueries("queries");
	}

	@Override
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(null);
	}
}
