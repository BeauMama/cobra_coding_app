package com.example.application.database;

import com.example.application.model.Recipe;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataGetAllRecipes implements Callable<List<Recipe>> {

    private final DataDao dataDao;

    public DataGetAllRecipes(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<Recipe> call() throws InvalidParameterException {
        return dataDao.getAllRecipes();
    }
}