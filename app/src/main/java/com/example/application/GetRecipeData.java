package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;

public class GetRecipeData implements Runnable {

    private WeakReference<Activity> activity;
    public GetRecipeData(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    @Override
    public void run() {
        Activity loadRecipeActivity = this.activity.get();
        if (loadRecipeActivity != null) {
            //loadRecipeActivity.recipes = dataRecipeDao.getAll();
        }

        //Log.d("printDataTest()", "Records: " + loadRecipeActivity.recipes.size());

    }
}
