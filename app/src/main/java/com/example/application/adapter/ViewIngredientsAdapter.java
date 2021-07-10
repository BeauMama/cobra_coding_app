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
import android.widget.CompoundButton;
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
import com.example.application.activity.RecipeActivity;
import com.example.application.databinding.IngredientlistRowBinding;
import com.example.application.viewmodel.RecipeViewModel;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {

    private int layoutId;
    private RecipeViewModel viewModel;
    private OnClickListener onClickListener;
    private Activity activity;
    private int selectPosition = -1;

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

        viewHolder.checkBox.setOnClickListener(view -> {
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
            if (viewHolder.checkBox.isChecked()) {
                viewHolder.byIngredient.setVisibility(View.VISIBLE);
                viewHolder.calcConvQty.setVisibility(View.INVISIBLE);
           } else {
                viewHolder.byIngredient.setVisibility(View.INVISIBLE);
                viewHolder.calcConvQty.setVisibility(View.VISIBLE);
            }
        } else {
            // Check box checked/unchecked. Uncheck all checkboxes for what was not checked
            // and show/hide controls that need to be.
            viewHolder.checkBox.setChecked(false);
            viewHolder.byIngredient.setVisibility(View.INVISIBLE);
            viewHolder.calcConvQty.setVisibility(View.VISIBLE);
        }


        viewHolder.spinnerMeasurementFrom.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                String measurementSelected = (String) parent.getItemAtPosition(position);
                String measurementType = MeasurementDetails.getMeasurementType( measurementSelected);

                Spinner spinner = (Spinner) viewHolder.spinnerMeasurementTo;
                ArrayAdapter adapter = new ArrayAdapter( activity, android.R.layout.simple_list_item_checked,
                        MeasurementDetails.getMeasurements(viewHolder.spinnerToSystem.getSelectedItem().toString(),measurementType ) );
                viewHolder.spinnerMeasurementTo.setAdapter( adapter );
                adapter.setDropDownViewResource( android.R.layout.simple_list_item_checked );
                spinner.setAdapter( adapter );


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );

        // I am not sure what listener event it needs to be when you select an item in the list.
        // The setOnItemSelectedListener might not be the right one.
        /*
        viewHolder.spinnerMeasurementFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Not sure if this gets the right spinner using parent. to get the measurement.
                String measurementSelected = MeasurementDetails.getMeasurementType(parent.getSelectedItem().toString());

                // Get the type from the enum
                String measurementType = MeasurementDetails.getMeasurementType(measurementSelected);

                //This needs te be adjusted to get a list based on the measurementType value
                List<String> measurements = MeasurementDetails.getMeasurements("all", "volume");

                // Not sure if this gets the right spinner using view.
                Spinner spinnerConvMeasurement = view.findViewById(R.id.convMeasurement);

                // Set up adapter to be used to update the spinner.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, measurements);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                //Update the spinner with the new list of items.
                //spinnerConvMeasurement.setAdapter(adapter); // currently crashes app
                }


        });

         */
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
        private CheckBox checkBox;
        private EditText byIngredient;
        private Spinner spinnerMeasurementFrom;
        private Spinner spinnerMeasurementTo;
        private Spinner spinnerFromSystem;
        private Spinner spinnerToSystem;



        public ViewHolder(@NonNull @NotNull IngredientlistRowBinding ingredientlistRowBinding, OnClickListener onClickListener) {
            super(ingredientlistRowBinding.getRoot());
            this.ingredientlistRowBinding = ingredientlistRowBinding;

            calcConvQty = itemView.findViewById(R.id.calcConvQuantity);

            textView = itemView.findViewById(R.id.ingredentName);
            ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
            textView.setThreshold(1);
            textView.setAdapter(adapter);

            byIngredient = itemView.findViewById(R.id.editOneIngredient);

            this.onClickListener = onClickListener;
            button = itemView.findViewById(R.id.buttonDeleteIngredient);
            button.setOnClickListener(this);

            checkBox = itemView.findViewById(R.id.checkBoxIsConvIngredient);
            checkBox.setOnClickListener(this);

            spinnerMeasurementFrom = itemView.findViewById(R.id.measurement);
            spinnerMeasurementTo = itemView.findViewById( R.id.convMeasurement );
            spinnerFromSystem = activity.findViewById(R.id.fromMeasSystem);
            spinnerToSystem = activity.findViewById( R.id.toMeasSystem );
            String measurementType = MeasurementDetails.getMeasurementType( spinnerMeasurementFrom.getSelectedItem().toString());

            ArrayAdapter adapter1 = new ArrayAdapter( activity, android.R.layout.simple_list_item_checked,
                    MeasurementDetails.getMeasurements(spinnerFromSystem.getSelectedItem().toString(),"All") );
            spinnerMeasurementFrom.setAdapter( adapter1 );

            ArrayAdapter adapter2 = new ArrayAdapter( activity, android.R.layout.simple_list_item_checked,
                    MeasurementDetails.getMeasurements(spinnerToSystem.getSelectedItem().toString(),measurementType ) );
            spinnerMeasurementTo.setAdapter( adapter2 );

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
                    onClickListener.deleteButtonClick(getAdapterPosition());
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
        void deleteButtonClick(int position);
        //void ingredientCheckboxClick(int position);
    }
}