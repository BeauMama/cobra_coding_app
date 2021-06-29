package com.example.application.database;

import com.example.application.Recipe;
import com.example.application.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataGetRecipeWithIngredientsById implements Callable<RecipeWithIngredients> {

    private DataDao dataDao;
    private int id;

    public DataGetRecipeWithIngredientsById(DataDao dataDao, int id) {
        this.dataDao = dataDao;
        this.id = id;
    }

    public RecipeWithIngredients call() throws InvalidParameterException {
        return dataDao.getRecipesWithIngredientsById(id);
    }
}