package com.example.application.viewmodel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.MeasurementDetails;
import com.example.application.R;
import com.example.application.adapter.ViewIngredientsAdapter;
import com.example.application.databinding.ActivityRecipeBinding;
import com.example.application.model.RecipeWithIngredients;
import java.util.List;

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
                        EditText byIngredient = ingredientView.findViewById(R.id.editOneIngredient);
                        Spinner spinnerMeasurement = ingredientView.findViewById(R.id.measurement);
                        String oldMeasurementValue = spinnerMeasurement.getSelectedItem().toString();
                        Spinner spinnerConvMeasurement = ingredientView.findViewById(R.id.convMeasurement);
                        String oldConvMeasurementValue = spinnerConvMeasurement.getSelectedItem().toString();


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

    public void init(Activity activity) {
        adapter = new ViewIngredientsAdapter( this, activity);
        this.activity = activity;
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
}
