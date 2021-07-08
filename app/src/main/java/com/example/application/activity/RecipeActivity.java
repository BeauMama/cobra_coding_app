package com.example.application.activity;

import android.content.Intent;
import android.os.Bundle;
import android.service.controls.actions.BooleanAction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;
import com.example.application.adapter.ViewIngredientsAdapter;
import com.example.application.database.DataDao;
import com.example.application.database.DataGetIngredientNames;
import com.example.application.database.DataGetRecipeWithIngredientsById;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.database.DataSaveRecipeWithIngredients;
import com.example.application.databinding.ActivityRecipeBinding;
import com.example.application.model.Ingredient;
import com.example.application.model.Recipe;
import com.example.application.model.RecipeWithIngredients;
import com.example.application.viewmodel.RecipeViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeActivity extends AppCompatActivity implements ViewIngredientsAdapter.OnClickListener {

    private DataDao dataDao;
    private RecyclerView recyclerView;
    private ViewIngredientsAdapter viewIngredientsAdapter;
    private RecipeViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        viewModel = new ViewModelProvider(this).get(RecipeViewModel.class);
        if (savedInstanceState == null) {
            viewModel.init(this);
        }

        initializeDatabase();

        if (loadRecipeWithIngredients() == false) {
            setupRecipeWithDummyData();

        }

        ActivityRecipeBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe);
        initializeRecycleView();
        binding.setViewModel(viewModel);

        if (getIngredientNames() == false) {
            viewModel.setIngredientNames(Arrays.asList("cinnamon","flour", "oil", "water"));
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

            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean validRecipe() {
        Boolean recipeValid = true;

        if (viewModel.getRecipeWithIngredients().recipe.getName().equals("")) {
            // The name should not be blank. Let the user know with a toast or some other method.
            recipeValid = false;
        }

        for (Ingredient ingredient: viewModel.getRecipeWithIngredients().ingredients) {
            if (ingredient.getName().equals("")) {
                // One of the recipe names are blank. Let the user know.
                recipeValid = false;
            }
        }

        return recipeValid;
    }

    private void initializeRecycleView() {
        recyclerView = findViewById(R.id.ingredientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewIngredientsAdapter = viewModel.getAdapter();
        recyclerView.setAdapter(viewIngredientsAdapter);
    }

    public void addIngredient(View view) {
        Ingredient ingredient = new Ingredient();
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        viewModel.getRecipeWithIngredients().ingredients.add(ingredient);
        viewIngredientsAdapter.notifyItemInserted(viewModel.getRecipeWithIngredients().ingredients.size() - 1);
    }

    @Override
    public void deleteButtonClick(int position) {
        if (viewModel.getRecipeWithIngredients().ingredients.size() > 1) {
            viewModel.getRecipeWithIngredients().ingredients.remove(position);
            viewIngredientsAdapter.notifyItemRemoved(position);
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

    private void setupRecipeWithDummyData() {
        // Temporarily setting data for testing.
        viewModel.setRecipeWithIngredients(new RecipeWithIngredients());
        viewModel.getRecipeWithIngredients().recipe = new Recipe();
        viewModel.getRecipeWithIngredients().recipe.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        viewModel.getRecipeWithIngredients().recipe.setName("Scrambled eggs");
        viewModel.getRecipeWithIngredients().recipe.setServingSize(2); // Not needed for this example.
        viewModel.getRecipeWithIngredients().recipe.setCookTimeMinutes(4);
        viewModel.getRecipeWithIngredients().recipe.setTemperature(180);
        viewModel.getRecipeWithIngredients().recipe.setTemperatureMeasurement("C");
        viewModel.getRecipeWithIngredients().recipe.setConversionTemperatureMeasurement("F");
        viewModel.getRecipeWithIngredients().recipe.setConversionType("One Ingredient"); // Example by one ingredient conversion
        viewModel.getRecipeWithIngredients().recipe.setConversionAmount((double) 2.5); // Not needed for this example
        viewModel.getRecipeWithIngredients().recipe.setNotes("This is my favorite scrambled egg recipe!");
        viewModel.getRecipeWithIngredients().recipe.setFromSystem("Metric");
        viewModel.getRecipeWithIngredients().recipe.setToSystem("Imperial");

        viewModel.getRecipeWithIngredients().ingredients = new ArrayList<>();

        Ingredient ingredient = new Ingredient();
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        ingredient.setName("milk");
        ingredient.setMeasurement("cups");
        ingredient.setConversionMeasurement("cups");
        ingredient.setQuantity((double) 60);
        ingredient.setIsConversionIngredient(false);
        viewModel.getRecipeWithIngredients().ingredients.add(ingredient);

        ingredient = new Ingredient();
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        ingredient.setName("eggs");
        ingredient.setMeasurement("cups");
        ingredient.setConversionMeasurement("tablespoons");
        ingredient.setQuantity((double) 5);
        ingredient.setIsConversionIngredient(true); // This is the conversion ingredient.
        ingredient.setConversionIngredientQuantity(4); // Recipe calls for 5 eggs but we only have 4.
        viewModel.getRecipeWithIngredients().ingredients.add(ingredient);

        ingredient = new Ingredient();
        ingredient.setRecipeWithIngredients(viewModel.getRecipeWithIngredients());
        ingredient.setName("salt");
        ingredient.setMeasurement("tablespoons");
        ingredient.setConversionMeasurement("teaspoons");
        ingredient.setQuantity((double) 5);
        ingredient.setIsConversionIngredient(false);
        viewModel.getRecipeWithIngredients().ingredients.add(ingredient);
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

            case "fahrenheit": //Temperature
                //Temperature
                if ("celsius".equals(endingUnit)) {
                    quantity = (quantity - 32) * 5 / 9;
                } else {
                    System.out.println("Invalid Entry");
                }

                break;

            case "celsius": //Temperature
                if ("fahrenheit".equals(endingUnit)) {
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