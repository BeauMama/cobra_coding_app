package com.example.application;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class IngredientWithName {
    @Embedded
    public Ingredient ingredient;
    @Relation(
            parentColumn = "ingredientNameId",
            entityColumn = "id",
            entity = IngredientName.class
    )

    public IngredientName ingredientName;
}
