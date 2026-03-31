package com.mycompany.recipeaggregator.repository;

import com.mycompany.recipeaggregator.models.RecipeIngredient;

import java.sql.SQLException;
import java.util.List;

public interface RecipeIngredientRepository extends CrudRepository<RecipeIngredient> {

    List<RecipeIngredient> findByRecipeId(int recipeId) throws SQLException;

    void removeIngredient(int recipeId, int ingredientId) throws SQLException;
}

