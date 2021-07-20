package com.example.application.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.application.adapter.ViewRecipeListAdapter;
import com.example.application.model.Recipe;
import java.util.List;

public class LoadRecipeViewModel extends ViewModel {

    private List<Recipe> recipes;
    private ViewRecipeListAdapter adapter;

    public void init(ViewRecipeListAdapter.SelectItemListener selectItemListener) {
        adapter = new ViewRecipeListAdapter(this, selectItemListener);
    }

    public ViewRecipeListAdapter getAdapter() { return adapter; }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
