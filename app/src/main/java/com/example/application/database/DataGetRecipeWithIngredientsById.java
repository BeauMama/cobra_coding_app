package com.example.application.database;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

public class DataGetRecipeWithIngredientsById implements Callable<RecipeWithIngredients> {

    private DataDao dataDao;
    private int id;

    public DataGetRecipeWithIngredientsById(DataDao dataDao, int id) {
        this.dataDao = dataDao;
        this.id = id;
    }

    public RecipeWithIngredients call() throws InvalidParameterException {
        RecipeWithIngredients recipeWithIngredients = dataDao.getRecipeWithIngredientsById(id);

        // This is needed so the ingredient information can be accessed by the recipe
        recipeWithIngredients.recipe.setRecipeWithIngredients(recipeWithIngredients);

        for (Ingredient ingredient : recipeWithIngredients.ingredients) {
            // This is needed so the recipe information can be accessed by the ingredient
            // so the ingredient.getQuantityIncreaseDecreaseString method can calculate what
            // it needs to
            ingredient.setRecipeWithIngredients(recipeWithIngredients);
        }

        return recipeWithIngredients;
    }
}