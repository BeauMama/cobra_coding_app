package com.example.application.database;

import android.content.Context;
import androidx.room.Room;

/**
 * Sets up the Room database as a singleton so it can be used throughout the app.
 */
public class DataInitializeDatabase {

    private static DataDao dataDao;

    /**
     * Constructor set as private to prevent instantiating the class.
     */
    private DataInitializeDatabase() {}

    /**
     * Creates a single instance of the database.
     *
     * @param context The context for the application.
     * @return A data access object.
     */
    public static DataDao getInstance(Context context) {
        DataAppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
            DataAppDatabase.class, "database")
            .fallbackToDestructiveMigration()
            .build();
        if (dataDao == null) {
            dataDao = db.dataDao();
        }
        return dataDao;
    }
}