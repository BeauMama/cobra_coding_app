package com.example.application;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class IngredientName {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @NonNull
    public String name;
}