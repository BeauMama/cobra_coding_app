package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetRecipe implements Runnable {

    private DataRecipeDao dataRecipeDao;
    private List<DataRecipeWithIngredients> recipes;
    //private List<Recipe> recipes;
    //private WeakReference<Activity> activity;
    public DataGetRecipe(DataRecipeDao dataRecipeDao, List<DataRecipeWithIngredients> recipes) {
        this.dataRecipeDao = dataRecipeDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        recipes = dataRecipeDao.getRecipeWithIngredients();
        Log.d("GetRecipeData.run()", "Records: " + recipes.size());

        for(DataRecipeWithIngredients recipe : recipes) {
            System.out.println("recipeId: " + recipe.recipe.id);
            System.out.println("name: " + recipe.recipe.name);
            System.out.println("notes: " + recipe.recipe.notes);
            System.out.println("temperature: " + recipe.recipe.temperature);
            System.out.println("servingSize: " + recipe.recipe.servingSize);
            System.out.println("conversionAmount: " + recipe.recipe.conversionAmount);
            System.out.println("----------");
        }

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
