package com.example.application.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;

@Database(entities = {Recipe.class, Ingredient.class}, version = 21)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}