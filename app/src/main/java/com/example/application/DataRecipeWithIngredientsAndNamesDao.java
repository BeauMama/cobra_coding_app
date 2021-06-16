package com.example.application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DataRecipeWithIngredientsAndNamesDao {
    @Transaction
    @Query("SELECT * FROM Recipe")
    public List<DataRecipeWithIngredientsAndNames> getAll();

    @Insert(entity = DataRecipe.class)
    public void insertRecipe(Recipe recipe);

}
