package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.Mapper;
import com.mycompany.recipeaggregator.dto.RecipeCreateDTO;
import com.mycompany.recipeaggregator.dto.RecipeResponseDTO;
import com.mycompany.recipeaggregator.models.Recipe;
import com.mycompany.recipeaggregator.repository.RecipeRepository;

import java.sql.SQLException;
import java.util.List;

public class RecipeService {
    private final RecipeRepository repository;

    public RecipeService(RecipeRepository repository) {
        this.repository = repository;
    }

    public List<RecipeResponseDTO> listRecipes() throws SQLException {
        List<Recipe> recipes = repository.list();

        return recipes.stream()
                .map(Mapper::toDTO)
                .toList();
    }


    public Recipe createRecipe(RecipeCreateDTO dto) throws SQLException {
        if (dto.getName() == null || dto.getName().isBlank()) {
            throw new IllegalArgumentException("Recipe name is required");
        }

        Recipe recipe = Mapper.toEntity(dto);

        repository.insert(recipe);

        return recipe;
    }

    public void updateRecipe(int id, RecipeCreateDTO dto) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        if (dto == null) {
            throw new IllegalArgumentException("Invalid data");
        }

        Recipe recipe = Mapper.toEntity(dto);
        recipe.setId(id);

        repository.update(recipe);
    }

    public void deleteRecipe(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid id");
        }

        repository.delete(id);
    }
}
