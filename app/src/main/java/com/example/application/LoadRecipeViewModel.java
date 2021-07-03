package com.example.application;

import androidx.lifecycle.ViewModel;
import java.util.List;

public class LoadRecipeViewModel extends ViewModel {

    private List<Recipe> recipes;
    private ViewRecipeListAdapter adapter;

    public void init(ViewRecipeListAdapter.SelectItemListener selectItemListener) {
        adapter = new ViewRecipeListAdapter(R.layout.recipelist_row, this, selectItemListener);
    }

    public ViewRecipeListAdapter getAdapter() { return adapter; }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
