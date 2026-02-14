package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.models.RecipeIngredient;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecipeIngredientDAO implements RecipeIngredientRepository {

    private final String url;

    public RecipeIngredientDAO(String url) {
        this.url = url;
    }

    public void insert(RecipeIngredient ri) throws SQLException {
        String sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ri.getRecipeId());
            pstmt.setInt(2, ri.getIngredientId());
            pstmt.setInt(3, ri.getQuantity());
            pstmt.setString(4, ri.getUnit());
            pstmt.executeUpdate();
        }
    }

    public List<RecipeIngredient> findByRecipeId(int recipeId) throws SQLException {
        List<RecipeIngredient> list = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredients WHERE recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new RecipeIngredient(
                                rs.getInt("recipe_id"),
                                rs.getInt("ingredient_id"),
                                rs.getInt("quantity"),
                                rs.getString("unit")
                        )
                );
            }
        }
        return list;
    }

    public void deleteByRecipeId(int recipeId) throws SQLException {
        String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
        }
    }

    public void addIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException {
        String sql = """
                INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit)
                VALUES (?, ?, ?, ?)
                """;
        try (Connection conn = DriverManager.getConnection(url);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.setInt(2, ingredientId);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, unit);
            pstmt.executeUpdate();
        }
    }

    public void removeIngredient(int recipeId, int ingredientId)
            throws SQLException {
        String sql = """
                DELETE FROM recipe_ingredients 
                WHERE recipe_id = ? AND ingredient_id = ?
                """;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.setInt(2, ingredientId);

            pstmt.executeUpdate();
        }
    }

    public void updateIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException {
        String sql = """
                UPDATE recipe_ingredients
                SET quantity = ?, unit = ?
                WHERE recipe_id = ? AND ingredient_id = ?
                """;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.setInt(2, ingredientId);
            pstmt.setInt(3, quantity);
            pstmt.setString(4, unit);
            pstmt.executeUpdate();
        }
    }

}

