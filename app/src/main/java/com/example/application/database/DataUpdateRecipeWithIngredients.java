package com.example.application.database;

import com.example.application.Ingredient;
import com.example.application.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

public class DataUpdateRecipeWithIngredients implements Callable<Long> {

    private DataDao dataDao;
    private RecipeWithIngredients recipeWithIngredients;

    public DataUpdateRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipeWithIngredients = recipeWithIngredients;
    }

    public Long call() throws InvalidParameterException {

        long id = dataDao.insertRecipe(recipeWithIngredients.recipe);

        for (Ingredient ingredient : recipeWithIngredients.ingredients) {
            ingredient.setRecipeId((int) id);
            dataDao.insertIngredient(ingredient);
        }
        return id;
    }
}