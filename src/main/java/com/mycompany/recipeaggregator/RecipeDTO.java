package com.mycompany.recipeaggregator;

public class RecipeDTO {

    private String name;
    private String description;
    private String ingredients;
    private int preparationTime;
    private int portions;

    public RecipeDTO() {
    }

    public RecipeDTO(String name, String description, String ingredients, int preparationTime, int portions) {
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

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
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
