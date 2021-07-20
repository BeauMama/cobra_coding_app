package com.example.application.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;
import java.util.List;

/**
 * Data access objects to interact with the data.
 */
@Dao
public interface DataDao {
    /**
     * Used for the LoadRecipeActivity to get all recipes.
     *
     * @return A list of recipes.
     */
    @Query("SELECT * FROM Recipe ORDER BY name")
    List<Recipe> getAllRecipes();

    /**
     * Gets a recipe with ingredients.
     *
     * @param id The id of the recipe with ingredients to get.
     * @return A recipe with ingredients.
     */
    @Transaction
    @Query("SELECT * FROM Recipe WHERE id = :id")
    RecipeWithIngredients getRecipeWithIngredientsById(int id);

    /**
     *  Gets all the ingredient names to be used for the ingredient name autocomplete.
     *
     * @return A list of ingredient names.
     */
    @Query("SELECT DISTINCT name FROM Ingredient ORDER BY name")
    List<String> getIngredientNames();

    /**
     * Inserts a recipe into the database.
     *
     * @param recipe The recipe to save.
     * @return The id for the recipe that was saved.
     */
    @Insert(entity = Recipe.class)
    long insertRecipe(Recipe recipe);

    /**
     * Inserts an ingredient into the database.
     *
     * @param ingredient The ingredient to save.
     * @return The id for the ingredient that was saved.
     */
    @Insert(entity = Ingredient.class)
    long insertIngredient(Ingredient ingredient);

    /**
     * Updates an existing recipe.
     *
     * @param id The recipe id.
     * @param name The recipe name.
     * @param servingSize The recipe serving size.
     * @param cookTimeMinutes The recipe cook time (in minutes).
     * @param temperature The temperature to cook the recipe at.
     * @param temperatureMeasurement The measurement used for the temperature (F or C).
     * @param conversionTemperatureMeasurement the converted measurement used for the
     *                                         temperature (F or C).
     * @param conversionType How the recipe should be converted.
     * @param conversionAmount How much the recipe should be converted.
     * @param notes Any notes for the recipe.
     * @param fromSystem The system used for the ingredient measurements.
     * @param toSystem The system used to convert the ingredients to.
     * @return The id of the recipe that was updated.
     */
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

    /**
     * Deletes a recipe.
     *
     * @param recipeId The recipe id to delete.
     */
    @Query("DELETE FROM Recipe WHERE id = :recipeId")
    void deleteRecipeById(int recipeId);

    /**
     * Deletes all ingredients for a recipe.
     *
     * @param recipeId The recipe id for the ingredients to delete.
     */
    @Query("DELETE FROM Ingredient WHERE recipeId = :recipeId")
    void deleteIngredientsByRecipeId(int recipeId);
}