package com.example.application.viewmodel;

import androidx.lifecycle.ViewModel;
import com.example.application.adapter.ViewRecipeListAdapter;
import com.example.application.model.Recipe;
import java.util.List;

/**
 * The view model to be used for the LoadRecipeActivity.
 */
public class LoadRecipeViewModel extends ViewModel {

    private List<Recipe> recipes;
    private ViewRecipeListAdapter adapter;

    /**
     * Used to initialize the view model.
     *
     * @param selectItemListener The listener to be used by the UI.
     */
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
