package com.example.application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "Recipe")
public class DataRecipe {
    @PrimaryKey(autoGenerate = true)
    public int recipeId;
    @NonNull
    public String name;
    public String notes;
    public String temperature;
    public float servingSize;
    public float conversionAmount;
}