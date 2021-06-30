package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.application.database.DataDao;
import com.example.application.database.DataGetAllRecipes;
import com.example.application.database.DataInitializeDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadRecipeActivity extends AppCompatActivity implements ViewRecipeListAdapter.SelectItemListener {

    private DataDao dataDao;
    private List<Recipe> recipes;
    private RecyclerView recyclerView;
    private ViewRecipeListAdapter viewRecipeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        initializeDatabase();
        loadAllRecipes();
        initializeRecycleView();
    }

    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

    public void loadAllRecipes() {
        DataGetAllRecipes dataGetAllRecipes = new DataGetAllRecipes(dataDao);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            recipes = executorService.submit(dataGetAllRecipes).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeRecycleView() {
        recyclerView = findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewRecipeListAdapter = new ViewRecipeListAdapter(recipes, this);
        recyclerView.setAdapter(viewRecipeListAdapter);
    }

    @Override
    public void selectItemClick(int position) {
        int recipeIdSelected = recipes.get(position).getId();
        recipeIdSelected = 138;
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("id", recipeIdSelected);
        startActivity(intent);
    }

}