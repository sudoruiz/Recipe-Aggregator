package com.mycompany.recipeaggregator.repository;

import com.mycompany.recipeaggregator.models.RecipeIngredient;

import java.sql.SQLException;
import java.util.List;

public interface RecipeIngredientRepository {

    void addIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException;

    void removeIngredient(int recipeId, int ingredientId)
            throws SQLException;

    void updateIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException;

    List<RecipeIngredient> findByRecipeId(int recipeId) throws SQLException;
}

