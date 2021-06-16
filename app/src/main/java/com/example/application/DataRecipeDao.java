package com.example.application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataRecipeDao {
    @Query("SELECT * FROM Recipe")
    //List<Recipe> getRecipeWithIngredients();
    List<DataRecipeWithIngredients> getRecipeWithIngredients();

    @Insert(entity = DataRecipe.class)
    public void insertRecipe(DataRecipe dataRecipe);

}
