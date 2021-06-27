package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.application.database.DataDao;
import com.example.application.database.DataGetAllRecipes;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.database.DataSaveRecipe;

import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    private DataDao dataDao;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());

        RecyclerView recyclerView = findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getAllRecipes(null);

    }

    public void getAllRecipes(View view) {
        DataGetAllRecipes recipes = new DataGetAllRecipes(this, this.dataDao, this.recipes);
        Thread thread = new Thread(recipes, "Get all recipes");
        thread.start();
    }

    public void addDataTest(View view) {
        DataSaveRecipe recipeData = new DataSaveRecipe(this.dataDao);
        Thread thread = new Thread(recipeData, "Save recipe data");
        thread.start();
    }


}