package com.mycompany.recipeaggregator.repository;

import com.mycompany.recipeaggregator.models.Recipe;

import java.sql.SQLException;
import java.util.List;

public interface RecipeRepository {
    List<Recipe> list() throws SQLException;
    void insert(Recipe recipe) throws SQLException;
    void update(Recipe recipe) throws SQLException;
    void delete(int id) throws SQLException;
}
