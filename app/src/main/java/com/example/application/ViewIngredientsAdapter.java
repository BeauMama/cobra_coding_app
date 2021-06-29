package com.example.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewIngredientsAdapter extends RecyclerView.Adapter<ViewIngredientsAdapter.ViewHolder> {

    private RecipeWithIngredients recipeWithIngredients;
    private DeleteButtonListener deleteButtonListener;

    public ViewIngredientsAdapter(RecipeWithIngredients recipeWithIngredients, DeleteButtonListener deleteButtonListener) {
        this.recipeWithIngredients = recipeWithIngredients;
        this.deleteButtonListener = deleteButtonListener;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.ingredientlist_row, parent, false);
        return new ViewHolder(view, deleteButtonListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewIngredientsAdapter.ViewHolder viewHolder, int position) {
        Ingredient ingredient = this.recipeWithIngredients.ingredients.get(position);
        viewHolder.getTestView().setText(ingredient.getName());
    }

    @Override
    public int getItemCount() {
        return recipeWithIngredients.ingredients.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView testView;
        private Button button;
        private DeleteButtonListener deleteButtonListener;

        public ViewHolder(@NonNull @NotNull View itemView, DeleteButtonListener deleteButtonListener) {
            super(itemView);
            testView = itemView.findViewById(R.id.ingredentName2);
            button = itemView.findViewById(R.id.delete2);

            this.deleteButtonListener = deleteButtonListener;
            button.setOnClickListener(this);
        }
        public TextView getTestView() {
            return testView;
        }

        @Override
        public void onClick(View view) {
            deleteButtonListener.deleteButtonClick(getAdapterPosition());
        }
    }

    public interface DeleteButtonListener {
        void deleteButtonClick(int position);
    }
}