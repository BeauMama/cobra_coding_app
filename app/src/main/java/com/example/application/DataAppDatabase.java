package com.example.application;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {DataRecipe.class}, version = 5)
public abstract class DataAppDatabase extends RoomDatabase {
    public abstract DataRecipeDao dataRecipeDao();
}