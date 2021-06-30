package com.example.application.database;

import com.example.application.Ingredient;
import com.example.application.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DataInsertRecipeWithIngredients implements Callable<Long> {

    private DataDao dataDao;
    private RecipeWithIngredients recipeWithIngredients;

    public DataInsertRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipeWithIngredients = recipeWithIngredients;
    }

    public Long call() throws InvalidParameterException {
        System.out.println("id before save: " + recipeWithIngredients.recipe.getId());
        long id = dataDao.insertRecipe(recipeWithIngredients.recipe);
        System.out.println("id after save: " + id);
        for (Ingredient ingredient : recipeWithIngredients.ingredients) {
            ingredient.setRecipeId((int) id);
            dataDao.insertIngredient(ingredient);
        }
        return id;
    }
}