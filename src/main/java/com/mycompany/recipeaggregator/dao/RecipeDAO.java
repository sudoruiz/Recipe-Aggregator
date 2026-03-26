package com.mycompany.recipeaggregator.dao;

import com.mycompany.recipeaggregator.models.Recipe;
import com.mycompany.recipeaggregator.models.RecipeIngredient;
import com.mycompany.recipeaggregator.repository.CrudRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeDAO implements CrudRepository<Recipe> {

    private final String url;

    public RecipeDAO(String url) {
        this.url = url;

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver SQlite loaded successfully.");
            createTable();

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error to start DAO: " + e.getMessage(), e);
        }

        System.out.println("Starting RecipeDAO");

    }

    public void createTable() throws SQLException {
        String sqlRecipes = "CREATE TABLE IF NOT EXISTS recipes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "preparationTime INTEGER, "
                + "portions INTEGER)";

        String sqlIngredients = "CREATE TABLE IF NOT EXISTS ingredients ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL)";

        String sqlRecipeIngredients = "CREATE TABLE IF NOT EXISTS recipe_ingredients ("
                + "recipe_id INTEGER NOT NULL, "
                + "ingredient_id INTEGER NOT NULL, "
                + "quantity INTEGER NOT NULL, "
                + "unit TEXT NOT NULL, "
                + "PRIMARY KEY (recipe_id, ingredient_id), "
                + "FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE, "
                + "FOREIGN KEY (ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE)";


        try (Connection conn = DriverManager.getConnection(url); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlRecipes);
            stmt.execute(sqlIngredients);
            stmt.execute(sqlRecipeIngredients);
        }
    }

    @Override
    public Recipe insert(Recipe recipe) throws SQLException {
        String sql = "INSERT INTO recipes(name, description, preparationTime, portions) VALUES(?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setInt(3, recipe.getPreparationTime());
            pstmt.setInt(4, recipe.getPortions());
            pstmt.executeUpdate();

            ResultSet keys = pstmt.getGeneratedKeys();
            if (keys.next()) {
                int recipeId = keys.getInt(1);
                recipe.setId(recipeId);

                RecipeIngredientDAO riDAO = new RecipeIngredientDAO(url);
                for (RecipeIngredient ri : recipe.getIngredients()) {
                    ri.setRecipeId(recipeId);
                    riDAO.insert(ri);
                }
            }
        }
        return recipe;
    }

    public List<Recipe> list() throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                List<RecipeIngredient> ingredients = new ArrayList<>();
                try {
                    RecipeIngredientDAO riDAO = new RecipeIngredientDAO(url);
                    ingredients = riDAO.findByRecipeId(rs.getInt("id"));
                } catch (Exception e) {
                    throw new SQLException("Error to convert ingredients of database", e);
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
                System.out.println("Finded recipe: " + rs.getString("name"));
            }
        }
        System.out.println("Listing recipes");

        return recipes;
    }

    @Override
    public Recipe update(Recipe recipe) throws SQLException {
        String sql = "UPDATE recipes SET name = ?, description = ?, preparationTime = ?, portions = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setInt(3, recipe.getPreparationTime());
            pstmt.setInt(4, recipe.getPortions());
            pstmt.setInt(5, recipe.getId());
            pstmt.executeUpdate();
        }
        return recipe;
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}
