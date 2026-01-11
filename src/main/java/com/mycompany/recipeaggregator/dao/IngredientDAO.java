package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.models.Ingredient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IngredientDAO {

    private final String url;

    public IngredientDAO(String url) {
        this.url = url;
    }

    public void insert(Ingredient ingredient) throws SQLException {
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
    }

    public List<Ingredient> list() throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        String sql = "SELECT * FROM ingredients";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ingredients.add(new Ingredient(rs.getInt("id"), rs.getString("name")));
            }
        }
        return ingredients;
    }

    public Ingredient findById(int id) throws SQLException {
        String sql = "SELECT * FROM ingredients WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Ingredient(rs.getInt("id"), rs.getString("name"));
            }
            return null;
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM ingredients WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
