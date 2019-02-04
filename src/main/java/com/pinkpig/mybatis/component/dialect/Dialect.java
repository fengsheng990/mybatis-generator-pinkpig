package com.pinkpig.mybatis.component.dialect;

public abstract class Dialect {
	public abstract String spellPageSql(String paramString, int paramInt1, int paramInt2);

	public String getCountSql(String sql) {
		String tempsql = sql;
		return "select count(1) from (" + tempsql + ") temp_";
	}
	
	public static enum Type {
		MYSQL, ORACLE, SQLSERVER, DB2;
	}
}