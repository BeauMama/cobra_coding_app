package com.example.application.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM Recipe ORDER BY name")
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

    @Query("UPDATE Recipe " +
           "SET name = :name, " +
           "servingSize = :servingSize, " +
           "cookTimeMinutes = :cookTimeMinutes, " +
           "temperature = :temperature , " +
           "temperatureMeasurement =:temperatureMeasurement, " +
           "conversionTemperatureMeasurement =:conversionTemperatureMeasurement, " +
           "conversionType = :conversionType, " +
           "conversionAmount = :conversionAmount, " +
           "notes = :notes, " +
           "fromSystem = :fromSystem, " +
           "toSystem = :toSystem " +
           "WHERE id = :id")
    int updateRecipe(int id, String name, int servingSize, int cookTimeMinutes, int temperature,
        String temperatureMeasurement, String conversionTemperatureMeasurement, String conversionType,
        double conversionAmount, String notes, String fromSystem, String toSystem);

    @Query("DELETE FROM Recipe WHERE id = :recipeId")
    void deleteRecipeById(int recipeId);

    @Query("DELETE FROM Ingredient WHERE recipeId = :recipeId")
    void deleteIngredientsByRecipeId(int recipeId);

}