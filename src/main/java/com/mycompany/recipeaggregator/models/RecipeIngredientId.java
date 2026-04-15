package com.mycompany.recipeaggregator.models;
import java.io.Serializable;
import java.util.Objects;

public class RecipeIngredientId implements Serializable {

    private int recipe;
    private int ingredient;

    public RecipeIngredientId() {}

    public RecipeIngredientId(int recipe, int ingredient) {
        this.recipe = recipe;
        this.ingredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RecipeIngredientId)) return false;
        RecipeIngredientId that = (RecipeIngredientId) o;
        return recipe == that.recipe && ingredient == that.ingredient;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipe, ingredient);
    }
}
