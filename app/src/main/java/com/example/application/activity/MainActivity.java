package com.example.application.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.application.R;

/**
 * Main splash screen. Holds two buttons. One to go to the LoadRecipeActivity
 * and one to go to the RecipeActivity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Setup when the activity is created.
     *
     * @param savedInstanceState Used to save the state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Starts the activity to select a recipe to load.
     *
     * @param view The view that was clicked.
     */
    public void onClickMyRecipes(View view) {
        Intent intent = new Intent(this, LoadRecipeActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the activity to create a new recipe.
     *
     * @param view The view that was clicked.
     */
    public void onClickNewRecipe(View view) {
        Intent intent = new Intent(this, RecipeActivity.class);
        startActivity(intent);
    }
}