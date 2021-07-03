package com.example.application.database;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

public class DataSaveRecipeWithIngredients implements Callable<RecipeWithIngredients> {

    private DataDao dataDao;
    private Recipe recipe;
    private List<Ingredient> ingredients;

    public DataSaveRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipe = recipeWithIngredients.recipe;
        this.ingredients = recipeWithIngredients.ingredients;
    }

    public RecipeWithIngredients call() throws InvalidParameterException {
        long recipeId = recipe.getId();

        if (recipeId == 0) {
            recipeId = dataDao.insertRecipe(recipe);

            for (Ingredient ingredient : ingredients) {
                ingredient.setRecipeId((int) recipeId);
                dataDao.insertIngredient(ingredient);
            }
        } else {
            dataDao.updateRecipe(recipe.getId(), recipe.getName(), recipe.getServingSize(),
                    recipe.getCookTimeMinutes(), recipe.getTemperature(), recipe.getTemperatureMeasurement(),
                    recipe.getConversionTemperatureMeasurement(), recipe.getConversionType(),
                    recipe.getConversionAmount(), recipe.getNotes(), recipe.getFromSystem(),
                    recipe.getToSystem());

            for (Ingredient ingredient : ingredients) {
                dataDao.updateIngredient(ingredient.getId(), (int) recipeId, ingredient.getName(),
                    ingredient.getQuantity(), ingredient.getMeasurement(), ingredient.getConversionMeasurement(),
                    ingredient.getIsConversionIngredient(), ingredient.getConversionIngredientQuantity());
            }
        }

        return dataDao.getRecipeWithIngredientsById((int) recipeId);
    }
}