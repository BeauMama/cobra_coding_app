package com.example.application.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.application.Ingredient;
import com.example.application.Recipe;
import com.example.application.RecipeWithIngredients;

import java.util.List;

@Dao
public interface DataDao {
    @Transaction
    @Query("SELECT * FROM Recipe")
    List<RecipeWithIngredients> getAllRecipesWithIngredients();

    @Query("SELECT * FROM Recipe ORDER BY id DESC")
    List<Recipe> getAllRecipes();

    @Query("SELECT DISTINCT name FROM Ingredient ORDER BY name")
    List<String> getIngredientNames();

    @Insert(entity = Recipe.class)
    public long insertRecipe(Recipe recipe);

    @Insert(entity = Ingredient.class)
    public long insertIngredient(Ingredient ingredient);
}
