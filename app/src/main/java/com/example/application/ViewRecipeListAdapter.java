package com.example.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewRecipeListAdapter extends RecyclerView.Adapter<ViewRecipeListAdapter.ViewHolder> {

    private List<Recipe> recipes;

    public ViewRecipeListAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipelist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewRecipeListAdapter.ViewHolder viewHolder, int position) {
        Recipe recipe = this.recipes.get(position);
        viewHolder.getTestView().setText(recipe.name);
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView testView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            testView = itemView.findViewById(R.id.recipeRow);
        }
        public TextView getTestView() {
            return testView;
        }
    }
}