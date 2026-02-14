package com.mycompany.recipeaggregator.repository;

import java.sql.SQLException;

public interface RecipeIngredientRepository {

    void addIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException;

    void removeIngredient(int recipeId, int ingredientId)
            throws SQLException;

    void updateIngredient(int recipeId, int ingredientId, int quantity, String unit)
            throws SQLException;
}

