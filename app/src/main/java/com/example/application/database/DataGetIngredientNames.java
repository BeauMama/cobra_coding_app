package com.example.application.database;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Gets the names of all the ingredients in the database.
 */
public class DataGetIngredientNames implements Callable<List<String>> {

    private final DataDao dataDao;

    /**
     * Constructor
     *
     * @param dataDao The data access object.
     */
    public DataGetIngredientNames(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    /**
     * Get names of all the ingredients in the database.
     *
     * @return List of ingredient names.
     * @throws InvalidParameterException Throws exception if invalid parameter is used.
     */
    public List<String> call() throws InvalidParameterException {
        return dataDao.getIngredientNames();
    }
}