package com.pinkpig.mybatis.component.dialect;

public class MysqlDialect extends Dialect {

	public String spellPageSql(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer buffer = new StringBuffer("");
		buffer.append(" select row_.* from (").append(sql).append(" limit ").append(offset).append(",").append(limit).append(") row_");
		return buffer.toString();
	}
}