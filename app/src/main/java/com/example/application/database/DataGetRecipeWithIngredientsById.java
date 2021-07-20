package com.example.application.database;

import com.example.application.model.Ingredient;
import com.example.application.model.RecipeWithIngredients;
import java.security.InvalidParameterException;
import java.util.concurrent.Callable;

/**
 * Gets a recipe and its related ingredients by the recipe id.
 */
public class DataGetRecipeWithIngredientsById implements Callable<RecipeWithIngredients> {

    private final DataDao dataDao;
    private final int id;

    /**
     * Constructor
     * @param dataDao The data access object.
     * @param id The id of the recipe to get.
     */
    public DataGetRecipeWithIngredientsById(DataDao dataDao, int id) {
        this.dataDao = dataDao;
        this.id = id;
    }

    /**
     * Get a recipe and its related ingredients by id.
     * @return A recipe with its ingredients.
     * @throws InvalidParameterException Throws exception if invalid parameter is used.
     */
    public RecipeWithIngredients call() throws InvalidParameterException {
        RecipeWithIngredients recipeWithIngredients = dataDao.getRecipeWithIngredientsById(id);

        // This is needed so the ingredient information can be accessed by the recipe.
        recipeWithIngredients.recipe.setRecipeWithIngredients(recipeWithIngredients);

        for (Ingredient ingredient : recipeWithIngredients.ingredients) {
            // This is needed so the recipe information can be accessed by the ingredient.
            ingredient.setRecipeWithIngredients(recipeWithIngredients);
        }
        return recipeWithIngredients;
    }
}