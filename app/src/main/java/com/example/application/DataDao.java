package com.example.application;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DataDao {
    @Transaction
    @Query("SELECT * FROM Recipe")
    List<RecipeWithIngredients> getRecipeWithIngredients();

    @Insert(entity = Recipe.class)
    public long insertRecipe(Recipe recipe);

    @Insert(entity = Ingredient.class)
    public long insertIngredient(Ingredient ingredient);

}
