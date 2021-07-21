package com.example.application.viewmodel;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.model.MeasurementDetails;
import com.example.application.R;
import com.example.application.adapter.ViewIngredientsAdapter;
import com.example.application.databinding.ActivityRecipeBinding;
import com.example.application.model.RecipeWithIngredients;
import java.util.List;

/**
 * The view model to be used for the RecipeActivity.
 */
public class RecipeViewModel extends ViewModel {

    private RecipeWithIngredients recipeWithIngredients;
    private List<String> ingredientNames;
    private ViewIngredientsAdapter adapter;
    private Activity activity;
    private RecyclerView recyclerView;
    private ViewIngredientsAdapter viewIngredientsAdapter;
    private ActivityRecipeBinding binding;

    public ActivityRecipeBinding getBinding() {
        return binding;
    }
    public void setBinding(ActivityRecipeBinding binding) {
        this.binding = binding;
    }

    public ViewIngredientsAdapter getViewIngredientsAdapter() {
        return viewIngredientsAdapter;
    }
    public void setViewIngredientsAdapter(ViewIngredientsAdapter viewIngredientsAdapter) {
        this.viewIngredientsAdapter = viewIngredientsAdapter;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;

        initializeRecyclerViewListener();
    }

    /**
     * Used to initialize the view model.
     *
     * @param activity The activity for the view model.
     */
    public void init(Activity activity) {
        adapter = new ViewIngredientsAdapter(this, activity);
        this.activity = activity;
    }

    /**
     * Sets up the RecyclerView listener so controls can be updated as they come into view.
     * This method uses an on scroll listener and could be more efficient if it was a different
     * listener to run when a new row in the recyclerview becomes partially visible.
     */
    private void initializeRecyclerViewListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                Spinner spinnerConvType = activity.findViewById(R.id.convType);
                String convType = spinnerConvType.getSelectedItem().toString();
                Spinner spinnerFromMeasSystem = activity.findViewById(R.id.fromMeasSystem);
                String fromMeasSystem = spinnerFromMeasSystem.getSelectedItem().toString();
                Spinner spinnerToMeasSystem = activity.findViewById(R.id.toMeasSystem);
                String toMeasSystem = spinnerToMeasSystem.getSelectedItem().toString();

                List<String> measurements = MeasurementDetails.getMeasurements(fromMeasSystem, "all");

                for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                    View ingredientView = recyclerView.getLayoutManager().findViewByPosition(i);
                    if (ingredientView != null) {
                        CheckBox checkBoxIsConvIngredient = ingredientView.findViewById(R.id.checkBoxIsConvIngredient);
                        TextView calcConvQty = ingredientView.findViewById(R.id.calcConvQuantity);
                        EditText byIngredient = ingredientView.findViewById(R.id.editIngredientQty);
                        Spinner spinnerMeasurement = ingredientView.findViewById(R.id.measurement);
                        String oldMeasurementValue = spinnerMeasurement.getSelectedItem().toString();
                        Spinner spinnerConvMeasurement = ingredientView.findViewById(R.id.convMeasurement);
                        String oldConvMeasurementValue = spinnerConvMeasurement.getSelectedItem().toString();

                        // Set up controls for the one ingredient setting.
                        if (convType.toLowerCase().equals("one ingredient")) {
                            checkBoxIsConvIngredient.setVisibility(View.VISIBLE);
                            if (checkBoxIsConvIngredient.isChecked()) {
                                byIngredient.setVisibility(View.VISIBLE);
                                calcConvQty.setVisibility(View.INVISIBLE);
                            } else {
                                byIngredient.setVisibility(View.INVISIBLE);
                                calcConvQty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            checkBoxIsConvIngredient.setVisibility(View.INVISIBLE);
                            byIngredient.setVisibility(View.INVISIBLE);
                            calcConvQty.setVisibility(View.VISIBLE);
                        }


                        // From Measurement System
                        ArrayAdapter<String> fromMeasSystemAdapter = new ArrayAdapter<>(ingredientView.getContext(), android.R.layout.simple_list_item_checked, measurements);
                        fromMeasSystemAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                        spinnerMeasurement.setAdapter(fromMeasSystemAdapter);
                        // After changing the spinner list, set it back to what it was selected to before if the item
                        // is still in the list
                        RecipeViewModel.setSpinnerToValue(spinnerMeasurement, oldMeasurementValue);

                        // To Measurement System
                        String measurementTypeSelected = MeasurementDetails.getMeasurementType(spinnerMeasurement.getSelectedItem().toString());
                        List<String> convMeasurements = MeasurementDetails.getMeasurements(toMeasSystem, measurementTypeSelected);

                        ArrayAdapter<String> toMeasSystemAdapter = new ArrayAdapter<>(ingredientView.getContext(), android.R.layout.simple_list_item_checked, convMeasurements);
                        toMeasSystemAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                        spinnerConvMeasurement.setAdapter(toMeasSystemAdapter);

                        // After changing the spinner list, set it back to what it was selected to before if the item
                        // is still in the list
                        RecipeViewModel.setSpinnerToValue(spinnerConvMeasurement, oldConvMeasurementValue);
                    }
                }
            }
        });
    }

    public ViewIngredientsAdapter getAdapter() { return adapter; }

    public RecipeWithIngredients getRecipeWithIngredients() {
        return recipeWithIngredients;
    }
    public void setRecipeWithIngredients(RecipeWithIngredients recipeWithIngredients) {
        this.recipeWithIngredients = recipeWithIngredients;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }
    public void setIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
    }

    /**
     * Changes the value of a spinner.
     *
     * @param spinner The spinner that you want to set to a specific value.
     * @param value The value that you want to set the spinner to.
     * @return If the spinner was able to be set to the value, then true is returned.
     *         If not, then false is returned.
     */
    public static Boolean setSpinnerToValue(Spinner spinner, String value) {

        for (int itemPosition = 0; itemPosition < spinner.getAdapter().getCount(); itemPosition++) {
            String itemValue = (String) spinner.getAdapter().getItem(itemPosition);
            if (itemValue.toLowerCase().equals(value.toLowerCase())) {
                spinner.setSelection(itemPosition, false);
                return true;
            }
        }
        return false;
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
