package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetRecipe implements Runnable {

    private DataDao dataDao;
    private List<RecipeWithIngredients> recipes;
    //private List<Recipe> recipes;
    //private WeakReference<Activity> activity;
    public DataGetRecipe(DataDao dataDao, List<RecipeWithIngredients> recipes) {
        this.dataDao = dataDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {
        recipes = dataDao.getRecipeWithIngredients();
        Log.d("GetRecipeData.run()", "Records: " + recipes.size());

        for(RecipeWithIngredients recipe : recipes) {
            System.out.println("--- Begin recipe ---");
            System.out.println("id: " + recipe.recipe.id);
            System.out.println("name: " + recipe.recipe.name);
            System.out.println("notes: " + recipe.recipe.notes);
            System.out.println("temperature: " + recipe.recipe.temperature);
            System.out.println("servingSize: " + recipe.recipe.servingSize);
            System.out.println("conversionAmount: " + recipe.recipe.conversionAmount);

            System.out.println("--- Ingredients ---");
            for(Ingredient ingredient : recipe.ingredients) {
                System.out.println("id: " + ingredient.id);
                System.out.println("recipe id: " + ingredient.recipeId);
                System.out.println("name: " + ingredient.name);
                System.out.println("quantity: " + ingredient.quantity);
                System.out.println("measurement: " + ingredient.measurement);
                System.out.println("conv meas: " + ingredient.conversionMeasurement);
                System.out.println("----------");
            }

            System.out.println("--- End of recipe ---");

        }

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
