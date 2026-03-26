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

    @Override
    public List<RecipeIngredient> list() throws SQLException {
        List<RecipeIngredient> list = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredients";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new RecipeIngredient(
                        rs.getInt("recipe_id"),
                        rs.getInt("ingredient_id"),
                        rs.getInt("quantity"),
                        rs.getString("unit")
                ));
            }
        }
        return list;
    }

    @Override
    public RecipeIngredient insert(RecipeIngredient ri) throws SQLException {
        String sql = "INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, unit) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ri.getRecipeId());
            pstmt.setInt(2, ri.getIngredientId());
            pstmt.setInt(3, ri.getQuantity());
            pstmt.setString(4, ri.getUnit());
            pstmt.executeUpdate();
        }
        return ri;
    }

    @Override
    public RecipeIngredient update(RecipeIngredient ri) throws SQLException {
        String sql = """
                UPDATE recipe_ingredients
                SET quantity = ?, unit = ?
                WHERE recipe_id = ? AND ingredient_id = ?
                """;
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ri.getQuantity());
            pstmt.setString(2, ri.getUnit());
            pstmt.setInt(3, ri.getRecipeId());
            pstmt.setInt(4, ri.getIngredientId());
            pstmt.executeUpdate();
        }
        return ri;
    }

    @Override
    public void delete(int recipeId) throws SQLException {
        String sql = "DELETE FROM recipe_ingredients WHERE recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public List<RecipeIngredient> findByRecipeId(int recipeId) throws SQLException {
        List<RecipeIngredient> list = new ArrayList<>();
        String sql = "SELECT * FROM recipe_ingredients WHERE recipe_id = ?";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recipeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new RecipeIngredient(
                        rs.getInt("recipe_id"),
                        rs.getInt("ingredient_id"),
                        rs.getInt("quantity"),
                        rs.getString("unit")
                ));
            }
        }
        return list;
    }

    @Override
    public void deleteByRecipeId(int recipeId) throws SQLException {
        delete(recipeId);
    }

    @Override
    public void removeIngredient(int recipeId, int ingredientId) throws SQLException {
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
}
