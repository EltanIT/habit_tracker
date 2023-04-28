package capybara.racesdispute.habit_tracker.trackers_settings;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "habit")
public class Models implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "habitName")
    public String name = "";

    @ColumnInfo(name = "habitPeriod")
    public String period = "";
    @ColumnInfo (name = "habitPeriodInt")
    public String periodInt = "";

    @ColumnInfo(name = "habitDate")
    public String date = "";

    @ColumnInfo (name = "pinned")
    public boolean pinned = false;

    @ColumnInfo (name = "habitDescription")
    public String description = "";

    public String getPeriodInt() {
        return periodInt;
    }

    public void setPeriodInt(String periodInt) {
        this.periodInt = periodInt;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPinned() {
        return pinned;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }
}
