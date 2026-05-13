package com.wedding.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static String jdbcUrl;
    private static String jdbcUsername;
    private static String jdbcPassword;

    public static void initialize(String driver, String url, String username, String password) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver class not found", e);
        }
        jdbcUrl = url;
        jdbcUsername = username;
        jdbcPassword = password;
    }

    public static Connection getConnection() throws SQLException {
        if (jdbcUrl == null) {
            throw new IllegalStateException("Database configuration not initialized");
        }
        return DriverManager.getConnection(jdbcUrl, jdbcUsername, jdbcPassword);
    }
}
