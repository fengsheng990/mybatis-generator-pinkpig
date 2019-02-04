package com.pinkpig.mybatis.generator.api;

import java.sql.Connection;
import java.sql.SQLException;

import org.mybatis.generator.api.ConnectionFactory;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.internal.JDBCConnectionFactory;
import org.mybatis.generator.internal.ObjectFactory;

public class PluginContext {

    private static Connection connection;
    
    public static Connection getConnection(Context context) throws SQLException {
        if(null == connection) {
            ConnectionFactory connectionFactory;
            if (context.getJdbcConnectionConfiguration() != null) {
                connectionFactory = new JDBCConnectionFactory(context.getJdbcConnectionConfiguration());
            } else {
                connectionFactory = ObjectFactory.createConnectionFactory(context);
            }
            return connectionFactory.getConnection();
        }else {
            return connection;
        }
    }
    
}
