package br.com.ironimedina.batch.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import br.com.ironimedina.batch.exceptions.QueryNaoEncontradaException;


/**
 * Classe Responsavel por carregar e manter no cache as consultas.
 */
public class LoadQueries {

	private static final char SEPARATE = '/';

	Map<IEnumQueries, String> queries = new ConcurrentHashMap<>();

	private String repositoriesPath;

	/**
	 * Instancia as consultas.
	 *
	 * @param repositoriesPath
	 *            local do repositorio das consultas
	 */
	public LoadQueries(String repositoriesPath) {
		super();
		this.repositoriesPath = repositoriesPath;
	}

	/**
	 * Busca uma query.
	 *
	 * @param queryName
	 *            nome da query
	 * @return SQL da consulta
	 */
	public String getQuery(IEnumQueries queryName) {
		if (queries.containsKey(queryName)) {
			return queries.get(queryName);
		}
		return getQueryFromFile(queryName);
	}

	private String getQueryFromFile(IEnumQueries queryName) {
		return getLinhasFromFile(queryName);
	}

	private String getLinhasFromFile(IEnumQueries queryName) {
		try (BufferedReader buffer = new BufferedReader(
				new InputStreamReader(getStream(queryName), Charset.forName("UTF-8")))) {
			String linhas = buffer.lines().collect(Collectors.joining(System.lineSeparator()));
			queries.put(queryName, linhas);
			return linhas;
		} catch (IOException e) {
			throw new QueryNaoEncontradaException(queryName.getFileName());
		}
	}

	private InputStream getStream(IEnumQueries queryName) {
		String caminho = repositoriesPath + SEPARATE + queryName.getFileName();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(caminho);
		if (is == null) {
			throw new QueryNaoEncontradaException(queryName.getFileName());
		}
		return is;
	}
}

