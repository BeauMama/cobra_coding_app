package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetRecipe implements Runnable {

    private DataRecipeDao dataRecipeDao;
    private List<Recipe> recipes;
    //private WeakReference<Activity> activity;
    public DataGetRecipe(DataRecipeDao dataRecipeDao, List<Recipe> recipes) {
        this.dataRecipeDao = dataRecipeDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        recipes = dataRecipeDao.getAll();
        Log.d("GetRecipeData.run()", "Records: " + recipes.size());

        for(Recipe recipe : recipes) {
            System.out.println("recipeId: " + recipe.recipeId);
            System.out.println("name: " + recipe.name);
            System.out.println("notes: " + recipe.notes);
            System.out.println("temperature: " + recipe.temperature);
            System.out.println("servingSize: " + recipe.servingSize);
            System.out.println("conversionAmount: " + recipe.conversionAmount);
            System.out.println("----------");
        }

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
