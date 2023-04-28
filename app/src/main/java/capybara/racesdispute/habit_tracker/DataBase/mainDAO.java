package capybara.racesdispute.habit_tracker.DataBase;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import capybara.racesdispute.habit_tracker.trackers_settings.Models;

@Dao
public interface mainDAO {

    @Insert (onConflict = REPLACE)
    void insert (Models models);

    @Query ("SELECT * FROM habit ORDER BY id DESC")
    List<Models> getAll();

    @Query ("UPDATE habit SET habitName = :name, habitPeriod = :period,habitDescription = :description, habitPeriodInt = :periodInt WHERE ID = :ID")
    void update(int ID, String name, String period, String description, String periodInt);

    @Delete ()
    void delete (Models models);

    @Query("SELECT habitName FROM habit WHERE ID=:id")
    String getFromId(int id);

    @Query("SELECT habitPeriodInt FROM habit WHERE ID=:id")
    String getPeriodFromId(int id);

    @Query("UPDATE habit SET habitPeriodInt =:periodInt WHERE ID=:id")
    void updatePeriodFromId(String periodInt, int id);

}
