package com.example.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.database.DataDao;
import com.example.application.database.DataGetIngredientNames;
import com.example.application.database.DataGetRecipeWithIngredientsById;
import com.example.application.database.DataInitializeDatabase;
import com.example.application.database.DataSaveRecipeWithIngredients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RecipeActivity extends AppCompatActivity implements ViewIngredientsAdapter.DeleteButtonListener {

    private DataDao dataDao;
    public RecipeWithIngredients recipeWithIngredients;
    private List<String> ingredientNames;
    private RecyclerView recyclerView;
    private ViewIngredientsAdapter viewIngredientsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        initializeDatabase();

        if (loadRecipeWithIngredients() == false) {
            setupRecipeWithDummyData();
        }

        if (getIngredientNames() == false) {
            ingredientNames = Arrays.asList("cinnamon","flour", "oil", "water");
        }

        initializeRecycleView();
    }

    private void initializeDatabase() {
        dataDao = DataInitializeDatabase.getInstance(getApplicationContext());
    }

    private boolean loadRecipeWithIngredients() {
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        if (intent != null) {
            DataGetRecipeWithIngredientsById dataGetRecipeWithIngredientsById = new DataGetRecipeWithIngredientsById(dataDao, id);
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            try {
                recipeWithIngredients = executorService.submit(dataGetRecipeWithIngredientsById).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (recipeWithIngredients == null) {
            return false;
        } else {
            return true;
        }
    }

    private long saveRecipe() {
        DataSaveRecipeWithIngredients dataSaveRecipeWithIngredients = new DataSaveRecipeWithIngredients(dataDao, recipeWithIngredients);
        long id = -1;
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            id = executorService.submit(dataSaveRecipeWithIngredients).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return id;
    }

    private void initializeRecycleView() {
        recyclerView = findViewById(R.id.ingredientList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewIngredientsAdapter = new ViewIngredientsAdapter(recipeWithIngredients, ingredientNames,this);
        recyclerView.setAdapter(viewIngredientsAdapter);
    }

    public void addIngredient(View view) {
        Ingredient ingredient = new Ingredient();
        recipeWithIngredients.ingredients.add(ingredient);
        viewIngredientsAdapter.notifyItemInserted(recipeWithIngredients.ingredients.size() - 1);
    }

    @Override
    public void deleteButtonClick(int position) {
        if (recipeWithIngredients.ingredients.size() > 1) {
            recipeWithIngredients.ingredients.remove(position);
            viewIngredientsAdapter.notifyItemRemoved(position);
        }
    }

    private boolean getIngredientNames() {
        DataGetIngredientNames dataGetIngredientNames = new DataGetIngredientNames(dataDao);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        try {
            ingredientNames = executorService.submit(dataGetIngredientNames).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (ingredientNames == null) {
            return false;
        } else {
            return true;
        }
    }

    private void setupRecipeWithDummyData() {
        // Temporarily setting data for testing.
        recipeWithIngredients = new RecipeWithIngredients();
        recipeWithIngredients.recipe = new Recipe();

        recipeWithIngredients.recipe.setName("Scrambled eggs");
        //recipeWithIngredients.recipe.setServingSize(2); // Not needed for this example.
        recipeWithIngredients.recipe.setCookTimeMinutes(4);
        recipeWithIngredients.recipe.setTemperature(180);
        recipeWithIngredients.recipe.setTemperatureMeasurement("celsius");
        recipeWithIngredients.recipe.setConversionTemperatureMeasurement("fahrenheit");
        recipeWithIngredients.recipe.setConversionType("One Ingredient"); // Example by one ingredient conversion
        //recipeWithIngredients.recipe.setConversionAmount((float) 2.5); // Not needed for this example
        recipeWithIngredients.recipe.setNotes("This is my favorite scrambled egg recipe!");
        recipeWithIngredients.recipe.setFromSystem("Metric");
        recipeWithIngredients.recipe.setToSystem("Imperial");

        recipeWithIngredients.ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setMeasurement("units");
        ingredient.setConversionMeasurement("units");
        ingredient.setQuantity((float) 5);
        ingredient.setIsConversionIngredient(true); // This is the conversion ingredient.
        ingredient.setConversionIngredientQuantity(4); // Recipe calls for 5 eggs but we only have 4.
        recipeWithIngredients.ingredients.add(ingredient);

        ingredient = new Ingredient();
        ingredient.setName("milk");
        ingredient.setMeasurement("milliliters");
        ingredient.setConversionMeasurement("cups");
        ingredient.setQuantity((float) 60);
        ingredient.setIsConversionIngredient(false);
        recipeWithIngredients.ingredients.add(ingredient);

        ingredient = new Ingredient();
        ingredient.setName("salt");
        ingredient.setMeasurement("grams");
        ingredient.setConversionMeasurement("teaspoons");
        ingredient.setQuantity((float) 5);
        ingredient.setIsConversionIngredient(false);
        recipeWithIngredients.ingredients.add(ingredient);
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