package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    public DataDao dataDao;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        DataAppDatabase db = Room.databaseBuilder(getApplicationContext(),
                DataAppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dataDao = db.dataDao();

        List<Recipe> recipes = new ArrayList<>();
/*
        Recipe recipe = new Recipe();
        recipe.name = "My recipe 1";
        //recipe.conversionType = "Multiply";
        recipes.add(recipe);
        recipe = new Recipe();
        recipe.name = "My recipe 2";
        //recipe.conversionType = "Multiply";
        recipes.add(recipe);

 */

        RecyclerView recyclerView = findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ViewRecipeListAdapter(recipes));

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