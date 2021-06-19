package com.example.application;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, Ingredient.class, IngredientName.class}, version = 12)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}