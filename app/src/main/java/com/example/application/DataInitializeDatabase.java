package com.example.application;

import android.app.Activity;
import android.content.Context;

import androidx.room.Room;

public class DataInitializeDatabase {

    private static DataDao dataDao;

    private DataInitializeDatabase() {}

    public static final DataDao getInstance(Context context) {
        //Activity context = null;
        DataAppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
            DataAppDatabase.class, "database")
            .fallbackToDestructiveMigration()
            .build();
        if(dataDao == null) {
            dataDao = db.dataDao();
        }
        return dataDao;
    }
}
