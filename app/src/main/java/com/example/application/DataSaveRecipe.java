package com.example.application;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

public class DataSaveRecipe implements Runnable {

    private DataDao dataDao;
    private List<RecipeWithIngredients> recipes;

    public DataSaveRecipe(DataDao dataDao, List<RecipeWithIngredients> recipes) {
        this.dataDao = dataDao;
        this.recipes = recipes;
    }

    @Override
    public void run() {
        Recipe recipe = new Recipe();
        recipe.name = "My first recipe";
        recipe.conversionType = "Multiply By";
        int recipeId = (int) dataDao.insertRecipe(recipe);
        Log.d("SaveRecipeData.run()", "Recipe saved as id " + recipeId);

        Ingredient ingredient = new Ingredient();
        ingredient.recipeId = recipeId;
        ingredient.name = "water";
        ingredient.measurement = "milliliters";
        ingredient.conversionMeasurement = "cup";
        ingredient.quantity = (float) 2.5;
        ingredient.conversionIngredient = false;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 1 saved");

        ingredient.name = "flour";
        ingredient.measurement = "grams";
        ingredient.conversionMeasurement = "ounces";
        ingredient.quantity = (float) 87;
        ingredient.conversionIngredient = false;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 2 saved");

        ingredient.name = "oil";
        ingredient.measurement = "liters";
        ingredient.conversionMeasurement = "milliliters";
        ingredient.quantity = (float) 0.5;
        ingredient.conversionIngredient = true;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 3 saved");
    }
}
