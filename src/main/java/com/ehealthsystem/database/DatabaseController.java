package com.ehealthsystem.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseController {

    public Connection getConnection() throws SQLException {
        Connection connection = null;
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "ehealth.admin");
        connectionProperties.put("password", "!3U*pzAuX?@TL#_n:_j?A+Rb#qdG+@T=6Vk2uDEn&}3u+ZT.xwgAZvv}^kvYw@):");

        connection = DriverManager.getConnection("jdbc:mysql://localhost/e_health_system", connectionProperties);

        System.out.println("Connection established");
        return connection;
    }

}
