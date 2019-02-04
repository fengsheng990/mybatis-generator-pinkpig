package com.pinkpig.mybatis.interceptor;

import com.pinkpig.mybatis.component.SimplePage;
import com.pinkpig.mybatis.component.dialect.Dialect;
import com.pinkpig.mybatis.component.dialect.MysqlDialect;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Intercepts({ @org.apache.ibatis.plugin.Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class }) })
public class PageInterceptor implements Interceptor {
	private static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

	@SuppressWarnings("rawtypes")
	public Object intercept(Invocation invocation) throws Throwable {
		RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
		StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(handler, "delegate");
		BoundSql boundSql = delegate.getBoundSql();
		Object temp = boundSql.getParameterObject();
		if ((temp instanceof MapperMethod.ParamMap)) {
			MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) temp;
			if ((paramMap.containsKey("page")) && (paramMap.get("page") != null)) {
				SimplePage page = (SimplePage) paramMap.get("page");
				Configuration configuration = (Configuration) ReflectUtil.getFieldValue(delegate, "configuration");
				if ((page.getCount() == 0) && (page.isReturnCount())) {
					MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(delegate, "mappedStatement");
					Connection connection = (Connection) invocation.getArgs()[0];
					totalCount(connection, boundSql, configuration, mappedStatement, page);
				}
				ReflectUtil.setFieldValue(boundSql, "sql", searchDialectByDbTypeEnum(configuration, page).spellPageSql(boundSql.getSql(), page.getOffset(), page.getPageSize()));
			}
		}
		return invocation.proceed();
	}

	private void totalCount(Connection conn, BoundSql orgBoundSql, Configuration configuration, MappedStatement mappedStatement, SimplePage page) throws SQLException {
		int totalCount = 0;
		String countSpellSql = searchDialectByDbTypeEnum(configuration, page).getCountSql(orgBoundSql.getSql());
		PreparedStatement preparedStatement = conn.prepareStatement(countSpellSql);
		Object parameterObject = orgBoundSql.getParameterObject();
		BoundSql boundSql = new BoundSql(configuration, countSpellSql, orgBoundSql.getParameterMappings(), parameterObject);
		setParameters(preparedStatement, mappedStatement, boundSql, parameterObject);
		ResultSet rs = preparedStatement.executeQuery();
		try {
			if ((rs != null) && (rs.next()))
				totalCount = rs.getInt(1);
		} finally {
			if (!rs.isClosed())
				rs.close();
		}
		page.setCount(totalCount);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
		ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
		List parameterMappings = boundSql.getParameterMappings();
		if (parameterMappings == null) {
			return;
		}
		Configuration configuration = mappedStatement.getConfiguration();
		TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
		MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
		for (int i = 0; i < parameterMappings.size(); i++) {
			ParameterMapping parameterMapping = (ParameterMapping) parameterMappings.get(i);
			if (parameterMapping.getMode() != ParameterMode.OUT) {
				String propertyName = parameterMapping.getProperty();
				PropertyTokenizer prop = new PropertyTokenizer(propertyName);
				Object value;
				if (parameterObject == null) {
					value = null;
				} else {
					if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
						value = parameterObject;
					} else {
						if (boundSql.hasAdditionalParameter(propertyName)) {
							value = boundSql.getAdditionalParameter(propertyName);
						} else if ((propertyName.startsWith("__frch_")) && (boundSql.hasAdditionalParameter(prop.getName()))) {
							value = boundSql.getAdditionalParameter(prop.getName());
							if (value != null)
								value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
						} else {
							value = metaObject == null ? null : metaObject.getValue(propertyName);
						}
					}
				}
				TypeHandler typeHandler = parameterMapping.getTypeHandler();
				if (typeHandler == null) {
					throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
				}
				typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
			}
		}
	}

	private Dialect searchDialectByDbTypeEnum(Configuration configuration, SimplePage page) {
		Dialect dialect = null;
		switch (searchDbTypeByConfig(configuration).ordinal()) {
		case 0:
			dialect = new MysqlDialect();
			break;
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		}

		return dialect;
	}

	private Dialect.Type searchDbTypeByConfig(Configuration configuration) {
		String dialectConfig = configuration.getVariables().getProperty("sqlType");
		if (null!=dialectConfig && ""!=dialectConfig) {
			return Dialect.Type.valueOf(dialectConfig.toUpperCase());
		}
		throw new RuntimeException("databaseType is null , please check your mybatis configuration!");
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}

	private static class ReflectUtil {
		public static Object getFieldValue(Object obj, String fieldName) {
			Object result = null;
			Field field = getField(obj, fieldName);
			if (field != null) {
				field.setAccessible(true);
				try {
					result = field.get(obj);
				} catch (IllegalArgumentException localIllegalArgumentException) {
				} catch (IllegalAccessException localIllegalAccessException) {
				}
			}
			return result;
		}

		@SuppressWarnings("rawtypes")
		private static Field getField(Object obj, String fieldName) {
			Field field = null;
			for (Class clazz = obj.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
				try {
					field = clazz.getDeclaredField(fieldName);
				} catch (NoSuchFieldException localNoSuchFieldException) {
				}
			}
			return field;
		}

		public static void setFieldValue(Object obj, String fieldName, String fieldValue) {
			Field field = getField(obj, fieldName);
			if (field != null)
				try {
					field.setAccessible(true);
					field.set(obj, fieldValue);
				} catch (IllegalArgumentException e) {
					logger.error("发生错误:" + e.getMessage());
					throw new ExecutorException(e);
				} catch (IllegalAccessException e) {
					logger.error("发生错误:" + e.getMessage());
					throw new ExecutorException(e);
				}
		}
	}
}
