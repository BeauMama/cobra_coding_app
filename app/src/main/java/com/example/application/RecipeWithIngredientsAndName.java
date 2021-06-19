package com.example.application;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class RecipeWithIngredientsAndName {
    @Embedded
    public RecipeWithIngredients recipeWithIngredients;
    @Relation(
            parentColumn = "ingredientNameId",
            entityColumn = "id",
            entity = IngredientName.class
    )
    public IngredientName ingredientName;
}
