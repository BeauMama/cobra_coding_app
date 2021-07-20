package com.example.application.database;

import com.example.application.model.Recipe;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Gets all recipes in the database.
 */
public class DataGetAllRecipes implements Callable<List<Recipe>> {

    private final DataDao dataDao;

    /**
     * Constructor
     *
     * @param dataDao The data access object.
     */
    public DataGetAllRecipes(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    /**
     * Get all recipes in the database.
     *
     * @return A list of recipes
     * @throws InvalidParameterException Throws exception if invalid parameter is used.
     */
    public List<Recipe> call() throws InvalidParameterException {
        return dataDao.getAllRecipes();
    }
}