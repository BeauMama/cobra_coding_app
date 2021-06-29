package com.example.application.database;

import com.example.application.Recipe;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataGetAllRecipes implements Callable<List<Recipe>> {

    private DataDao dataDao;

    public DataGetAllRecipes(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<Recipe> call() throws InvalidParameterException {
        return dataDao.getAllRecipes();
    }
}