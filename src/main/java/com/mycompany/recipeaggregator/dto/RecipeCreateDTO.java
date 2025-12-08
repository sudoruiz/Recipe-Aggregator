package com.mycompany.recipeaggregator.dto;

import com.mycompany.recipeaggregator.models.RecipeIngredient;

import java.util.List;

public class RecipeCreateDTO {

    private String name;
    private String description;
    private List<RecipeIngredient> ingredients;
    private int preparationTime;
    private int portions;

    public RecipeCreateDTO() {}

    public RecipeCreateDTO(String name, String description, List<RecipeIngredient> ingredients, int preparationTime, int portions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.preparationTime = preparationTime;
        this.portions = portions;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeIngredient>  getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

}
