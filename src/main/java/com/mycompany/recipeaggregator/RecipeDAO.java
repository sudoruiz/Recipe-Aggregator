package com.mycompany.recipeaggregator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class RecipeDAO {

    private static final String URL = "jdbc:sqlite:/home/renan/recipe-data/recipes.db";

    public RecipeDAO() {

        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("Driver SQlite carregado com sucesso.");
        } catch (ClassNotFoundException e) {
            System.out.println("Erro ao carregar o arquivo JDBC" + e.getMessage());
        }

        System.out.println("Iniciando RecipeDAO");
        createTable();

    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS recipes ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "description TEXT, "
                + "ingredients TEXT, "
                + "preparationTime INTEGER, "
                + "portions INTEGER)";
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela recipes verificada/criada com sucesso.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar a tabela: " + e.getMessage());
        }
    }

    public void insert(Recipe recipe) {
        System.out.println("Inserindo receita: " + recipe.getName());
        String sql = "INSERT INTO recipes(name, description, ingredients, preparationTime, portions) VALUES(?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, recipe.getIngredients());
            pstmt.setInt(4, recipe.getPreparationTime());
            pstmt.setInt(5, recipe.getPortions());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao inserir receita: " + e.getMessage());
        }
        System.out.println("Receita inserida com sucesso");
    }

    //Listar receitas
    public List<Recipe> list() {
        List<Recipe> recipes = new ArrayList<>();
        String sql = "SELECT * FROM recipes";
        try (Connection conn = DriverManager.getConnection(URL); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("ingredients"),
                        rs.getInt("preparationTime"),
                        rs.getInt("portions")
                );
                recipes.add(recipe);
                System.out.println("Encontrada a receita: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar receitas: " + e.getMessage());
        }
        System.out.println("Listando receitas");

        return recipes;
    }

    //Atualiza receita
    public void update(Recipe recipe) {
        String sql = "UPDATE recipes SET name = ?, description = ?, ingredients = ?, preparationTime = ?, portions = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, recipe.getName());
            pstmt.setString(2, recipe.getDescription());
            pstmt.setString(3, recipe.getIngredients());
            pstmt.setInt(4, recipe.getPreparationTime());
            pstmt.setInt(5, recipe.getPortions());
            pstmt.setInt(6, recipe.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar receita: " + e.getMessage());
        }

    }

    public void delete(int id) {
        String sql = "DELETE FROM recipes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erro ao deletar receita: " + e.getMessage());
        }
    }
}
