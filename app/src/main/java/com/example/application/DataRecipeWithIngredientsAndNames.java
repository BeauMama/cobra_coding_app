package com.example.application;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DataRecipeWithIngredientsAndNames {
    @Embedded public Recipe recipe;
    @Relation(
            entity = DataRecipe.class,
            parentColumn = "recipeId",
            entityColumn = "recipeId"
    )
    public List<DataRecipeWithIngredients> ingredients;
}
