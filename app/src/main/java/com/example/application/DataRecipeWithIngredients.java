package com.example.application;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DataRecipeWithIngredients {
    @Embedded
    public Recipe recipe;
    @Relation(
            parentColumn = "id",
            entityColumn = "recipeId",
            entity = DataIngredient.class
    )
    public List<DataIngredient> ingredients;


}
