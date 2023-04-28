package capybara.racesdispute.habit_tracker.DataBase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import capybara.racesdispute.habit_tracker.trackers_settings.Models;

@Database (entities = Models.class, version = 5,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;
    private static String DATABASE_NAME = "TrackerApp";

    public synchronized static RoomDB getInstance(Context context){

        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
        }
        return database;

    }
    public abstract mainDAO mainDAO();

}
