package com.example.application.model;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.example.application.BR;
import com.example.application.activity.RecipeActivity;
import java.text.DecimalFormat;

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

    @Bindable
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public double getQuantity() {
        return quantity;
    }
    public void setQuantity(double quantity) {
        this.quantity = quantity;
        try {
            for (Ingredient ingredient : recipeWithIngredients.ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
    }

    public String getQuantityString() {
        if (getQuantity() == 0) {
            return null;
        } else {
            double value = getQuantity();
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(value);
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

        boolean hasConversionIngredient = false;

        // Don't show a converted quantity if the user does not have a measurement selected for the one ingredient
        if (getRecipeWithIngredients().recipe.getConversionType().toLowerCase().equals("one ingredient")) {
            for (Ingredient ingredient : getRecipeWithIngredients().ingredients) {
                if (ingredient.getIsConversionIngredient()) {
                    hasConversionIngredient = true;
                    if (ingredient.getMeasurement().toLowerCase().equals("select") ||
                             ingredient.getConversionMeasurement().toLowerCase().equals("select")) {
                        return "0";
                    }
                }
            }
            if (!hasConversionIngredient) {
                return "0";
            }
        }

        if (getMeasurement().toLowerCase().equals("select") || getConversionMeasurement().toLowerCase().equals("select")) {
            return "0";
        }

        try {
            double value = RecipeActivity.convertMeasurement(getQuantityIncreaseDecrease(), getMeasurement(), getConversionMeasurement());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(value);
        } catch (Exception e) {
            return "";
        }
    }
    public void setQuantityConvertedString(String string) { }

    @Bindable
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
        notifyPropertyChanged(BR.measurement);
    }

    @Bindable
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
        notifyPropertyChanged(BR.conversionMeasurement);
    }

    @Bindable
    public Boolean getIsConversionIngredient() {
        return isConversionIngredient;
    }
    public void setIsConversionIngredient(Boolean isConversionIngredient) {
        this.isConversionIngredient = isConversionIngredient;
        notifyPropertyChanged(BR.isConversionIngredient);
        notifyPropertyChanged(BR.quantityConvertedString);
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
        notifyPropertyChanged(BR.conversionIngredientQuantityString);
    }
    @Bindable
    public String getConversionIngredientQuantityString() {
        if (getConversionIngredientQuantity() == 0) {
            return null;
        } else {
            double value = RecipeActivity.convertMeasurement(getQuantityIncreaseDecrease(), getMeasurement(), getConversionMeasurement());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(value);
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