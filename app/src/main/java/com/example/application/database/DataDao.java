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
    @Query("SELECT * FROM Recipe ORDER BY id DESC")
    List<Recipe> getAllRecipes();

    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    RecipeWithIngredients getRecipeWithIngredientsById(int id);

    @Query("SELECT DISTINCT name FROM Ingredient ORDER BY name")
    List<String> getIngredientNames();

    @Insert(entity = Recipe.class)
    long insertRecipe(Recipe recipe);

    @Insert(entity = Ingredient.class)
    long insertIngredient(Ingredient ingredient);

    @Query("UPDATE Recipe SET name = :name WHERE id = :id")
    int updateRecipe(int id, String name);
}