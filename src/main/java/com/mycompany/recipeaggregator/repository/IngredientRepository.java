package com.mycompany.recipeaggregator.repository;

import com.mycompany.recipeaggregator.dto.IngredientCreateDTO;
import com.mycompany.recipeaggregator.dto.IngredientUsageDTO;
import com.mycompany.recipeaggregator.models.Ingredient;

import java.sql.SQLException;
import java.util.List;

public interface IngredientRepository {

    List<Ingredient> listAll() throws SQLException;

    List<IngredientUsageDTO> listMostUsed() throws SQLException;

    Ingredient create(Ingredient ingredient)  throws SQLException;

    void delete(int id) throws SQLException;
}
