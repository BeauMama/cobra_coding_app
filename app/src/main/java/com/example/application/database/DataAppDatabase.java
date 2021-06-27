package com.example.application.database;
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.application.Ingredient;
import com.example.application.Recipe;

@Database(entities = {Recipe.class, Ingredient.class}, version = 17)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}