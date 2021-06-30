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

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    RecipeWithIngredients getRecipesWithIngredientsById(int id);

    @Query("SELECT DISTINCT name FROM Ingredient ORDER BY name")
    List<String> getIngredientNames();

    @Insert(entity = RecipeWithIngredients.class)
    public long insertRecipeWithIngredients(RecipeWithIngredients recipeWithIngredients);
}