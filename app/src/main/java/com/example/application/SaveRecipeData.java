package com.example.application;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

public class SaveRecipeData implements Runnable {

    private DataRecipeDao dataRecipeDao;
    private List<Recipe> recipes;
    //private WeakReference<Activity> activity;
    public SaveRecipeData(DataRecipeDao dataRecipeDao, List<Recipe> recipes) {
        this.dataRecipeDao = dataRecipeDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        Recipe recipe = new Recipe();
        recipe.name  = "A recipe";
        recipe.servingSize = (float) 2.4;
        recipe.conversionAmount = 3;
        System.out.println("The name is: " + recipe.servingSize);

        dataRecipeDao.insertRecipe(recipe);
        Log.d("SaveRecipeData.run()", "Recipe saved");

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
