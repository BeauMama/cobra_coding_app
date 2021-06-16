package com.example.application;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.List;

public class DataSaveRecipe implements Runnable {

    private DataRecipeDao dataRecipeDao;
    private List<DataRecipeWithIngredients> recipes;
    //private WeakReference<Activity> activity;
    public DataSaveRecipe(DataRecipeDao dataRecipeDao, List<DataRecipeWithIngredients> recipes) {
        this.dataRecipeDao = dataRecipeDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        DataRecipe recipe = new DataRecipe();
        recipe.name = "A recipe with null";
        //recipe.servingSize = (float) 2.4;
        //recipe.conversionAmount = 3;
        System.out.println("The name is: " + recipe.name);
        System.out.println("The serving size is: " + recipe.servingSize);

        dataRecipeDao.insertRecipe(recipe);
        Log.d("SaveRecipeData.run()", "Recipe saved");

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
