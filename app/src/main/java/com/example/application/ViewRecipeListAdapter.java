package com.example.application;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewRecipeListAdapter extends RecyclerView.Adapter<ViewRecipeListAdapter.ViewHolder> {

    private List<Recipe> recipes;
    private SelectItemListener selectItemListener;

    public ViewRecipeListAdapter(List<Recipe> recipes, SelectItemListener selectItemListener) {
        this.recipes = recipes;
        this.selectItemListener = selectItemListener;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recipelist_row, parent, false);
        return new ViewHolder(view, selectItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewRecipeListAdapter.ViewHolder viewHolder, int position) {
        Recipe recipe = this.recipes.get(position);
        viewHolder.getRecipeNameTextView().setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        return this.recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recipeNameTextView;
        private LinearLayout linearLayout;
        private SelectItemListener selectItemListener;

        public ViewHolder(@NonNull @NotNull View itemView, SelectItemListener selectItemListener) {
            super(itemView);
            recipeNameTextView = itemView.findViewById(R.id.recipeTextView);
            linearLayout = itemView.findViewById(R.id.linearLayout2);

            this.selectItemListener = selectItemListener;
            linearLayout.setOnClickListener(this);

        }
        public TextView getRecipeNameTextView() {
            return recipeNameTextView;
        }

        @Override
        public void onClick(View view) {
            selectItemListener.selectItemClick(getAdapterPosition());
        }
    }

    public interface SelectItemListener {
        void selectItemClick(int Position);
    }
}