package capybara.racesdispute.habit_tracker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import capybara.racesdispute.habit_tracker.Adapter.TrackersListAdapter;
import capybara.racesdispute.habit_tracker.DataBase.RoomDB;
import capybara.racesdispute.habit_tracker.trackers_settings.Models;

public class MainActivity extends AppCompatActivity {

    final String[] period = {"1 неделя","2 недели", "1 месяц"};
    RecyclerView recyclerView;
    TrackersListAdapter trackersListAdapter;
    RoomDB database;
    List<Models> trackers = new ArrayList<>();
    Models models;

    String days;
    int daysComplete;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Мои привычки");
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#282030")));


        recyclerView = findViewById(R.id.recycler_home);
        database = RoomDB.getInstance(this);
        trackers = database.mainDAO().getAll();

        updateRecycler(trackers);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("requestCode",String.valueOf(requestCode));
        if (resultCode == RESULT_OK){
            Models new_models1 = (Models) data.getSerializableExtra("models");
            database.mainDAO().update(new_models1.getID(),new_models1.getName(), new_models1.getPeriod(), new_models1.getDescription(), new_models1.getPeriodInt());
            trackers.clear();
            trackers.addAll(database.mainDAO().getAll());
            trackersListAdapter.notifyDataSetChanged();
        }
        if (requestCode == 201){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                int NOTIFY_ID = extras.getInt("id");
                String days = database.mainDAO().getPeriodFromId(NOTIFY_ID);
                int day = Integer.parseInt(days);
                day++;
                days = String.valueOf(day);
                database.mainDAO().updatePeriodFromId(days, NOTIFY_ID);
                trackers.clear();
                trackers.addAll(database.mainDAO().getAll());
                trackersListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 200){
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                int NOTIFY_ID = extras.getInt("id");
                id = NOTIFY_ID;
                String days = database.mainDAO().getPeriodFromId(NOTIFY_ID);
                int day = Integer.parseInt(days);
                days = String.valueOf(day);
                database.mainDAO().updatePeriodFromId(days, NOTIFY_ID);
                trackers.clear();
                trackers.addAll(database.mainDAO().getAll());
                trackersListAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == 0){
        }
    }

    private void updateRecycler(List<Models> trackers) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        trackersListAdapter = new TrackersListAdapter(MainActivity.this,trackers, trackersClickListener);
        recyclerView.setAdapter(trackersListAdapter);
    }
    private final TrackersClickListener trackersClickListener = new TrackersClickListener() {
        @Override
        public void onClick(Models trackers) {
            Intent intent = new Intent(MainActivity.this,tracker.class);
            intent.putExtra("old_tracker", trackers);
            startActivityForResult(intent,101);
        }

        @Override
        public void onLongClick(Models trackers1, CardView cardView) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Удалить привычку?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            database.mainDAO().delete(trackers1);
                            trackers.remove(trackers1);
                            trackersListAdapter.notifyDataSetChanged();
                            Toast.makeText(MainActivity.this, "Удалено", Toast.LENGTH_SHORT).show();
                            dialogInterface.cancel();
                        }
                    })
                    .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
            builder.show();
        }
    };

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        final EditText editText = new EditText(MainActivity.this);
        editText.setHint("Имя привычки");

        if(item.getItemId() == R.id.addIcon){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Введите имя цели и срок выполнения")

                    .setSingleChoiceItems(period, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // что делает при выборе срока
                            if (i == 0){
                                days = "/7";
                                daysComplete = 0;
                            }
                            else if (i == 1){
                                days = "/14";
                                daysComplete = 0;
                            }
                            else if (i == 2){
                                days = "/30";
                                daysComplete = 0;
                            }
                            else{
                                days = "";
                                daysComplete = 0;
                            }

                            i =-1;

                        }
                    })
                    .setView(editText)
                    .setPositiveButton("Создать цель", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // создать трекер
                            if (editText.getText().toString().isEmpty() | days == ""){
                                Toast.makeText(MainActivity.this,"Не все парамметры заполнены", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else {
                                dialog.cancel();

                                String title = editText.getText().toString();

                                SimpleDateFormat formater = new SimpleDateFormat("EEE, MMM d, ''yy");
                                Date date = new Date();
                                models = new Models();
                                models.setName(title);
                                models.setPeriod(days);
                                models.setPeriodInt(String.valueOf(daysComplete));
                                days = "";
                                models.setDate(formater.format(date));
                                models.setDescription("Описание");

                                database.mainDAO().insert(models);
                                trackers.clear();
                                trackers.addAll(database.mainDAO().getAll());
                                trackersListAdapter.notifyDataSetChanged();



                            }
                        }
                    })
                    .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // закрыть окно
                            dialog.cancel();
                        }
                    });
            builder.show();
        }
        return true;
    }


}