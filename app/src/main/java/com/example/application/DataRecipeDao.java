package com.example.application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataRecipeDao {
    @Query("SELECT * FROM Recipe")
    List<Recipe> getAll();

    @Insert(entity = DataRecipe.class)
    public void insertRecipe(Recipe recipe);

}
