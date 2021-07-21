package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import com.example.application.viewmodel.LoadRecipeViewModel;
import com.example.application.R;
import com.example.application.adapter.ViewRecipeListAdapter;
import com.example.application.database.DataDao;
import com.example.application.database.DataGetAllRecipes;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.databinding.ActivityLoadRecipeBinding;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Gives the user the ability to pick a saved recipe from a list.
 */
public class LoadRecipeActivity extends AppCompatActivity implements ViewRecipeListAdapter.SelectItemListener {

    private DataDao dataDao;
    private RecyclerView recyclerView;
    private ViewRecipeListAdapter viewRecipeListAdapter;
    private LoadRecipeViewModel viewModel;

    /**
     * Setup when the activity is created.
     *
     * @param savedInstanceState Used to save the state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        // Set up the view model.
        viewModel = new ViewModelProvider(this).get(LoadRecipeViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
        }

        initializeDatabase();
        loadAllRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload the recipes when coming back to the activity.
        loadAllRecipes();
    }

    /**
     * Initialize the database as a singleton.
     */
    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

    /**
     * Load all recipes from the database into the RecyclerView.
     */
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

        // Bind the loaded recipes to the view model and RecyclerView.
        ActivityLoadRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_load_recipe);
        initializeRecycleView();
        binding.setViewModel(viewModel);
    }

    /**
     * Initialize the RecyclerView.
     */
    private void initializeRecycleView() {
        recyclerView = findViewById(R.id.recipeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewRecipeListAdapter = viewModel.getAdapter();
        recyclerView.setAdapter(viewRecipeListAdapter);
    }

    /**
     * Run the RecipeActivity when a recipe is selected.
     *
     * @param position The position of the item clicked in the RecyclerView.
     */
    @Override
    public void selectItemClick(int position) {
        // Get the recipe id that was selected to load it in the next activity.
        int recipeIdSelected = viewModel.getRecipes().get(position).getId();
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("id", recipeIdSelected);
        startActivity(intent);
    }
}