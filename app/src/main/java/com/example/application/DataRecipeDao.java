package com.example.application;

import androidx.room.Query;

import java.util.List;

public interface DataRecipeDao {
    @Query("SELECT * FROM Recipe")
    List<Recipe> getAll();
}
