package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    public String name;
    private int servingSize;
    private int cookTimeMinutes;
    private int temperature;
    private String temperatureMeasurement;
    private String conversionTemperatureMeasurement;
    @NonNull
    private String conversionType;
    private float conversionAmount;
    private String notes;
    private String fromSystem;
    private String toSystem;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getServingSize() {
        return servingSize;
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