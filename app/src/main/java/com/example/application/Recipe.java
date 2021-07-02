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
    private String name;
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
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getServingSize() {
        return servingSize;
    }
    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
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
            this.setServingSize(0);
        }
    }

    public int getCookTimeMinutes() {
        return cookTimeMinutes;
    }
    public void setCookTimeMinutes(int cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
    }

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
    }

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

    public String getTemperatureMeasurement() {
        return temperatureMeasurement;
    }
    public void setTemperatureMeasurement(String temperatureMeasurement) {
        this.temperatureMeasurement = temperatureMeasurement;
    }

    public String getConversionTemperatureMeasurement() {
        return conversionTemperatureMeasurement;
    }
    public void setConversionTemperatureMeasurement(String conversionTemperatureMeasurement) {
        this.conversionTemperatureMeasurement = conversionTemperatureMeasurement;
    }

    public String getConversionType() {
        return conversionType;
    }
    public void setConversionType(String conversionType) {
        this.conversionType = conversionType;
    }

    public float getConversionAmount() {
        return conversionAmount;
    }
    public void setConversionAmount(float conversionAmount) {
        this.conversionAmount = conversionAmount;
    }

    public String getConversionAmountString() {
        if (getConversionAmount() == 0) {
            return null;
        } else {
            return Float.toString(getConversionAmount());
        }
    }
    public void setConversionAmountString(String setConversionAmount) {
        try {
            float val = Float.parseFloat(setConversionAmount);
            this.setConversionAmount(val);
        } catch(NumberFormatException ex) {
            this.setConversionAmount(0);
        }
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
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