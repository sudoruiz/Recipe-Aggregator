package com.mycompany.recipeaggregator.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfigTest {

    private static final String TEST_DB_URL = "jdbc:sqlite:file:testdb?mode=memory&cache=shared";
    private static Connection keepAlive;

    public static void configure() {
        System.setProperty("db.url", TEST_DB_URL);
        try {
            keepAlive = DriverManager.getConnection(TEST_DB_URL);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize in-memory test database", e);
        }
    }
}
