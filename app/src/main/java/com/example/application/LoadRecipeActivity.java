package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    public DataRecipeDao dataRecipeDao;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dataRecipeDao = db.dataRecipeDao();
    }

    public void addDataTest(View view) {
        SaveRecipeData recipeData = new SaveRecipeData(this.dataRecipeDao, this.recipes);
        Thread thread = new Thread(recipeData, "Save recipe data");
        thread.start();

    }

    public void printDataTest(View view) {

        GetRecipeData recipeData = new GetRecipeData(this.dataRecipeDao, this.recipes);
        Thread thread = new Thread(recipeData, "Get recipe data");
        thread.start();


    }


}