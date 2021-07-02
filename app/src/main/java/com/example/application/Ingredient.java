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
    private float calculatedConvertedQuantity;
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
    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }
    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getQuantityString() {
        if (getQuantity() == 0) {
            return null;
        } else {
            return Float.toString(getQuantity());
        }
    }
    public void setQuantityString(String setQuantity) {
        try {
            float val = Float.parseFloat(setQuantity);
            this.setQuantity(val);
        } catch(NumberFormatException ex) {
            this.setQuantity(0);
        }
    }

    public float getCalculatedConvertedQuantity() {
        return getQuantity() * 2;
        //return calculatedConvertedQuantity;
    }

    public void setCalculatedConvertedQuantity(float calculatedConvertedQuantity) {
        this.calculatedConvertedQuantity = getQuantity() * 2;
        //this.calculatedConvertedQuantity = calculatedConvertedQuantity;
    }

    public String getCalculatedConvertedQuantityString() {
        if (getCalculatedConvertedQuantity() == 0) {
            return null;
        } else {
            return Float.toString(getCalculatedConvertedQuantity());
        }
    }
    public void setCalculatedConvertedQuantityString(String setCalculatedConvertedQuantity) {
        try {
            float val = Float.parseFloat(setCalculatedConvertedQuantity);
            this.setCalculatedConvertedQuantity(val);
        } catch(NumberFormatException ex) {
            this.setCalculatedConvertedQuantity(0);
        }
    }

    public float getQuantityConverted(RecipeWithIngredients recipeWithIngredients) {
        float quantityConverted = getQuantity();
        Recipe recipe = recipeWithIngredients.recipe;

        switch (recipe.getConversionType().toLowerCase()) {
            case "multiply by":
                quantityConverted = getQuantity() * recipe.getConversionAmount();
                break;
            case "divide by":
                if (recipe.getConversionAmount() != 0) {
                    quantityConverted = getQuantity() / recipe.getConversionAmount();
                }
                break;
            case "servings":
                if (recipe.getServingSize() != 0) {
                    quantityConverted = getQuantity() * recipe.getConversionAmount() / recipe.getServingSize();
                }
                break;
            case "one ingredient":
                if(getIsConversionIngredient()) {
                    // Don't convert the quantity if this ingredient is the conversion ingredient.
                    quantityConverted = getConversionIngredientQuantity();
                } else {
                    List<Ingredient> ingredients = recipeWithIngredients.ingredients;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getIsConversionIngredient() && ingredient.getQuantity() != 0) {
                            quantityConverted = getQuantity() * ingredient.getConversionIngredientQuantity() / ingredient.getQuantity();
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
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }

    public String getConversionMeasurement() {
        return conversionMeasurement;
    }
    public void setConversionMeasurement(String conversionMeasurement) {
        this.conversionMeasurement = conversionMeasurement;
    }

    public Boolean getIsConversionIngredient() {
        return isConversionIngredient;
    }
    public void setIsConversionIngredient(Boolean isConversionIngredient) {
        this.isConversionIngredient = isConversionIngredient;
    }

    public float getConversionIngredientQuantity() {
        return conversionIngredientQuantity;
    }
    public void setConversionIngredientQuantity(float conversionIngredientQuantity) {
        this.conversionIngredientQuantity = conversionIngredientQuantity;
    }

    public String getConversionIngredientQuantityString() {
        if (getConversionIngredientQuantity() == 0) {
            return null;
        } else {
            return Float.toString(getConversionIngredientQuantity());
        }
    }
    public void setConversionIngredientQuantityString(String setConversionIngredientQuantity) {
        try {
            float val = Float.parseFloat(setConversionIngredientQuantity);
            this.setConversionIngredientQuantity(val);
        } catch(NumberFormatException ex) {
            this.setConversionIngredientQuantity(0);
        }
    }
}