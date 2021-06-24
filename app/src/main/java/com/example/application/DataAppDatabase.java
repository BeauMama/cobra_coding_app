package com.example.application;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Recipe.class, Ingredient.class}, version = 17)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataDao dataDao();
}