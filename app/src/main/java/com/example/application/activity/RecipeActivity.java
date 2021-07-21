package com.example.application.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.application.model.MeasurementDetails;
import com.example.application.R;
import com.example.application.adapter.ViewIngredientsAdapter;
import com.example.application.database.DataDao;
import com.example.application.database.DataDeleteRecipe;
import com.example.application.database.DataGetIngredientNames;
import com.example.application.database.DataGetRecipeWithIngredientsById;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.database.DataSaveRecipeWithIngredients;
import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;
import com.example.application.viewmodel.RecipeViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An edit activity so the user can create and change a recipe and the ingredients for the recipe.
 */
public class RecipeActivity extends AppCompatActivity implements ViewIngredientsAdapter.OnClickListener, SpinnerItemSelected {

    private DataDao dataDao;
    private RecipeViewModel viewModel;
    private Boolean initializeFromSystem = true;
    private Boolean initializeToSystem = true;

    /**
     * Setup when the activity is created.
     *
     * @param savedInstanceState Used to save the state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initializeDatabase();

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
        }

        if (!loadRecipeWithIngredients()) {
            setupRecipeWithDefaultData();
        }

        // Used for ingredient name auto complete.
        getIngredientNames();

        viewModel.setBinding(DataBindingUtil.setContentView(this, R.layout.activity_recipe));
        initializeRecycleView();
        viewModel.getBinding().setViewModel(viewModel);
        viewModel.getBinding().setSpinnerItemSelected(this);
    }

    /**
     * Initialize the database as a singleton.
     */
    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

    /**
     * Initialize the RecyclerView.
     */
    private void initializeRecycleView() {
        viewModel.setRecyclerView(findViewById(R.id.ingredientList));
        viewModel.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        viewModel.setViewIngredientsAdapter(viewModel.getAdapter());
        viewModel.getRecyclerView().setAdapter(viewModel.getViewIngredientsAdapter());
    }

    /**
     * Loads a recipe that the user selected.
     *
     * @return True if a recipe was found and loaded.
     *         False if a recipe was not found.
     */
    private boolean loadRecipeWithIngredients() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        if (id != 0) {
            DataGetRecipeWithIngredientsById dataGetRecipeWithIngredientsById = new DataGetRecipeWithIngredientsById(dataDao, id);
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            try {
                viewModel.setRecipeWithIngredients(executorService.submit(dataGetRecipeWithIngredientsById).get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (viewModel.getRecipeWithIngredients() == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get all the ingredients in the database to be used for the ingredient name
     * auto complete.
     */
    private void getIngredientNames() {
        DataGetIngredientNames dataGetIngredientNames = new DataGetIngredientNames(dataDao);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            viewModel.setIngredientNames(executorService.submit(dataGetIngredientNames).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Setup recipe with default values.
     */
    private void setupRecipeWithDefaultData() {
        // Recipe portion
        if (viewModel.getRecipeWithIngredients() == null) {
            viewModel.setRecipeWithIngredients(new RecipeWithIngredients());
        }
        if (viewModel.getRecipeWithIngredients().recipe == null) {
            viewModel.getRecipeWithIngredients().recipe = new Recipe();
        }

        Recipe recipe = viewModel.getRecipeWithIngredients().recipe;
        recipe.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        recipe.setId(0);
        recipe.setName("");
        recipe.setServingSize(0);
        recipe.setCookTimeMinutes(0);
        recipe.setTemperature(0);
        recipe.setTemperatureMeasurement("F");
        recipe.setConversionTemperatureMeasurement("F");
        recipe.setConversionType("Multiply by");
        recipe.setConversionAmount(0);
        recipe.setNotes("");
        recipe.setFromSystem("All");
        recipe.setToSystem("All");

        // These views are not data bound so they need to be manually set.
        RecipeViewModel.setSpinnerToValue(findViewById(R.id.fromMeasSystem), "All");
        RecipeViewModel.setSpinnerToValue(findViewById(R.id.toMeasSystem), "All");

        // Ingredient portion
        if (viewModel.getRecipeWithIngredients().ingredients != null) {
            viewModel.getRecipeWithIngredients().ingredients.clear();
        }

        if (viewModel.getRecyclerView() != null) {
            viewModel.getRecyclerView().removeAllViews();
        }

        viewModel.getRecipeWithIngredients().ingredients = new ArrayList<>();
        Ingredient ingredient;

        for(int i = 0; i < 3; i++) {
            ingredient = new Ingredient();
            ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
            ingredient.setId(0);
            ingredient.setRecipeId(0);
            ingredient.setName("");
            ingredient.setQuantity(0);
            ingredient.setMeasurement("select");
            ingredient.setConversionMeasurement("select");
            ingredient.setIsConversionIngredient(false);
            ingredient.setConversionIngredientQuantity(0);
            viewModel.getRecipeWithIngredients().ingredients.add(ingredient);
        }
    }

    /**
     * Add new ingredient in the recyclerview.
     *
     * @param view The view that was clicked.
     */
    public void addIngredient(View view) {
        //Get auto complete to work when you add new ingredient.
        AutoCompleteTextView textView = findViewById(R.id.ingredientName);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
        textView.setThreshold(1);
        textView.setAdapter(adapter);

        // Set up new ingredient with default values.
        Ingredient ingredient = new Ingredient();
        ingredient.setName("");
        ingredient.setMeasurement("select");
        ingredient.setConversionMeasurement("select");
        ingredient.setIsConversionIngredient(false);
        // This is needed so the ingredient can reference the recipe.
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());

        viewModel.getRecipeWithIngredients().ingredients.add(ingredient);

        // Update the adapter and binding with the new ingredient.
        viewModel.getAdapter().notifyItemInserted(viewModel.getRecipeWithIngredients().ingredients.size() - 1);
        viewModel.getAdapter().notifyDataSetChanged();
        viewModel.getBinding().setViewModel(viewModel);
        viewModel.getBinding().setSpinnerItemSelected(this);


        viewModel.getRecyclerView().scrollToPosition(viewModel.getRecipeWithIngredients().ingredients.size() - 1);
    }

    /**
     * Delete an ingredient from the RecyclerView.
     *
     * @param position The position of the ingredient to delete.
     */
    @Override
    public void deleteIngredient(int position) {
        if (viewModel.getRecipeWithIngredients().ingredients.size() > 1) {
            viewModel.getRecipeWithIngredients().ingredients.remove(position);
            viewModel.getAdapter().notifyItemRemoved(position);
            viewModel.getAdapter().notifyDataSetChanged();
        }
    }

    /**
     * Deletes the ingredient from the database and clears the recipe to the default.
     *
     * @param view
     */
    public void deleteRecipe(View view) {
        // Setup double confirmation when deleting a recipe.
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                // Set message
                .setTitle("Are you sure you want to delete the recipe.")
                // Set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Recipe will be deleted.
                        DataDeleteRecipe dataDeleteRecipe = new DataDeleteRecipe(dataDao, viewModel.getRecipeWithIngredients().recipe.getId());

                        ExecutorService executorService = Executors.newFixedThreadPool(3);
                        try {
                            executorService.submit(dataDeleteRecipe).get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        setupRecipeWithDefaultData();
                    }

                })
                // Set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Message will show that recipe was not deleted.
                        Toast.makeText(getApplicationContext(), "Recipe was not deleted.", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    /**
     * Checks if the user filled out the recipe correct.
     *
     * @return True if the recipe has been filled out correctly.
     *         False if the recipe has not been filled out correctly.
     */
    private boolean isValidRecipe() {
        boolean recipeValid = true;
        String message = "";

        if (viewModel.getRecipeWithIngredients().recipe.getName().equals("")) {
            message = "Please enter a recipe name";
            recipeValid = false;

        } else {
            for (int i = 0; i < viewModel.getRecipeWithIngredients().ingredients.size(); i++ ) {
                Ingredient ingredient = viewModel.getRecipeWithIngredients().ingredients.get(i);

                if (ingredient.getName().equals("")) {
                    message = "Ingredient " + (i + 1) + " is missing a name";
                    recipeValid = false;
                    break;
                }
                if (ingredient.getMeasurement().toLowerCase().equals("select")) {
                    message = "Ingredient " + (i + 1) + " needs a measurement";
                    recipeValid = false;
                    break;
                }
                if (ingredient.getConversionMeasurement().toLowerCase().equals("select")) {
                    message = "Ingredient " + (i + 1) + " needs a conversion measurement";
                    recipeValid = false;
                    break;
                }
            }
        }

        if (!recipeValid) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }

        return recipeValid;
    }

    public void saveRecipe(View view) {
        if (isValidRecipe()) {
            // The recipe is valid. Save it to the database.
            DataSaveRecipeWithIngredients dataSaveRecipeWithIngredients = new DataSaveRecipeWithIngredients(dataDao, viewModel.getRecipeWithIngredients());

            ExecutorService executorService = Executors.newFixedThreadPool(3);
            try {
                viewModel.setRecipeWithIngredients(executorService.submit(dataSaveRecipeWithIngredients).get());
                Toast.makeText(this,"Recipe saved", Toast.LENGTH_SHORT).show();

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void fromSystemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Set up spinner with correct value from model when initializing it
        if (initializeFromSystem) {
            initializeFromSystem = false;
            RecipeViewModel.setSpinnerToValue((Spinner) parent, viewModel.getRecipeWithIngredients().recipe.getFromSystem());
        }

        String systemSelected = parent.getSelectedItem().toString();
        viewModel.getRecipeWithIngredients().recipe.setFromSystem(systemSelected);

        List<String> measurements = MeasurementDetails.getMeasurements(systemSelected, "all");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, measurements);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        for(int i = 0; i < viewModel.getAdapter().getItemCount(); i++) {
            View ingredient = viewModel.getRecyclerView().getLayoutManager().findViewByPosition(i);
            if (ingredient != null) {
                Spinner spinnerMeasurement = ingredient.findViewById(R.id.measurement);
                String oldMeasurementValue = spinnerMeasurement.getSelectedItem().toString();
                spinnerMeasurement.setAdapter(adapter);

                // After changing the spinner list, set it back to what it was selected to before if the item
                // is still in the list
                RecipeViewModel.setSpinnerToValue(spinnerMeasurement, oldMeasurementValue);
            }
        }
    }

    @Override
    public void toSystemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Set up spinner with correct value from model when initializing it
        if (initializeToSystem) {
            initializeToSystem = false;
            RecipeViewModel.setSpinnerToValue((Spinner) parent, viewModel.getRecipeWithIngredients().recipe.getToSystem());

            for (int itemPosition = 0; itemPosition < parent.getAdapter().getCount(); itemPosition++) {
                String itemValue = (String) parent.getAdapter().getItem(itemPosition);
                if (itemValue.equals(viewModel.getRecipeWithIngredients().recipe.getToSystem())) {
                    parent.setSelection(itemPosition);
                    break;
                }
            }
        }
        String systemSelected = parent.getSelectedItem().toString();
        viewModel.getRecipeWithIngredients().recipe.setToSystem(systemSelected);

        for(int i = 0; i < viewModel.getAdapter().getItemCount(); i++) {
            View ingredient = viewModel.getRecyclerView().getLayoutManager().findViewByPosition(i);

            if (ingredient != null) {
                Spinner spinnerMeasurement = ingredient.findViewById(R.id.measurement);
                String measurementTypeSelected = MeasurementDetails.getMeasurementType(spinnerMeasurement.getSelectedItem().toString());

                Spinner spinnerConvMeasurement = ingredient.findViewById(R.id.convMeasurement);
                String oldMeasurementValue = spinnerConvMeasurement.getSelectedItem().toString();

                List<String> measurements = MeasurementDetails.getMeasurements(systemSelected, measurementTypeSelected);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_checked, measurements);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

                spinnerConvMeasurement.setAdapter(adapter);

                // After changing the spinner list, set it back to what it was selected to before if the item
                // is still in the list
                RecipeViewModel.setSpinnerToValue(spinnerConvMeasurement, oldMeasurementValue);
            }
        }
    }





}