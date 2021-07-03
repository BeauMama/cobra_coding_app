package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.application.database.DataDao;
import com.example.application.database.DataGetAllRecipes;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.databinding.ActivityLoadRecipeBinding;
import com.example.application.databinding.ActivityRecipeBinding;
import com.example.application.databinding.RecipelistRowBinding;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadRecipeActivity extends AppCompatActivity implements ViewRecipeListAdapter.SelectItemListener {

    private DataDao dataDao;
    private RecyclerView recyclerView;
    private ViewRecipeListAdapter viewRecipeListAdapter;
    private LoadRecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        viewModel = new ViewModelProvider(this).get(LoadRecipeViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
        }

        initializeDatabase();
        loadAllRecipes();

        ActivityLoadRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_load_recipe);
        initializeRecycleView();

        binding.setModel(viewModel);

    }

    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

    public void loadAllRecipes() {
        DataGetAllRecipes dataGetAllRecipes = new DataGetAllRecipes(dataDao);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            viewModel.setRecipes(executorService.submit(dataGetAllRecipes).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void initializeRecycleView() {
        recyclerView = findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewRecipeListAdapter = viewModel.getAdapter();
        recyclerView.setAdapter(viewRecipeListAdapter);
    }

    @Override
    public void selectItemClick(int position) {
        int recipeIdSelected = viewModel.getRecipes().get(position).getId();
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("id", recipeIdSelected);
        startActivity(intent);
    }

}