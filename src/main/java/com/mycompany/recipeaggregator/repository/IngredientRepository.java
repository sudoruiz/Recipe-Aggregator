package com.mycompany.recipeaggregator.repository;

import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.models.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient> {

    List<IngredientUsageDTO> listMostUsed() throws SQLException;
}
