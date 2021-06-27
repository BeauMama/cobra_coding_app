package com.example.application.database;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.Recipe;
import com.example.application.ViewRecipeListAdapter;
import com.example.application.database.DataDao;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetAllRecipes implements Runnable {

    private DataDao dataDao;
    private List<Recipe> recipes;
    private WeakReference<Activity> activity;

    public DataGetAllRecipes(Activity activity, DataDao dataDao, List<Recipe> recipes) {
        this.activity = new WeakReference<>(activity);
        this.dataDao = dataDao;
        this.recipes = recipes;
    }

    @Override
    public void run() {
        recipes = dataDao.getAllRecipes();

        if (recipes.size() > 0) {
            Activity mainActivity = this.activity.get();
            if (mainActivity != null) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecyclerView recyclerView = mainActivity.findViewById(R.id.recipeList);
                        ViewRecipeListAdapter viewRecipeListAdapter = new ViewRecipeListAdapter(recipes);
                        recyclerView.setAdapter(viewRecipeListAdapter);
                    }
                });
            }
        }
    }
}
