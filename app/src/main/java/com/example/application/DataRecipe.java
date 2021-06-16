package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recipe")
public class DataRecipe {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String name;
    public String notes;
    @Nullable
    public float temperature;
    public String temperatureMeasurement;
    public String conversionTemperatureMeasurement;
    public float servingSize;
    public float conversionAmount;
    public float cookTime;
}