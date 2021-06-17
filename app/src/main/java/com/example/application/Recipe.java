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
    public String notes;
    public int temperature;
    public String temperatureMeasurement;
    public String conversionTemperatureMeasurement;
    public int servingSize;
    public float conversionAmount;
    public int cookTimeMinutes;
}