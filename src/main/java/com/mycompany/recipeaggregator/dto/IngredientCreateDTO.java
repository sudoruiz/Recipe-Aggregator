package com.mycompany.recipeaggregator.dto;

public class IngredientCreateDTO {

    private String name;

    public IngredientCreateDTO() {
    }

    public IngredientCreateDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
