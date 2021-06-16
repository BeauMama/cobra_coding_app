package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    public DataRecipeDao dataRecipeDao;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        DataAppDatabase db = Room.databaseBuilder(getApplicationContext(),
                DataAppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dataRecipeDao = db.dataRecipeDao();
    }

    public void addDataTest(View view) {
        DataSaveRecipe recipeData = new DataSaveRecipe(this.dataRecipeDao, this.recipes);
        Thread thread = new Thread(recipeData, "Save recipe data");
        thread.start();

    }

    public void printDataTest(View view) {

        DataGetRecipe recipeData = new DataGetRecipe(this.dataRecipeDao, this.recipes);
        Thread thread = new Thread(recipeData, "Get recipe data");
        thread.start();


    }


}