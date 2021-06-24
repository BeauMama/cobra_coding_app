package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    @NonNull
    private String name;
    private float quantity;
    @NonNull
    private String measurement;
    @NonNull
    private String conversionMeasurement;
    @NonNull
    private Boolean isConversionIngredient;
    private float conversionIngredientQuantity;

    public int getId() {
        return id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public String getName() {
        return name;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getQuantityConverted(RecipeWithIngredients recipeWithIngredients) {
        float quantityConverted = quantity;
        Recipe recipe = recipeWithIngredients.recipe;

        switch (recipe.getConversionType().toLowerCase()) {
            case "multiply by":
                quantityConverted = quantity * recipe.getConversionAmount();
                break;
            case "divide by":
                if (recipe.getConversionAmount() != 0) {
                    quantityConverted = quantity / recipe.getConversionAmount();
                }
                break;
            case "servings":
                if (recipe.getServingSize() != 0) {
                    quantityConverted = quantity * recipe.getConversionAmount() / recipe.getServingSize();
                }
                break;
            case "one ingredient":
                if(isConversionIngredient) {
                    // Don't convert the quantity if this ingredient is the conversion ingredient.
                    quantityConverted = conversionIngredientQuantity;
                } else {
                    List<Ingredient> ingredients = recipeWithIngredients.ingredients;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getIsConversionIngredient() && ingredient.getQuantity() != 0) {
                            quantityConverted = quantity * ingredient.getConversionIngredientQuantity() / ingredient.getQuantity();
                            break;
                        }
                    }
                }
                break;
        }
        
        return quantityConverted;
    }

    public String getMeasurement() {
        return measurement;
    }

    public String getConversionMeasurement() {
        return conversionMeasurement;
    }

    public Boolean getIsConversionIngredient() {
        return isConversionIngredient;
    }

    public float getConversionIngredientQuantity() {
        return conversionIngredientQuantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public void setConversionMeasurement(String conversionMeasurement) {
        this.conversionMeasurement = conversionMeasurement;
    }

    public void setIsConversionIngredient(Boolean isConversionIngredient) {
        this.isConversionIngredient = isConversionIngredient;
    }

    public void setConversionIngredientQuantity(float conversionIngredientQuantity) {
        this.conversionIngredientQuantity = conversionIngredientQuantity;
    }
}