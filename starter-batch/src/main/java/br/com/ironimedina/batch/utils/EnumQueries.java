package br.com.ironimedina.batch.utils;

public enum EnumQueries implements IEnumQueries {

	DIARIA("query_diaria.sql"),
	MENSAL("query_mensal.sql"),
	COD_RELATORIO("query_cod_relatorio.sql"),
	PRODUTOS("query_busca_produtos.sql"),
	PARAMETROS("query_parametros_gerais.sql");

	private String fileName;

	private EnumQueries(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

}
