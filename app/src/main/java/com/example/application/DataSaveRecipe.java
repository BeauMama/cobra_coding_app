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

        IngredientName ingredientName = new IngredientName();
        ingredientName.name = "water";
        dataDao.insertIngredientName(ingredientName);
        ingredientName.name = "flour";
        dataDao.insertIngredientName(ingredientName);
        ingredientName.name = "oil";
        dataDao.insertIngredientName(ingredientName);

        Ingredient ingredient = new Ingredient();
        ingredient.recipeId = recipeId;
        ingredient.ingredientNameId = 1;
        ingredient.measurement = "milliliters";
        ingredient.conversionMeasurement = "cup";
        ingredient.quantity = (float) 2.5;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 1 saved");

        ingredient.ingredientNameId = 2;
        ingredient.measurement = "grams";
        ingredient.conversionMeasurement = "ounces";
        ingredient.quantity = (float) 87;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 2 saved");

        ingredient.ingredientNameId = 3;
        ingredient.measurement = "liters";
        ingredient.conversionMeasurement = "milliliters";
        ingredient.quantity = (float) 0.5;
        dataDao.insertIngredient(ingredient);
        Log.d("SaveRecipeData.run()", "Ingredient 3 saved");

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
