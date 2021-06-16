package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Ingredient")
public class DataIngredient {
    @PrimaryKey(autoGenerate = true)
    public int ingredientId;
    public int recipeId;
    public int ingredientNameId;
    public float quantity;
    public String measurement;
    public String newMeasurement;
}