package com.mycompany.recipeaggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class RecipeDAO {

    private final String url;
    
    public RecipeDAO(String url) {
        this.url = url;

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver SQlite carregado com sucesso.");
            createTable();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erro ao inicializar o DAO: " + e.getMessage(), e);
        }

        System.out.println("Iniciando RecipeDAO");

    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS recipes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "ingredients TEXT, "
                + "preparationTime INTEGER, "
                + "portions INTEGER)";
        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }

    public void insert(Recipe recipe) throws SQLException{
        System.out.println("Inserindo receita: " + recipe.getName());
        String sql = "INSERT INTO recipes(name, description, ingredients, preparationTime, portions) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ObjectMapper mapper = new ObjectMapper();
            String ingredientsJson;
            try {
                ingredientsJson = mapper.writeValueAsString(recipe.getIngredients());
            } catch (JsonProcessingException e) {
                throw new SQLException("Erro ao converter ingredientes para JSON", e);
            }

            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, ingredientsJson);
            pstmt.setInt(4, recipe.getPreparationTime());
            pstmt.setInt(5, recipe.getPortions());
            pstmt.executeUpdate();
        }
    }

    private List<String> parseIngredients(String input) {
        if (input == null || input.isBlank()) {
            return new ArrayList<>();
        }
        input = input.trim().toLowerCase();

        input = input.replaceAll("\\s+e\\s+", ",");

        String[] itens = input.split(",");

        List<String> ingredients = Arrays.stream(itens)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1))
                .toList();

                return ingredients;
    }

    public List<Recipe> list() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                List<String> ingredients;
                try {
                    String ingredientsStr = rs.getString("ingredients");
                    ingredients = parseIngredients(ingredientsStr);
                } catch (Exception e) {
                    throw new SQLException("Erro ao converter ingredientes do banco", e);
                }
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        ingredients,
                        rs.getInt("preparationTime"),
                        rs.getInt("portions")
                );
                recipes.add(recipe);
                System.out.println("Encontrada a receita: " + rs.getString("name"));
            }
        }
        System.out.println("Listando receitas");

        return recipes;
    }

    public void update(Recipe recipe) throws SQLException {
        String sql = "UPDATE recipes SET name = ?, description = ?, ingredients = ?, preparationTime = ?, portions = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ObjectMapper mapper = new ObjectMapper();
            String ingredientsJson;
            try {
                ingredientsJson = mapper.writeValueAsString(recipe.getIngredients());
            } catch (JsonProcessingException e) {
                throw new SQLException("Erro ao converter ingredientes para JSON", e);
            }

            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, ingredientsJson);
            pstmt.setInt(4, recipe.getPreparationTime());
            pstmt.setInt(5, recipe.getPortions());
            pstmt.setInt(6, recipe.getId());
            pstmt.executeUpdate();
        }

    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

}
