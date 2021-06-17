package com.example.application;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

public class DataSaveRecipe implements Runnable {

    private DataDao dataDao;
    private List<RecipeWithIngredients> recipes;
    //private WeakReference<Activity> activity;
    public DataSaveRecipe(DataDao dataDao, List<RecipeWithIngredients> recipes) {
        this.dataDao = dataDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {
        Recipe recipe = new Recipe();

        recipe.name = "My first recipe";
        int recipeId = (int) dataDao.insertRecipe(recipe);
        Log.d("SaveRecipeData.run()", "Recipe saved as id " + recipeId);

        Ingredient ingredient = new Ingredient();
        ingredient.recipeId = recipeId;
        ingredient.name = "water";
        ingredient.measurement = "milliliters";
        ingredient.conversionMeasurement = "cup";
        ingredient.quantity = (float) 2.5;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 1 saved");

        ingredient.name = "flour";
        ingredient.measurement = "grams";
        ingredient.conversionMeasurement = "ounces";
        ingredient.quantity = (float) 87;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 2 saved");

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
