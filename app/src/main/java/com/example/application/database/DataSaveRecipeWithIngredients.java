package com.example.application.database;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Saves a recipe and its related ingredients.
 */
public class DataSaveRecipeWithIngredients implements Callable<RecipeWithIngredients> {

    private final DataDao dataDao;
    private final Recipe recipe;
    private final List<Ingredient> ingredients;

    /**
     * Constructor
     * @param dataDao The data access object.
     * @param recipeWithIngredients The recipe and its ingredients to save.
     */
    public DataSaveRecipeWithIngredients(DataDao dataDao, RecipeWithIngredients recipeWithIngredients) {
        this.dataDao = dataDao;
        this.recipe = recipeWithIngredients.recipe;
        this.ingredients = recipeWithIngredients.ingredients;
    }

    /**
     * Save a recipe and its related ingredients.
     * @return The id that the recipe was assigned when saving.
     * @throws InvalidParameterException Throws exception if invalid parameter is used.
     */
    public RecipeWithIngredients call() throws InvalidParameterException {
        long recipeId = recipe.getId();

        if (recipeId == 0) {
            // New recipe
            recipeId = dataDao.insertRecipe(recipe);

            for (Ingredient ingredient : ingredients) {
                // Assign the ingredients to the recipe.
                ingredient.setRecipeId((int) recipeId);

                dataDao.insertIngredient(ingredient);
            }
        } else {
            // Existing recipe to update.
            dataDao.updateRecipe((int) recipeId, recipe.getName(), recipe.getServingSize(),
                    recipe.getCookTimeMinutes(), recipe.getTemperature(), recipe.getTemperatureMeasurement(),
                    recipe.getConversionTemperatureMeasurement(), recipe.getConversionType(),
                    recipe.getConversionAmount(), recipe.getNotes(), recipe.getFromSystem(),
                    recipe.getToSystem());

            // To simplify saving the ingredients, first delete all existing ingredients
            // for the recipe before saving the ingredients.
            dataDao.deleteIngredientsByRecipeId((int) recipeId);
            for (Ingredient ingredient : ingredients) {
                // Assign the ingredients to the recipe.
                ingredient.setRecipeId((int) recipeId);

                ingredient.setId(0);
                dataDao.insertIngredient(ingredient);
            }
        }
        return dataDao.getRecipeWithIngredientsById((int) recipeId);
    }
}