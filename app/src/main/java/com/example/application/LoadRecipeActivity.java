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
    List<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        DataAppDatabase db = Room.databaseBuilder(getApplicationContext(),
                DataAppDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
        dataDao = db.dataDao();


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

    public void getIngredientNames(View view) {
        DataGetIngredientNames ingredientNames = new DataGetIngredientNames(this, this.dataDao, this.ingredients);
        Thread thread = new Thread(ingredientNames, "Get all ingredient names");
        thread.start();
    }
}