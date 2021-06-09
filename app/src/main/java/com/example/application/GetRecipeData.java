package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class GetRecipeData implements Runnable {

    private DataRecipeDao dataRecipeDao;
    private List<Recipe> recipes;
    //private WeakReference<Activity> activity;
    public GetRecipeData(DataRecipeDao dataRecipeDao, List<Recipe> recipes) {
        this.dataRecipeDao = dataRecipeDao;
        this.recipes = recipes;
        //this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        recipes = dataRecipeDao.getAll();
        Log.d("printDataTest()", "Records: " + recipes.size());

        //Activity loadRecipeActivity = this.activity.get();
        //if (loadRecipeActivity != null) {

        //}



    }
}
