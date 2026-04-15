package com.mycompany.recipeaggregator.dto;

import com.mycompany.recipeaggregator.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    public static Ingredient toEntity(IngredientCreateDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        return ingredient;
    }

    public static IngredientResponseDTO toDTO(Ingredient entity) {
        return new IngredientResponseDTO(entity.getId(), entity.getName());
    }

    public static RecipeIngredient toEntity(RecipeIngredientDTO dto, Recipe recipe, Ingredient ingredient) {
        RecipeIngredient ri = new RecipeIngredient();

        ri.setRecipe(recipe);
        ri.setIngredient(ingredient);
        ri.setQuantity(dto.getQuantity());
        ri.setUnit(dto.getUnit());

        return ri;
    }

    public static RecipeIngredientDTO toDTO(RecipeIngredient entity) {
        return new RecipeIngredientDTO(
                entity.getIngredient() != null ? entity.getIngredient().getId() : 0,
                entity.getQuantity(),
                entity.getUnit()
        );
    }

    public static Recipe toEntity(RecipeCreateDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setPortions(dto.getPortions());

        if (dto.getIngredients() != null) {
            List<RecipeIngredient> ingredients = dto.getIngredients().stream()
                    .map(riDto -> {
                        Ingredient ingredient = new Ingredient();
                        ingredient.setId(riDto.getIngredientId());
                        return new RecipeIngredient(recipe, ingredient, riDto.getQuantity(), riDto.getUnit());
                    })
                    .collect(Collectors.toList());
            recipe.setIngredients(ingredients);
        }

        return recipe;
    }

    public static RecipeResponseDTO toDTO(Recipe entity) {
        List<RecipeIngredientDTO> ingredientDTOs = entity.getIngredients()
                .stream()
                .map(Mapper::toDTO)
                .toList();

        RecipeResponseDTO dto = new RecipeResponseDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setIngredients(ingredientDTOs);
        dto.setPreparationTime(entity.getPreparationTime());
        dto.setPortions(entity.getPortions());

        return dto;
    }

}
