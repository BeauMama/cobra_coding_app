package com.example.application.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.BR;
import com.example.application.viewmodel.LoadRecipeViewModel;
import com.example.application.R;
import com.example.application.databinding.RecipelistRowBinding;
import com.example.application.model.Recipe;

import org.jetbrains.annotations.NotNull;

public class ViewRecipeListAdapter extends RecyclerView.Adapter<ViewRecipeListAdapter.ViewHolder> {

    private int layoutId;
    private LoadRecipeViewModel viewModel;
    private SelectItemListener selectItemListener;

    public ViewRecipeListAdapter(@LayoutRes int layoutId, LoadRecipeViewModel viewModel, SelectItemListener selectItemListener) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
        this.selectItemListener = selectItemListener;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        RecipelistRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.recipelist_row, parent, false);

        return new ViewHolder(binding, selectItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewRecipeListAdapter.ViewHolder viewHolder, int position) {
        Recipe recipe = viewModel.getRecipes().get(position);
        viewHolder.bind(recipe);
    }

    @Override
    public int getItemCount() {
        return viewModel.getRecipes().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView recipeNameTextView;
        private LinearLayout linearLayout;
        private SelectItemListener selectItemListener;
        public RecipelistRowBinding recipelistRowBinding;

        public ViewHolder(@NonNull @NotNull RecipelistRowBinding recipelistRowBinding, SelectItemListener selectItemListener) {
            super(recipelistRowBinding.getRoot());
            this.recipelistRowBinding = recipelistRowBinding;

            linearLayout = itemView.findViewById(R.id.linearLayoutRecipeList);

            this.selectItemListener = selectItemListener;
            linearLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            selectItemListener.selectItemClick(getAdapterPosition());
        }

        public void bind(Recipe recipe) {
            recipelistRowBinding.setVariable(BR.recipe, recipe);
            recipelistRowBinding.executePendingBindings();
        }
    }

    public interface SelectItemListener {
        void selectItemClick(int Position);
    }
}