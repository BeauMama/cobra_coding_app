package com.example.application.database;

import android.content.Context;
import androidx.room.Room;

public class DataInitializeDatabase {

    private static DataDao dataDao;

    private DataInitializeDatabase() {}

    public static DataDao getInstance(Context context) {
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