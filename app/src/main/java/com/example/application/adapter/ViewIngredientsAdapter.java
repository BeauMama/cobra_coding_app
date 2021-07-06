package com.example.application.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.BR;
import com.example.application.R;
import com.example.application.databinding.IngredientlistRowBinding;
import com.example.application.viewmodel.RecipeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {

    private int layoutId;
    private RecipeViewModel viewModel;
    private DeleteButtonListener deleteButtonListener;
    private Activity activity;
    private int selectPosition = -1;

    public ViewIngredientsAdapter(@LayoutRes int layoutId, RecipeViewModel viewModel, Activity activity) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.deleteButtonListener = (DeleteButtonListener) activity;
        this.activity = activity;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        IngredientlistRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.ingredientlist_row, parent, false);

        return new ViewHolder(binding, deleteButtonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewIngredientsAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bind(viewModel, position);

        //viewHolder.getCalcConvQty().setText(Float.toString(ingredient.getQuantity() * 2));
        viewHolder.checkBox.setOnClickListener(view -> {
            selectPosition = viewHolder.getAdapterPosition();
            notifyDataSetChanged();
        });

        if (selectPosition == position){
            viewHolder.checkBox.setChecked(true);
            viewHolder.byIngredient.setVisibility(View.VISIBLE);
            viewHolder.calcConvQty.setVisibility(View.INVISIBLE);

        }
        else{
            viewHolder.checkBox.setChecked(false);
            viewHolder.byIngredient.setVisibility(View.INVISIBLE);
            viewHolder.calcConvQty.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public int getItemCount() {
        return viewModel.getRecipeWithIngredients().ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private AutoCompleteTextView textView;
        private Button button;
        private DeleteButtonListener deleteButtonListener;
        public IngredientlistRowBinding ingredientlistRowBinding;
        private TextView calcConvQty;
        private CheckBox checkBox;
        private EditText byIngredient;

        public ViewHolder(@NonNull @NotNull IngredientlistRowBinding ingredientlistRowBinding, DeleteButtonListener deleteButtonListener) {
            super(ingredientlistRowBinding.getRoot());
            this.ingredientlistRowBinding = ingredientlistRowBinding;

            calcConvQty = itemView.findViewById(R.id.calcConvQuantity);

            textView = itemView.findViewById(R.id.ingredentName);
            ArrayAdapter adapter = new ArrayAdapter(activity, android.R.layout.simple_list_item_1, viewModel.getIngredientNames());
            textView.setThreshold(1);
            textView.setAdapter(adapter);

            button = itemView.findViewById(R.id.buttonDeleteIngredient);
            this.deleteButtonListener = deleteButtonListener;
            button.setOnClickListener(this);

            checkBox = itemView.findViewById(R.id.checkBoxIsConvIngredient);
            byIngredient = itemView.findViewById(R.id.editOneIngredient);
        }
        public TextView getTextView() {
            return textView;
        }
        public TextView getCalcConvQty() {
            return calcConvQty;
        }

        @Override
        public void onClick(View view) {
            deleteButtonListener.deleteButtonClick(getAdapterPosition());
        }

        public void bind(RecipeViewModel viewModel, int position) {
            ingredientlistRowBinding.setVariable(BR.viewModel, viewModel);
            ingredientlistRowBinding.setVariable(BR.position, position);
            ingredientlistRowBinding.executePendingBindings();
        }
    }

    public interface DeleteButtonListener {
        void deleteButtonClick(int position);
    }
}