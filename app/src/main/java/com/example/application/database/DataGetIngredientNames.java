package com.example.application.database;

import com.example.application.database.DataDao;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataGetIngredientNames implements Callable<List<String>> {

    private DataDao dataDao;

    public DataGetIngredientNames(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<String> call() throws InvalidParameterException {
        return dataDao.getIngredientNames();
    }
}