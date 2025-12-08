package com.mycompany.recipeaggregator.dto;

public class RecipeIngredientDTO {
    private int ingredientId;
    private int quantity;
    private String unit;

    public RecipeIngredientDTO() {}

    public RecipeIngredientDTO(int ingredientId, int quantity, String unit) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
