package com.example.application.database;

import com.example.application.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

public class DataSaveRecipeWithIngredients implements Callable<Long> {

    private DataDao dataDao;
    private RecipeWithIngredients recipeWithIngredients;

    public DataSaveRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipeWithIngredients = recipeWithIngredients;
    }

    public Long call() throws InvalidParameterException {
        return dataDao.insertRecipeWithIngredients(recipeWithIngredients);
    }
}