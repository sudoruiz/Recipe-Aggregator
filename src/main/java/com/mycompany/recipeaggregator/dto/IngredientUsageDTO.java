package com.mycompany.recipeaggregator.dto;

public class IngredientUsageDTO {

    private int id;
    private String name;
    private int usageCount;

    public IngredientUsageDTO(int id, String name, int usageCount) {
        this.id = id;
        this.name = name;
        this.usageCount = usageCount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getUsageCount() {
        return usageCount;
    }
}
