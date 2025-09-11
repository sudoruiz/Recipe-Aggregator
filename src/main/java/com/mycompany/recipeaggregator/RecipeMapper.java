package com.mycompany.recipeaggregator;

public class RecipeMapper {

    public static Recipe toEntity(RecipeDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setIngredients(dto.getIngredients());
        recipe.setPreparationTime(dto.getPreparationTime());
        recipe.setPortions(dto.getPortions());

        return recipe;
    }

    public static RecipeDTO toDTO(Recipe entity) {
        return new RecipeDTO(
                entity.getName(),
                entity.getDescription(),
                entity.getIngredients(),
                entity.getPreparationTime(),
                entity.getPortions()
        );
    }

}
