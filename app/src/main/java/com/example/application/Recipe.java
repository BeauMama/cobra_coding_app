package com.example.application;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    public String name;
    public int servingSize;
    public int cookTimeMinutes;
    public int temperature;
    public String temperatureMeasurement;
    public String conversionTemperatureMeasurement;
    @NonNull
    public String conversionType;
    public float conversionAmount;
    public String notes;
    public String fromSystem;
    public String toSystem;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServingSize() {
        return servingSize;
    }

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
            this.setServingSize(0); //default value
        }
    }

    public int getCookTimeMinutes() {
        return cookTimeMinutes;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getTemperatureMeasurement() {
        return temperatureMeasurement;
    }

    public String getConversionTemperatureMeasurement() {
        return conversionTemperatureMeasurement;
    }

    public String getConversionType() {
        return conversionType;
    }

    public float getConversionAmount() {
        return conversionAmount;
    }

    public String getNotes() {
        return notes;
    }

    public String getFromSystem() {
        return fromSystem;
    }

    public String getToSystem() {
        return toSystem;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }

    public void setCookTimeMinutes(int cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public void setTemperatureMeasurement(String temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }

    public void setConversionTemperatureMeasurement(String conversionTemperatureMeasurement) {
        this.conversionTemperatureMeasurement = conversionTemperatureMeasurement;
    }

    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    public void setConversionAmount(float conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setFromSystem(String fromSystem) {
        this.fromSystem = fromSystem;
    }

    public void setToSystem(String toSystem) {
        this.toSystem = toSystem;
    }
}