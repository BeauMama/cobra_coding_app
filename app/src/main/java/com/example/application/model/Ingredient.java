package com.example.application.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.application.BR;
import com.example.application.activity.RecipeActivity;

import java.util.List;

@Entity
public class Ingredient extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int recipeId;
    @NonNull
    private String name;
    private double quantity;
    @NonNull
    private String measurement;
    @NonNull
    private String conversionMeasurement;
    @NonNull
    private Boolean isConversionIngredient;
    private double conversionIngredientQuantity;

    public transient RecipeWithIngredients recipeWithIngredients;

    @Ignore
    public RecipeWithIngredients getRecipeWithIngredients() {
        return recipeWithIngredients;
    }

    @Ignore
    public void setRecipeWithIngredients(RecipeWithIngredients recipeWithIngredients) {
        this.recipeWithIngredients = recipeWithIngredients;
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

    public double getQuantity() {
        return quantity;
    }


    public void setQuantity(double quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantityConvertedString);
    }

    public String getQuantityString() {
        if (getQuantity() == 0) {
            return null;
        } else {
            return Double.toString(getQuantity());
        }
    }
    public void setQuantityString(String setQuantity) {
        try {
            double val = Double.parseDouble(setQuantity);
            this.setQuantity(val);
        } catch(NumberFormatException ex) {
            this.setQuantity(0);
        }
    }

    public double getQuantityIncreaseDecrease() {

        double quantityConverted = getQuantity();

        switch (getRecipeWithIngredients().recipe.getConversionType().toLowerCase()) {
            case "multiply by":
                quantityConverted = getQuantity() * getRecipeWithIngredients().recipe.getConversionAmount();
                break;
            case "divide by":
                if (getRecipeWithIngredients().recipe.getConversionAmount() != 0) {
                    quantityConverted = getQuantity() / getRecipeWithIngredients().recipe.getConversionAmount();
                }
                break;
            case "servings":
                if (getRecipeWithIngredients().recipe.getServingSize() != 0) {
                    quantityConverted = getQuantity() * getRecipeWithIngredients().recipe.getConversionAmount() / getRecipeWithIngredients().recipe.getServingSize();
                }
                break;
            case "one ingredient":
                if(getIsConversionIngredient()) {
                    // Don't convert the quantity if this ingredient is the conversion ingredient.
                    quantityConverted = getConversionIngredientQuantity();
                } else {
                    for (Ingredient ingredient : getRecipeWithIngredients().ingredients) {
                        if (ingredient.getIsConversionIngredient() && ingredient.getQuantity() != 0) {
                            quantityConverted = getQuantity() *
                                    RecipeActivity.convertMeasurement(ingredient.getConversionIngredientQuantity(),
                                            ingredient.getConversionMeasurement(), ingredient.getMeasurement()) /
                                    ingredient.getQuantity();
                            break;
                        }
                    }
                }
                break;
        }
        return quantityConverted;
    }

    @Bindable
    public String getQuantityConvertedString() {
        try {
            return Double.toString(RecipeActivity.convertMeasurement(getQuantityIncreaseDecrease(), getMeasurement(), getConversionMeasurement()));
        }
        catch (Exception e) {
            return "";
        }
    }

    public void setQuantityConvertedString(String string) { }

    public String getMeasurement() {
        return measurement;
    }
    public void setMeasurement(String measurement) {
        this.measurement = measurement;
        try {
            for (Ingredient ingredient : recipeWithIngredients.ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
    }

    public String getConversionMeasurement() {
        return conversionMeasurement;
    }

    public void setConversionMeasurement(String conversionMeasurement) {
        this.conversionMeasurement = conversionMeasurement;
        try {
            for (Ingredient ingredient : recipeWithIngredients.ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
    }

    @Bindable
    public Boolean getIsConversionIngredient() {
        return isConversionIngredient;
    }
    public void setIsConversionIngredient(Boolean isConversionIngredient) {
        this.isConversionIngredient = isConversionIngredient;
        notifyPropertyChanged(BR.quantityConvertedString);
        notifyPropertyChanged(BR.isConversionIngredient);
    }

    public double getConversionIngredientQuantity() {
        return conversionIngredientQuantity;
    }
    public void setConversionIngredientQuantity(double conversionIngredientQuantity) {
        this.conversionIngredientQuantity = conversionIngredientQuantity;

        try {
            for (Ingredient ingredient : recipeWithIngredients.ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
    }

    public String getConversionIngredientQuantityString() {
        if (getConversionIngredientQuantity() == 0) {
            return null;
        } else {
            return Double.toString(getConversionIngredientQuantity());
        }
    }
    public void setConversionIngredientQuantityString(String setConversionIngredientQuantity) {
        try {
            double val = Double.parseDouble(setConversionIngredientQuantity);
            this.setConversionIngredientQuantity(val);
        } catch(NumberFormatException ex) {
            this.setConversionIngredientQuantity(0);
        }
    }
}