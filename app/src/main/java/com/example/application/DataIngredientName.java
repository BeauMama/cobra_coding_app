package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "IngredientName")
public class DataIngredientName {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String name;
}