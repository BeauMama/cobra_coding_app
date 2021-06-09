package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    public DataRecipeDao dataRecipeDao;
    List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database").build();
        dataRecipeDao = db.dataRecipeDao();
    }

    public void addDataTest(View view) {


    }

    public void printDataTest(View view) {

        GetRecipeData recipeData = new GetRecipeData(this);
        Thread thread = new Thread(recipeData, "Get recipe data");
        thread.start();;


    }


}