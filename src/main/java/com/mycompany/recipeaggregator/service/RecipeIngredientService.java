package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.RecipeIngredientPatchDTO;
import com.mycompany.recipeaggregator.models.RecipeIngredient;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;

import java.sql.SQLException;
import java.util.List;

public class RecipeIngredientService {

    private final RecipeIngredientRepository repository;

    public RecipeIngredientService(RecipeIngredientRepository repository) {
        this.repository = repository;
    }

    public List<RecipeIngredient> patchIngredients(int recipeId, RecipeIngredientPatchDTO dto)
            throws SQLException {

        if (recipeId <= 0) {
            throw new IllegalArgumentException("Invalid recipe id");
        }

        if (dto == null) {
            throw new IllegalArgumentException("Invalid data");
        }

        if (dto.getAction() == null || dto.getAction().isBlank()) {
            throw new IllegalArgumentException("Action is required");
        }

        if (dto.getIngredientId() <= 0) {
            throw new IllegalArgumentException("Invalid ingredient id");
        }

        switch (dto.getAction()) {

            case "add" -> {
                validateQuantity(dto);
                RecipeIngredient ri = new RecipeIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );
                repository.insert(ri);
            }

            case "remove" -> {
                repository.removeIngredient(
                        recipeId,
                        dto.getIngredientId()
                );
            }

            case "update" -> {
                validateQuantity(dto);
                RecipeIngredient ri = new RecipeIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );
                repository.update(ri);
            }

            default -> throw new IllegalArgumentException("Invalid action");
        }
        return repository.findByRecipeId(recipeId);
    }

    private void validateQuantity(RecipeIngredientPatchDTO dto) {

        if (dto.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (dto.getUnit() == null || dto.getUnit().isBlank()) {
            throw new IllegalArgumentException("Unit is required");
        }
    }
}

