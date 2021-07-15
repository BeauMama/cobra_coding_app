package com.example.application.viewmodel;

import android.app.Activity;
import android.widget.Spinner;

import androidx.lifecycle.ViewModel;

import com.example.application.R;
import com.example.application.adapter.ViewIngredientsAdapter;
import com.example.application.model.RecipeWithIngredients;

import java.util.List;

public class RecipeViewModel extends ViewModel {

    private RecipeWithIngredients recipeWithIngredients;
    private List<String> ingredientNames;
    private ViewIngredientsAdapter adapter;
    private Activity activity;

    public void init(Activity activity) {
        adapter = new ViewIngredientsAdapter(R.layout.recipelist_row, this, activity);
        this.activity = activity;
    }

    public ViewIngredientsAdapter getAdapter() { return adapter; }

    public RecipeWithIngredients getRecipeWithIngredients() {
        return recipeWithIngredients;
    }

    public void setRecipeWithIngredients(RecipeWithIngredients recipeWithIngredients) {
        this.recipeWithIngredients = recipeWithIngredients;
    }

    public List<String> getIngredientNames() {
        return ingredientNames;
    }

    public void setIngredientNames(List<String> ingredientNames) {
        this.ingredientNames = ingredientNames;
    }

    public static Boolean setSpinnerToValue(Spinner spinner, String value) {
        for (int itemPosition = 0; itemPosition < spinner.getAdapter().getCount(); itemPosition++) {
            String itemValue = (String) spinner.getAdapter().getItem(itemPosition);
            if (itemValue.toLowerCase().equals(value.toLowerCase())) {
                spinner.setSelection(itemPosition, false);
                return true;
            }
        }
        return false;
    }
}
