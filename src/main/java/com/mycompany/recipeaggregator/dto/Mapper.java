package com.mycompany.recipeaggregator.dto;

import com.mycompany.recipeaggregator.models.*;

import java.util.List;
import java.util.stream.Collectors;

public class Mapper {

    public static Ingredient toEntity(IngredientDTO dto) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(dto.getName());
        return ingredient;
    }

    public static IngredientDTO toDTO(Ingredient entity) {
        return new IngredientDTO(entity.getName());
    }

    public static RecipeIngredient toEntity(RecipeIngredientDTO dto, int recipeId) {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setRecipeId(recipeId);
        ri.setIngredientId(dto.getIngredientId());
        ri.setQuantity(dto.getQuantity());
        ri.setUnit(dto.getUnit());

        return ri;
    }

    public static RecipeIngredientDTO toDTO(RecipeIngredient entity) {
        return new RecipeIngredientDTO(
                entity.getIngredientId(),
                entity.getQuantity(),
                entity.getUnit()
        );
    }

    public static Recipe toEntity(RecipeCreateDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setIngredients(dto.getIngredients());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setPortions(dto.getPortions());

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
