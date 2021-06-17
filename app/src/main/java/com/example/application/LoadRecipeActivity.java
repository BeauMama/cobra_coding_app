package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    public DataDao dataDao;
    List<RecipeWithIngredients> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        DataAppDatabase db = Room.databaseBuilder(getApplicationContext(),
                DataAppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dataDao = db.dataDao();
    }

    public void addDataTest(View view) {
        DataSaveRecipe recipeData = new DataSaveRecipe(this.dataDao, this.recipes);
        Thread thread = new Thread(recipeData, "Save recipe data");
        thread.start();

    }

    public void printDataTest(View view) {
        DataGetRecipe recipeData = new DataGetRecipe(this.dataDao, this.recipes);
        Thread thread = new Thread(recipeData, "Get recipe data");
        thread.start();
    }
}