package com.mycompany.recipeaggregator.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String DB_URL = System.getProperty(
            "db.url",
            "jdbc:sqlite:/home/renan/recipe-data/recipes.db"
    );

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
}
