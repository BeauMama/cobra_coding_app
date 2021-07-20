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
import com.example.application.MeasurementDetails;
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

public class RecipeActivity extends AppCompatActivity implements ViewIngredientsAdapter.OnClickListener, SpinnerItemSelected {

    private DataDao dataDao;
    private RecipeViewModel viewModel;

    private Boolean initializeFromSystem = true;
    private Boolean initializeToSystem = true;

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

        getIngredientNames();

        viewModel.setBinding(DataBindingUtil.setContentView(this, R.layout.activity_recipe));
        initializeRecycleView();

        viewModel.getBinding().setViewModel(viewModel);
        viewModel.getBinding().setSpinnerItemSelected(this);
    }

    private void initializeRecycleView() {
        viewModel.setRecyclerView(findViewById(R.id.ingredientList));
        viewModel.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        viewModel.setViewIngredientsAdapter(viewModel.getAdapter());
        viewModel.getRecyclerView().setAdapter(viewModel.getViewIngredientsAdapter());
    }

    public void addIngredient(View view) {
        /*
        Add new ingredient in the recyclerview.
         */

        //Get auto complete to work when you add new ingredient.
        AutoCompleteTextView textView = findViewById(R.id.ingredientName);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
        textView.setThreshold(1);
        textView.setAdapter(adapter);

        Ingredient ingredient = new Ingredient(); //creates new ingredient
        ingredient.setName(""); //Setup with blank name
        ingredient.setMeasurement("select"); //Setup to measurement system
        ingredient.setConversionMeasurement("select"); //Setup from measurement system
        ingredient.setIsConversionIngredient(false);
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients()); //ingredient news to reference the recipe
        viewModel.getRecipeWithIngredients().ingredients.add(ingredient); //Add it to the recipe to the model
        viewModel.getAdapter().notifyItemInserted(viewModel.getRecipeWithIngredients().ingredients.size() - 1);
        viewModel.getAdapter().notifyDataSetChanged();
        viewModel.getBinding().setViewModel(viewModel); //Bind new ingredient to the viewModel(rebinding add to the bind)
        viewModel.getBinding().setSpinnerItemSelected(this);


        viewModel.getRecyclerView().scrollToPosition(viewModel.getRecipeWithIngredients().ingredients.size() - 1);
    }

    private void setupRecipeWithDefaultData() {
        /*
        Setup recipe with default values.
         */
        if (viewModel.getRecipeWithIngredients() == null) {
            viewModel.setRecipeWithIngredients(new RecipeWithIngredients());
        }
        if (viewModel.getRecipeWithIngredients().recipe == null) {
            viewModel.getRecipeWithIngredients().recipe = new Recipe();
        }

        // Recipe
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
        // These views are not data bound.
        RecipeViewModel.setSpinnerToValue(findViewById(R.id.fromMeasSystem), "All");
        RecipeViewModel.setSpinnerToValue(findViewById(R.id.toMeasSystem), "All");

        // Ingredients
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

    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

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

    public void saveRecipe(View view) {
        if (validRecipe()) {
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

    private boolean validRecipe() {
        boolean recipeValid = true;
        String message = "";

        if (viewModel.getRecipeWithIngredients().recipe.getName().equals("")) {
            // The name should not be blank. Let the user know with a toast or some other method.
            message = "Please enter a recipe name";
            recipeValid = false;

        } else {
            for (Ingredient ingredient : viewModel.getRecipeWithIngredients().ingredients) {
                if (ingredient.getName().equals("")) {
                    // One of the recipe names are blank. Let the user know.
                    message = "Please enter an ingredient name";
                    recipeValid = false;
                    break;
                }
                if (ingredient.getMeasurement().toLowerCase().equals("select")) {
                    message = "Please select a measurement for the ingredient";
                    recipeValid = false;
                    break;
                }
                if (ingredient.getConversionMeasurement().toLowerCase().equals("select")) {
                    message = "Please select a conversion measurement for the ingredient";
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

    public void deleteRecipe(View view) {
        /*
        Setup double confirmation when deleting a recipe.
         */
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //Set message
                .setTitle("Are you sure you want to delete the recipe.")
                //Set positive button
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Recipe will be deleted.
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
                //Set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Message will show that recipe was not deleted.
                        Toast.makeText(getApplicationContext(), "Recipe was not deleted.", Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }

    @Override
    public void deleteIngredient(int position) {
        if (viewModel.getRecipeWithIngredients().ingredients.size() > 1) {
            viewModel.getRecipeWithIngredients().ingredients.remove(position);
            viewModel.getAdapter().notifyItemRemoved(position);
            viewModel.getAdapter().notifyDataSetChanged();
        }
    }

    private boolean getIngredientNames() {
        DataGetIngredientNames dataGetIngredientNames = new DataGetIngredientNames(dataDao);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            viewModel.setIngredientNames(executorService.submit(dataGetIngredientNames).get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (viewModel.getIngredientNames() == null) {
            return false;
        } else {
            return true;
        }
    }

    public void onClickMyRecipe(View view) {
        Intent intent = new Intent(this, LoadRecipeActivity.class);
        startActivity(intent);
    }

    public static Double convertMeasurement(Double quantity, String startingUnit, String endingUnit) {
        /*
        Take the quantity from the user input, use the starting and ending unit of measurements to determine the correct conversion formula.
        Then return the new value.

        This method will only convert from volume to volume, weight to weight, temperature to temperature, and units does not change.

        This method will convert between US Imperial and Metric within the above approved conversion units.
         */

        switch (startingUnit) {
            case "fluid ounces":
                if ("cups".equals(endingUnit)) {
                    quantity /= 8;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 6;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 2;
                } else if ("pints".equals(endingUnit)) {
                    quantity /= 16;
                } else if ("quarts".equals(endingUnit)) {
                    quantity /= 32;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 128;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 29.574;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 33.814;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "cups":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity *= 8;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 48;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 16;
                } else if ("pints".equals(endingUnit)) {
                    quantity /= 2;
                } else if ("quarts".equals(endingUnit)) {
                    quantity /= 4;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 16;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 237;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 4.227;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "teaspoons":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity /= 6;
                } else if ("cups".equals(endingUnit)) {
                    quantity /= 48;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity /= 3;
                } else if ("pints".equals(endingUnit)) {
                    quantity /= 96;
                } else if ("quarts".equals(endingUnit)) {
                    quantity /= 192;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 768;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 4.929;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 203;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "tablespoons":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity /= 2;
                } else if ("cups".equals(endingUnit)) {
                    quantity /= 16;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 3;
                } else if ("pints".equals(endingUnit)) {
                    quantity /= 32;
                } else if ("quarts".equals(endingUnit)) {
                    quantity /= 64;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 256;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 14.787;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 67.628;
                } else {
                    System.out.println("Invalid entry");
                }

                break;
            case "pints":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity *= 16;
                } else if ("cups".equals(endingUnit)) {
                    quantity *= 2;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 96;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 32;
                } else if ("quarts".equals(endingUnit)) {
                    quantity /= 2;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 8;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 473;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 2.113;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "quarts":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity *= 32;
                } else if ("cups".equals(endingUnit)) {
                    quantity *= 3.94314;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 192;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 64;
                } else if ("pints".equals(endingUnit)) {
                    quantity *= 2;
                } else if ("gallons".equals(endingUnit)) {
                    quantity /= 4;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 946.353;
                } else if ("liters".equals(endingUnit)) {
                    quantity /= 1.057;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "gallons":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity *= 128;
                } else if ("cups".equals(endingUnit)) {
                    quantity *= 16;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 768;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 256;
                } else if ("pints".equals(endingUnit)) {
                    quantity *= 8;
                } else if ("quarts".equals(endingUnit)) {
                    quantity *= 4;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 3785.41;
                } else if ("liters".equals(endingUnit)) {
                    quantity *= 3.78541;
                } else {
                    System.out.println("Error in spelling Perhaps.");
                }

                break;
            case "ounces":
                if ("pounds".equals(endingUnit)) {
                    quantity /= 16;
                } else if ("grams".equals(endingUnit)) {
                    quantity *= 28.35;
                } else if ("kilograms".equals(endingUnit)) {
                    quantity /= 35.274;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "pounds":
                if ("ounces".equals(endingUnit)) {
                    quantity *= 16;
                } else if ("grams".equals(endingUnit)) {
                    quantity *= 454;
                } else if ("kilograms".equals(endingUnit)) {
                    quantity /= 2.205;
                } else {
                    System.out.println("Error in spelling Perhaps.");
                }

                break;
            case "milliliters":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity /= 28.413;
                } else if ("cups".equals(endingUnit)) {
                    quantity *= 0.00422675;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 0.202884;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 0.067628;
                } else if ("pints".equals(endingUnit)) {
                    quantity *= 0.00211338;
                } else if ("quarts".equals(endingUnit)) {
                    quantity *= 0.00105669;
                } else if ("gallons".equals(endingUnit)) {
                    quantity *= 0.000264172;
                } else if ("liters".equals(endingUnit)) {
                    quantity *= 0.001;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "liters":
                if ("fluid ounces".equals(endingUnit)) {
                    quantity *= 35.195;
                } else if ("cups".equals(endingUnit)) {
                    quantity *= 4.22675;
                } else if ("teaspoons".equals(endingUnit)) {
                    quantity *= 168.936;
                } else if ("tablespoons".equals(endingUnit)) {
                    quantity *= 56.3121;
                } else if ("pints".equals(endingUnit)) {
                    quantity *= 2.11338;
                } else if ("quarts".equals(endingUnit)) {
                    quantity *= 1.05669;
                } else if ("gallons".equals(endingUnit)) {
                    quantity *= 0.264172;
                } else if ("milliliters".equals(endingUnit)) {
                    quantity *= 1000;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "grams":
                if ("ounces".equals(endingUnit)) {
                    quantity /= 28.35;
                } else if ("pounds".equals(endingUnit)) {
                    quantity /= 454;
                } else if ("kilograms".equals(endingUnit)) {
                    quantity /= 1000;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;
            case "kilograms":
                if ("ounces".equals(endingUnit)) {
                    quantity *= 35.274;
                } else if ("pounds".equals(endingUnit)) {
                    quantity *= 2.205;
                } else if ("grams".equals(endingUnit)) {
                    quantity *= 1000;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;

            case "F": //Temperature
                //Temperature
                if ("C".equals(endingUnit)) {
                    quantity = (quantity - 32) * 5 / 9;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;

            case "C": //Temperature
                if ("F".equals(endingUnit)) {
                    quantity = (quantity * 9 / 5) + 32;
                } else {
                    System.out.println("Invalid Entry");
                }

            case "units": //Units
                if (!endingUnit.equals("units")){ System.out.println("Invalid Entry"); }
                else { return quantity; }
        }
        return quantity;
    }
}