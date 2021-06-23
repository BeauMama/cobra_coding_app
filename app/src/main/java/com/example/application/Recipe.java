package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Recipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
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
}