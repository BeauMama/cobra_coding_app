package com.example.application;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DataRecipe.class, DataIngredient.class/*, DataIngredientName.class */}, version = 7)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataRecipeDao dataRecipeDao();
    //public abstract DataRecipeWithIngredientsAndNamesDao dataRecipeWithIngredientsAndNamesDao();
}