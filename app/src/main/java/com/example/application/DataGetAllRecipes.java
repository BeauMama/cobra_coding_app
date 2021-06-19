package com.example.application;

import android.app.Activity;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

public class DataGetAllRecipes implements Runnable {

    private DataDao dataDao;
    private List<RecipeWithIngredients> recipes;

    public DataGetAllRecipes(DataDao dataDao, List<RecipeWithIngredients> recipes) {
        this.dataDao = dataDao;
        this.recipes = recipes;
    }

    @Override
    public void run() {
        recipes = dataDao.getAllRecipes();
        Log.d("DataGetAllRecipes.run()", "Records: " + recipes.size());

        for(RecipeWithIngredients recipeWithIngredients : recipes) {
            Recipe recipe = recipeWithIngredients.recipe;

            System.out.println("--- Begin recipe ---");
            System.out.println("id: " + recipe.id);
            System.out.println("name: " + recipe.name);
            System.out.println("notes: " + recipe.notes);
            System.out.println("temperature: " + recipe.temperature);
            System.out.println("servingSize: " + recipe.servingSize);
            System.out.println("conversionAmount: " + recipe.conversionAmount);
            System.out.println("conversionType: " + recipe.conversionType);

            System.out.println("--- Ingredients ---");
            for(Ingredient ingredient: recipeWithIngredients.ingredients) {
                System.out.println("id: " + ingredient.id);
                System.out.println("recipe id: " + ingredient.recipeId);
                System.out.println("name: " + ingredient.name);
                System.out.println("conversionIngredient: " + ingredient.conversionIngredient);
                System.out.println("quantity: " + ingredient.quantity);
                System.out.println("measurement: " + ingredient.measurement);
                System.out.println("conv meas: " + ingredient.conversionMeasurement);
                System.out.println("----------");
            }

            System.out.println("--- End of recipe ---");
        }
    }
}
