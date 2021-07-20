package com.example.application.database;

import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

public class DataDeleteRecipe implements Callable<Boolean> {

    private final DataDao dataDao;
    private final int recipeId;

    public DataDeleteRecipe(DataDao dataDao, int recipeId) {
        this.dataDao = dataDao;
        this.recipeId = recipeId;
    }

    public Boolean call() throws InvalidParameterException {
        if (recipeId != 0) {
            dataDao.deleteRecipeById(recipeId);
            dataDao.deleteIngredientsByRecipeId(recipeId);
            return true;
        } else {
            return false;
        }
    }
}