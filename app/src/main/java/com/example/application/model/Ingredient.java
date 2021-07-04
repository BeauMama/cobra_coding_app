package com.example.application.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.application.BR;

import java.util.List;

@Entity
public class Ingredient extends BaseObservable {
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

    public transient Recipe recipe;

    @Ignore
    public Recipe getRecipe() {
        return recipe;
    }

    @Ignore
    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

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

    @Bindable
    public void setQuantity(float quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantityString);
        notifyPropertyChanged(BR.quantityIncreaseDecreaseString);
    }

    @Bindable
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

    @Bindable
    public String getQuantityIncreaseDecreaseString() {
        float quantityConverted = getQuantity();

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

                    /* This is not implemented yet.
                    List<Ingredient> ingredients = recipeWithIngredients.ingredients;
                    for (Ingredient ingredient : ingredients) {
                        if (ingredient.getIsConversionIngredient() && ingredient.getQuantity() != 0) {
                            quantityConverted = getQuantity() * ingredient.getConversionIngredientQuantity() / ingredient.getQuantity();
                            break;
                        }
                    }
                    */
                }
                break;
        }

        if (quantityConverted == 0) {
            return null;
        } else {
            return Float.toString(quantityConverted);
        }
    }


    public void setQuantityIncreaseDecreaseString(String string) {

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