package com.example.application.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableInt;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.application.BR;
import com.example.application.activity.RecipeActivity;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

@Entity
public class Recipe extends BaseObservable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String name;
    private int servingSize;
    private int cookTimeMinutes;
    private int temperature;
    private String temperatureMeasurement;
    private String conversionTemperatureMeasurement;
    @NonNull
    private String conversionType;
    private double conversionAmount;
    private String notes;
    private String fromSystem;
    private String toSystem;

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

    @Bindable
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public int getServingSize() {
        return servingSize;
    }
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;

        try {
            for (Ingredient ingredient : getRecipeWithIngredients().ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
        notifyPropertyChanged(BR.servingSizeString);
    }

    @Bindable
    public String getServingSizeString() {
        if (getServingSize() == 0) {
            return null;
        } else {
            return Integer.toString(getServingSize());
        }
    }
    public void setServingSizeString(String servingSize) {
        try {
            int val = Integer.parseInt(servingSize);
            this.setServingSize(val);
        } catch(NumberFormatException ex) {
            this.setServingSize(0);
        }
    }


    public int getCookTimeMinutes() {
        return cookTimeMinutes;
    }
    public void setCookTimeMinutes(int cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
        notifyPropertyChanged(BR.cookTimeMinutesString);
    }
    @Bindable
    public String getCookTimeMinutesString() {
        if (getCookTimeMinutes() == 0) {
            return null;
        } else {
            return Integer.toString(getCookTimeMinutes());
        }
    }
    public void setCookTimeMinutesString(String cookTimeMinutes) {
        try {
            int val = Integer.parseInt(cookTimeMinutes);
            this.setCookTimeMinutes(val);
        } catch(NumberFormatException ex) {
            this.setCookTimeMinutes(0);
        }
    }

    public int getTemperature() {
        return temperature;
    }
    public void setTemperature(int temperature) {
        this.temperature = temperature;
        notifyPropertyChanged(BR.temperatureString);
        notifyPropertyChanged(BR.temperatureConvertedString);
    }
    @Bindable
    public String getTemperatureString() {
        if (getTemperature() == 0) {
            return null;
        } else {
            return Integer.toString(getTemperature());
        }
    }
    public void setTemperatureString(String setTemperature) {
        try {
            int val = Integer.parseInt(setTemperature);
            this.setTemperature(val);
        } catch(NumberFormatException ex) {
            this.setTemperature(0);
        }
    }

    @Bindable
    public String getTemperatureMeasurement() {
        return temperatureMeasurement;
    }
    public void setTemperatureMeasurement(String temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
        notifyPropertyChanged(BR.temperatureMeasurement);
        notifyPropertyChanged(BR.temperatureConvertedString);
    }

    @Bindable
    public String getConversionTemperatureMeasurement() {
        return conversionTemperatureMeasurement;
    }
    public void setConversionTemperatureMeasurement(String conversionTemperatureMeasurement) {
        this.conversionTemperatureMeasurement = conversionTemperatureMeasurement;
        notifyPropertyChanged(BR.conversionTemperatureMeasurement);
        notifyPropertyChanged(BR.temperatureConvertedString);
    }

    @Bindable
    public String getTemperatureConvertedString() {
        double value = RecipeActivity.convertMeasurement((double) getTemperature(), getTemperatureMeasurement(), getConversionTemperatureMeasurement());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }
    public void setTemperatureConvertedString(String string) { }

    @Bindable
    public String getConversionType() {
        return conversionType;
    }
    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;

        try {
            for (Ingredient ingredient : getRecipeWithIngredients().ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
        notifyPropertyChanged(BR.conversionType);
    }

    public double getConversionAmount() {
        return conversionAmount;
    }
    public void setConversionAmount(double conversionAmount) {
        this.conversionAmount = conversionAmount;

        try {
            for (Ingredient ingredient : getRecipeWithIngredients().ingredients) {
                ingredient.notifyPropertyChanged(BR.quantityConvertedString);
            }
        }
        catch (Exception e) {
        }
        notifyPropertyChanged(BR.conversionAmountString);
    }
    @Bindable
    public String getConversionAmountString() {
        if (getConversionAmount() == 0) {
            return null;
        } else {
            double value = getConversionAmount();
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            return decimalFormat.format(value);
        }
    }
    public void setConversionAmountString(String setConversionAmount) {
        try {
            double val = Double.parseDouble(setConversionAmount);
            this.setConversionAmount(val);
        } catch(NumberFormatException ex) {
            this.setConversionAmount(0);
        }
    }

    @Bindable
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
        notifyPropertyChanged(BR.notes);
    }

    public String getFromSystem() {
        return fromSystem;
    }
    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public String getToSystem() {
        return toSystem;
    }
    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }
}