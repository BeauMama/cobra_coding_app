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

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.BR;
import com.example.application.MeasurementDetails;
import com.example.application.R;
import com.example.application.databinding.IngredientlistRowBinding;
import com.example.application.viewmodel.RecipeViewModel;

import org.jetbrains.annotations.NotNull;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {

    private int layoutId;
    private RecipeViewModel viewModel;
    private OnClickListener onClickListener;
    private Activity activity;
    private int selectPosition = -1;
    private Boolean initializeSpinners = true;

    public ViewIngredientsAdapter(@LayoutRes int layoutId, RecipeViewModel viewModel, Activity activity) {
        this.layoutId = layoutId;
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

        ArrayAdapter adapterMeasurement = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                MeasurementDetails.getMeasurements(viewHolder.spinnerFromSystem.getSelectedItem().toString(),"All"));
        viewHolder.spinnerMeasurement.setAdapter(adapterMeasurement);

        RecipeViewModel.setSpinnerToValue(viewHolder.spinnerMeasurement,
                viewModel.getRecipeWithIngredients().ingredients.get(position).getMeasurement());

        String measurementType = MeasurementDetails.getMeasurementType(viewHolder.spinnerMeasurement.getSelectedItem().toString());
        ArrayAdapter adapterConversionMeasurement = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                MeasurementDetails.getMeasurements(viewHolder.spinnerToSystem.getSelectedItem().toString(), measurementType));
        viewHolder.spinnerConversionMeasurement.setAdapter(adapterConversionMeasurement);

        RecipeViewModel.setSpinnerToValue(viewHolder.spinnerConversionMeasurement,
                viewModel.getRecipeWithIngredients().ingredients.get(position).getConversionMeasurement());

        viewHolder.checkBoxIsConvIngredient.setOnClickListener(view -> {
            selectPosition = viewHolder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectPosition == -1) {
            // Checkbox has never been pressed. Set up visibility of items based on the recipe.
            if (viewModel.getRecipeWithIngredients().ingredients.get(position).getIsConversionIngredient()) {
                viewHolder.byIngredient.setVisibility(View.VISIBLE);
                viewHolder.calcConvQty.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.byIngredient.setVisibility(View.INVISIBLE);
                viewHolder.calcConvQty.setVisibility(View.VISIBLE);
            }
        } else if (selectPosition == position) {
            // Check boxed checked/unchecked. Hide/show the items for the related ingredient.
            if (viewHolder.checkBoxIsConvIngredient.isChecked()) {
                viewHolder.byIngredient.setVisibility(View.VISIBLE);
                viewHolder.calcConvQty.setVisibility(View.INVISIBLE);
           } else {
                viewHolder.byIngredient.setVisibility(View.INVISIBLE);
                viewHolder.calcConvQty.setVisibility(View.VISIBLE);
            }
        } else {
            // Check box checked/unchecked. Uncheck all checkboxes for what was not checked
            // and show/hide controls that need to be.
            viewHolder.checkBoxIsConvIngredient.setChecked(false);
            viewHolder.byIngredient.setVisibility(View.INVISIBLE);
            viewHolder.calcConvQty.setVisibility(View.VISIBLE);
        }

        viewHolder.spinnerMeasurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {

                String measurementSelected = parent.getItemAtPosition(spinnerPosition).toString();
                String measurementType = MeasurementDetails.getMeasurementType(measurementSelected);

                viewModel.getRecipeWithIngredients().ingredients.get(position).setMeasurement(measurementSelected);

                Spinner spinnerConversionMeasurement = (Spinner) viewHolder.spinnerConversionMeasurement;


                String oldMeasurementValue = spinnerConversionMeasurement.getSelectedItem().toString();

                ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_checked,
                        MeasurementDetails.getMeasurements(viewHolder.spinnerToSystem.getSelectedItem().toString(), measurementType));
                viewHolder.spinnerConversionMeasurement.setAdapter(adapter);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);
                spinnerConversionMeasurement.setAdapter(adapter);

                // After changing the spinner list, set it back to what it was selected to before if the item
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
        private AutoCompleteTextView textView;
        private Button button;
        private OnClickListener onClickListener;
        public IngredientlistRowBinding ingredientlistRowBinding;
        private TextView calcConvQty;
        private CheckBox checkBoxIsConvIngredient;
        private EditText byIngredient;
        private Spinner spinnerMeasurement;
        private Spinner spinnerConversionMeasurement;
        private Spinner spinnerFromSystem;
        private Spinner spinnerToSystem;
        private Spinner spinnerConvertBy;

        public ViewHolder(@NonNull @NotNull IngredientlistRowBinding ingredientlistRowBinding, OnClickListener onClickListener) {
            super(ingredientlistRowBinding.getRoot());
            this.ingredientlistRowBinding = ingredientlistRowBinding;

            spinnerConvertBy = activity.findViewById(R.id.convType );
            calcConvQty = itemView.findViewById(R.id.calcConvQuantity);
            textView = itemView.findViewById(R.id.ingredientName);
            ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
            textView.setThreshold(1);
            textView.setAdapter(adapter);
            byIngredient = itemView.findViewById(R.id.editOneIngredient);
            this.onClickListener = onClickListener;
            button = itemView.findViewById(R.id.buttonDeleteIngredient);
            button.setOnClickListener(this);
            checkBoxIsConvIngredient = itemView.findViewById(R.id.checkBoxIsConvIngredient);
            checkBoxIsConvIngredient.setOnClickListener(this);

            spinnerMeasurement = itemView.findViewById(R.id.measurement);
            spinnerConversionMeasurement = itemView.findViewById( R.id.convMeasurement );
            spinnerFromSystem = activity.findViewById(R.id.fromMeasSystem);
            spinnerToSystem = activity.findViewById( R.id.toMeasSystem );



                   }
        public TextView getTextView() {
            return textView;
        }
        public TextView getCalcConvQty() {
            return calcConvQty;
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonDeleteIngredient:
                    onClickListener.deleteIngredient(getAdapterPosition());
                    break;
                default:
                    break;
            }
        }

        public void bind(RecipeViewModel viewModel, int position) {
            ingredientlistRowBinding.setVariable(BR.viewModel, viewModel);
            ingredientlistRowBinding.setVariable(BR.position, position);
            ingredientlistRowBinding.executePendingBindings();
        }
    }

    public interface OnClickListener {
        void deleteIngredient(int position);
        //void ingredientCheckboxClick(int position);
    }
}