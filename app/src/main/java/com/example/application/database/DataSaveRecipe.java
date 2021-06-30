package com.example.application.database;

import android.util.Log;

import com.example.application.Ingredient;
import com.example.application.Recipe;
import com.example.application.RecipeWithIngredients;
import com.example.application.database.DataDao;

import java.util.ArrayList;
import java.util.List;
// This class is only here for reference and will be deleted.
public class DataSaveRecipe implements Runnable {

    private DataDao dataDao;
    private List<RecipeWithIngredients> recipes = new ArrayList<>();

    public DataSaveRecipe(DataDao dataDao) {
        this.dataDao = dataDao;
        //this.recipes = recipes;
    }

    @Override
    public void run() {
        Recipe recipe = new Recipe();
        /*
        recipe.setName("My first recipe 2");
        recipe.setConversionType("Multiply By");
        int recipeId = (int) dataDao.insertRecipe(recipe);
        Log.d("SaveRecipeData.run()", "Recipe saved as id " + recipeId);

        Ingredient ingredient = new Ingredient();
        ingredient.setRecipeId(recipeId);
        ingredient.setName("water");
        ingredient.setMeasurement("milliliters");
        ingredient.setConversionMeasurement("cup");
        ingredient.setQuantity((float) 2.5);
        ingredient.setIsConversionIngredient(false);
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 1 saved");

        ingredient.setName("flour");
        ingredient.setMeasurement("grams");
        ingredient.setConversionMeasurement("ounces");
        ingredient.setQuantity((float) 87);
        ingredient.setIsConversionIngredient(false);
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 2 saved");

        ingredient.setName("oil");
        ingredient.setMeasurement("liters");
        ingredient.setConversionMeasurement("milliliters");
        ingredient.setQuantity((float) 0.5);
        ingredient.setIsConversionIngredient(true);
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 3 saved");

         */
    }
}
