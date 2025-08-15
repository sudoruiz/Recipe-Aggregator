package com.mycompany.recipeaggregator;

import static com.mycompany.recipeaggregator.config.DatabaseConfig.TEST_DB_URL;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.*;

public class RecipeDAOTest {
    
    private RecipeDAO dao;
    
    @BeforeEach
    void setup() {
        dao = new RecipeDAO(TEST_DB_URL);
    }
    
    @Test
    void testCreateTable() throws SQLException {
        dao.createTable();
        
        try (Connection conn = DriverManager.getConnection(TEST_DB_URL);
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='recipes'")) {
            ResultSet rs = ps.executeQuery();
            assertTrue(rs.next(), "A tabela 'recipes' deveria ter sido criada");
        }
    }
    
    
}
