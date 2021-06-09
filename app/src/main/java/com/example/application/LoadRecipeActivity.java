package com.example.application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.os.Bundle;
import java.util.List;

public class LoadRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_recipe);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "").build();
        DataRecipeDao dataRecipeDao = db.dataRecipeDao();
        List<Recipe> recipes = dataRecipeDao.getAll();


    }

}