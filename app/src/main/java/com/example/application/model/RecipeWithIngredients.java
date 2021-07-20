package com.example.application.model;

import androidx.room.Embedded;
import androidx.room.Relation;
import java.util.List;

/**
 * The recipe model combined with a list of ingredients with
 * the relationship defined between the two for Rooms.
 */
public class RecipeWithIngredients {
    @Embedded
    public Recipe recipe;
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId",
        entity = Ingredient.class
    )

    public List<Ingredient> ingredients;
}
