package com.example.application;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetIngredientNames implements Runnable {

    private DataDao dataDao;
    private List<String> ingredientNames;
    private WeakReference<Activity> activity;

    public DataGetIngredientNames(Activity activity, DataDao dataDao, List<String> ingredientNames) {
        this.activity = new WeakReference<>(activity);
        this.dataDao = dataDao;
        this.ingredientNames = ingredientNames;
    }

    @Override
    public void run() {
        ingredientNames = dataDao.getIngredientNames();

        if (ingredientNames.size() > 0) {
            System.out.println("Ingredient List");
            for (String ingredient : ingredientNames) {
                System.out.println(ingredient);
            }

            /*
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

             */
        }
    }
}
