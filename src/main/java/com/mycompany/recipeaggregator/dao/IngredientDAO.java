package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.dto.IngredientResponseDTO;
import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.models.Ingredient;
import com.mycompany.recipeaggregator.repository.IngredientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO implements IngredientRepository {

    private final String url;

    public IngredientDAO(String url) {
        this.url = url;
    }

    @Override
    public List<Ingredient> listAll() throws SQLException {
        List<Ingredient> list = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Ingredient(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return list;
    }

    @Override
    public Ingredient create(Ingredient ingredient) throws SQLException {
        String sql = "INSERT INTO ingredients (name) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, ingredient.getName());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                ingredient.setId(keys.getInt(1));
            }
        }
        return ingredient;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<IngredientUsageDTO> listMostUsed() throws SQLException {
        List<IngredientUsageDTO> result = new ArrayList<>();

        String sql = """
                    SELECT
                        i.id,
                        i.name,
                        COUNT(ri.ingredient_id) AS usage_count
                    FROM recipe_ingredients ri
                    JOIN ingredients i ON i.id = ri.ingredient_id
                    GROUP BY i.id, i.name
                    ORDER BY usage_count DESC
                """;

        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                result.add(new IngredientUsageDTO(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("usage_count")
                ));
            }
        }

        return result;
    }

}
