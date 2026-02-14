package com.mycompany.recipeaggregator.service;

import com.mycompany.recipeaggregator.dto.RecipeIngredientPatchDTO;
import com.mycompany.recipeaggregator.repository.RecipeIngredientRepository;

import java.sql.SQLException;

public class RecipeIngredientService {

    private final RecipeIngredientRepository repository;

    public RecipeIngredientService(RecipeIngredientRepository repository) {
        this.repository = repository;
    }

    public void patchIngredients(int recipeId, RecipeIngredientPatchDTO dto)
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
                repository.addIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );
            }

            case "remove" -> {
                repository.removeIngredient(
                        recipeId,
                        dto.getIngredientId()
                );
            }

            case "update" -> {
                validateQuantity(dto);
                repository.updateIngredient(
                        recipeId,
                        dto.getIngredientId(),
                        dto.getQuantity(),
                        dto.getUnit()
                );
            }

            default -> throw new IllegalArgumentException("Invalid action");
        }
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

