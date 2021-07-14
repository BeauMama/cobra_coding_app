package com.example.application.database;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class DataDeleteRecipe implements Callable<Boolean> {

    private DataDao dataDao;
    private int recipeId;

    public DataDeleteRecipe(DataDao dataDao, int recipeId) {
        this.dataDao = dataDao;
        this.recipeId = recipeId;
    }

    public Boolean call() throws InvalidParameterException {
        if (recipeId != 0) {
            dataDao.deleteRecipeById(recipeId);
            dataDao.deleteIngredientByRecipeId(recipeId);
            return true;
        } else {
            return false;
        }
    }
}