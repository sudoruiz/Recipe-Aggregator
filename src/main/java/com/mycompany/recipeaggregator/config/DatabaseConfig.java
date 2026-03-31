package com.mycompany.recipeaggregator.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    public static final String DB_URL = System.getProperty(
            "db.url",
            "jdbc:sqlite:/home/renan/recipe-data/recipes.db"
    );

    private static Connection keepAlive;

    public static void init() {
        if (DB_URL.contains(":memory:") || DB_URL.contains("mode=memory")) {
            try {
                keepAlive = DriverManager.getConnection(DB_URL);
            } catch (SQLException e) {
                throw new RuntimeException("Failed to initialize in-memory database", e);
            }
        }
    }
}
