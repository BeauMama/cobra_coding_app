package com.example.application.database;

import android.util.Log;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataSaveRecipeWithIngredients implements Callable<RecipeWithIngredients> {

    private DataDao dataDao;
    private Recipe recipe;
    private List<Ingredient> ingredients;

    public DataSaveRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipe = recipeWithIngredients.recipe;
        this.ingredients = recipeWithIngredients.ingredients;
    }

    public RecipeWithIngredients call() throws InvalidParameterException {
        long recipeId = recipe.getId();

        if (recipeId == 0) {
            // New recipe
            recipeId = dataDao.insertRecipe(recipe);

            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeId((int) recipeId);
                dataDao.insertIngredient(ingredient);
            }
        } else {
            // Existing recipe
            dataDao.updateRecipe((int) recipeId, recipe.getName(), recipe.getServingSize(),
                    recipe.getCookTimeMinutes(), recipe.getTemperature(), recipe.getTemperatureMeasurement(),
                    recipe.getConversionTemperatureMeasurement(), recipe.getConversionType(),
                    recipe.getConversionAmount(), recipe.getNotes(), recipe.getFromSystem(),
                    recipe.getToSystem());

            dataDao.deleteIngredientsByRecipeId((int) recipeId);
            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeId((int) recipeId);
                ingredient.setId(0);
                dataDao.insertIngredient(ingredient);
            }
        }

        return dataDao.getRecipeWithIngredientsById((int) recipeId);
    }
}