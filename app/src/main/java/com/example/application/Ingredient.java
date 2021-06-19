package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ingredient {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int recipeId;
    public float quantity;
    @NonNull
    public String name;
    @NonNull
    public String measurement;
    @NonNull
    public String conversionMeasurement;
    @NonNull
    public Boolean conversionIngredient;
}