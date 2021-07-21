package com.example.application.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.application.BR;
import com.example.application.model.MeasurementDetails;
import com.example.application.R;
import com.example.application.databinding.IngredientlistRowBinding;
import com.example.application.viewmodel.RecipeViewModel;
import org.jetbrains.annotations.NotNull;

/**
 * The adapter for the RecyclerView for a list of ingredients located in the RecipeActivity.
 */
public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {
    private final RecipeViewModel viewModel;
    private final OnClickListener onClickListener;
    private final Activity activity;
    private int selectPosition = -1;
    private Boolean initializeSpinners = true;

    /**
     * Constructor.
     *
     * @param viewModel The view model for adapter to access.
     * @param activity The activity the RecyclerView belongs to.
     */
    public ViewIngredientsAdapter(RecipeViewModel viewModel, Activity activity) {
        this.viewModel = viewModel;
        this.onClickListener = (OnClickListener) activity;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IngredientlistRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.ingredientlist_row, parent, false);

        return new ViewHolder(binding, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewIngredientsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(viewModel, position);

        // Set up the measurement spinner with the right list of items and value.
        ArrayAdapter adapterMeasurement = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                MeasurementDetails.getMeasurements(viewHolder.spinnerFromSystem.getSelectedItem().toString(),"All"));
        viewHolder.spinnerMeasurement.setAdapter(adapterMeasurement);

        RecipeViewModel.setSpinnerToValue(viewHolder.spinnerMeasurement,
                viewModel.getRecipeWithIngredients().ingredients.get(position).getMeasurement());

        // Set up the conversion measurement spinner with the right list of items and value.
        String measurementType = MeasurementDetails.getMeasurementType(viewHolder.spinnerMeasurement.getSelectedItem().toString());
        ArrayAdapter adapterConversionMeasurement = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                MeasurementDetails.getMeasurements(viewHolder.spinnerToSystem.getSelectedItem().toString(), measurementType));
        viewHolder.spinnerConversionMeasurement.setAdapter(adapterConversionMeasurement);

        RecipeViewModel.setSpinnerToValue(viewHolder.spinnerConversionMeasurement,
                viewModel.getRecipeWithIngredients().ingredients.get(position).getConversionMeasurement());

        // Set up listener for ingredient check boxes.
        viewHolder.checkBoxIsConvIngredient.setOnClickListener(view -> {
            selectPosition = viewHolder.getAdapterPosition();
            notifyDataSetChanged();
        });

        // Update controls based on what checkbox was checked.
        if (selectPosition == -1) {
            // Checkbox has never been pressed. Set up visibility of items based on the recipe.
            if (viewModel.getRecipeWithIngredients().ingredients.get(position).getIsConversionIngredient()) {
                viewHolder.editIngredientQty.setVisibility(View.VISIBLE);
                viewHolder.calcConvQty.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.editIngredientQty.setVisibility(View.INVISIBLE);
                viewHolder.calcConvQty.setVisibility(View.VISIBLE);
            }
        } else if (selectPosition == position) {
            // Check boxed checked/unchecked. Hide/show the items for the related ingredient.
            if (viewHolder.checkBoxIsConvIngredient.isChecked()) {
                viewHolder.editIngredientQty.setVisibility(View.VISIBLE);
                viewHolder.calcConvQty.setVisibility(View.INVISIBLE);
           } else {
                viewHolder.editIngredientQty.setVisibility(View.INVISIBLE);
                viewHolder.calcConvQty.setVisibility(View.VISIBLE);
            }
        } else {
            // Check box checked/unchecked. Uncheck all checkboxes for what was not checked
            // and show/hide controls that need to be.
            viewHolder.checkBoxIsConvIngredient.setChecked(false);
            viewHolder.editIngredientQty.setVisibility(View.INVISIBLE);
            viewHolder.calcConvQty.setVisibility(View.VISIBLE);
        }

        /**
         * Listener for the measurement spinner to update the related UI controls when it is
         * changed.
         */
        viewHolder.spinnerMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                String measurementSelected = parent.getItemAtPosition(spinnerPosition).toString();
                String measurementType = MeasurementDetails.getMeasurementType(measurementSelected);

                // Update the model with the changed spinner value
                viewModel.getRecipeWithIngredients().ingredients.get(position).setMeasurement(measurementSelected);

                // Update the conversion measurement spinner based on what value was selected for
                // the measurement spinner.
                Spinner spinnerConversionMeasurement = viewHolder.spinnerConversionMeasurement;
                String oldMeasurementValue = spinnerConversionMeasurement.getSelectedItem().toString();

                ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                        MeasurementDetails.getMeasurements(viewHolder.spinnerToSystem.getSelectedItem().toString(), measurementType));
                viewHolder.spinnerConversionMeasurement.setAdapter(adapter);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                spinnerConversionMeasurement.setAdapter(adapter);

                // After changing the spinner list, set it back to what was selected if the item
                // is still in the list
                RecipeViewModel.setSpinnerToValue(spinnerConversionMeasurement, oldMeasurementValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    @Override
    public int getItemCount() {
        return viewModel.getRecipeWithIngredients().ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public IngredientlistRowBinding ingredientlistRowBinding;
        private final AutoCompleteTextView ingredientName;
        private final OnClickListener onClickListener;
        private final TextView calcConvQty;
        private final EditText editIngredientQty;
        private final CheckBox checkBoxIsConvIngredient;
        private final Spinner spinnerMeasurement;
        private final Spinner spinnerConversionMeasurement;
        private final Spinner spinnerFromSystem;
        private final Spinner spinnerToSystem;

        public ViewHolder(@NonNull @NotNull IngredientlistRowBinding ingredientlistRowBinding, OnClickListener onClickListener) {
            super(ingredientlistRowBinding.getRoot());
            this.ingredientlistRowBinding = ingredientlistRowBinding;
            this.onClickListener = onClickListener;

            // Set up auto complete for the ingredient name.
            ingredientName = itemView.findViewById(R.id.ingredientName);
            ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
            ingredientName.setThreshold(1);
            ingredientName.setAdapter(adapter);

            // Set up listener for delete button.
            Button deleteButton = itemView.findViewById(R.id.buttonDeleteIngredient);
            deleteButton.setOnClickListener(this);

            // Set up listener for checkboxes.
            checkBoxIsConvIngredient = itemView.findViewById(R.id.checkBoxIsConvIngredient);
            checkBoxIsConvIngredient.setOnClickListener(this);

            calcConvQty = itemView.findViewById(R.id.calcConvQuantity);
            editIngredientQty = itemView.findViewById(R.id.editIngredientQty);
            spinnerMeasurement = itemView.findViewById(R.id.measurement);
            spinnerConversionMeasurement = itemView.findViewById(R.id.convMeasurement);
            spinnerFromSystem = activity.findViewById(R.id.fromMeasSystem);
            spinnerToSystem = activity.findViewById(R.id.toMeasSystem);
        }

        @Override
        public void onClick(View view) {
            onClickListener.deleteIngredient(getAdapterPosition());
        }

        public void bind(RecipeViewModel viewModel, int position) {
            ingredientlistRowBinding.setVariable(BR.viewModel, viewModel);
            ingredientlistRowBinding.setVariable(BR.position, position);
            ingredientlistRowBinding.executePendingBindings();
        }
    }

    public interface OnClickListener {
        void deleteIngredient(int position);
    }
}