package com.example.application.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.BR;
import com.example.application.R;
import com.example.application.databinding.IngredientlistRowBinding;
import com.example.application.model.Ingredient;
import com.example.application.viewmodel.RecipeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {

    private int layoutId;
    private RecipeViewModel viewModel;
    private DeleteButtonListener deleteButtonListener;
    private Activity activity;

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
        Ingredient ingredient = viewModel.getRecipeWithIngredients().ingredients.get(position);
        viewHolder.bind(ingredient);
        //viewHolder.getCalcConvQty().setText(Float.toString(ingredient.getQuantity() * 2));
        viewHolder.checkBox.setClickable(true);
        viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = viewHolder.getAdapterPosition();
                notifyItemChanged(position);

                if (selectedPosition == position){
                    viewHolder.checkBox.setClickable(false);

                }else{
                    viewHolder.checkBox.setClickable(true);
                }
            }
        });
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

        public void bind(Ingredient ingredient) {
            ingredientlistRowBinding.setVariable(BR.ingredient, ingredient);
            ingredientlistRowBinding.executePendingBindings();

        }
    }

    public interface DeleteButtonListener {
        void deleteButtonClick(int position);
    }
}