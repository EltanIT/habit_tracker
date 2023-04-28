package capybara.racesdispute.habit_tracker;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import org.json.JSONObject;

import java.util.Calendar;
import capybara.racesdispute.habit_tracker.DataBase.RoomDB;
import capybara.racesdispute.habit_tracker.trackers_settings.Models;
public class tracker extends AppCompatActivity {

    RoomDB database;
    Models models;

    ImageView back,imageView_save;
    TextView title, textView_period, textView_periodDays;
    EditText editText_description;
    ListView listView;

    Models textModels;

    String textNew;
    String textOld;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        getSupportActionBar().hide();

        back = findViewById(R.id.imageView_back);
        imageView_save = findViewById(R.id.imageView_save);
        title = findViewById(R.id.textView_title);
        textView_period = findViewById(R.id.textView_period);
        textView_periodDays = findViewById(R.id.textView_periodDays);
        editText_description = findViewById(R.id.editText_discription);
        listView = findViewById(R.id.listView_check);

        models = new Models();

        try {
            models = (Models) getIntent().getSerializableExtra("old_tracker");
            textModels = models;
            title.setText(models.getName());
            textView_period.setText(models.getPeriod());
            editText_description.setText(models.getDescription(),TextView.BufferType.EDITABLE);
            textView_periodDays.setText(models.getPeriodInt());
            textOld = editText_description.getText().toString();
            textNew = textOld;

        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("period",models.getPeriod());

        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textNew = editText_description.getText().toString();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (textOld != textNew){
                    models.setDescription(textNew);
                    Intent intent = new Intent(tracker.this,MainActivity.class);
                    intent.putExtra("models", models);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }
                else{
                    models.setDescription(textOld);
                    Intent intent = new Intent(tracker.this,MainActivity.class);
                    intent.putExtra("models", models);
                    setResult(Activity.RESULT_OK,intent);
                    finish();
                }


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (resultCode == Activity.RESULT_OK){
                Models new_models = (Models) data.getSerializableExtra("models");
                database.mainDAO().insert(new_models);
            }
            if (requestCode == 102){
                if (resultCode == Activity.RESULT_OK){
                    Models new_models = (Models) data.getSerializableExtra("models");
                    database.mainDAO().update(new_models.getID(),new_models.getName(), new_models.getPeriod(), new_models.description, new_models.getPeriodInt());
                    }
                }
            }

    public void onCreateNotification(){
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(0)
                .setMinute(0)
                .setTitleText("Выберите время на сегодня")
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
            calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(),getAlarmInfoPendingIntent());

            alarmManager.setAlarmClock(alarmClockInfo,getAlarmActionPendingIntent());
        });
        materialTimePicker.show(getSupportFragmentManager(),"tag_bum");
    }

    private PendingIntent getAlarmInfoPendingIntent(){
        Intent alarmIntent = new Intent(this,MainActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this,0,alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmActionPendingIntent(){
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, Notification.class);
        bundle.putString("title",models.getName());
        bundle.putInt("id",models.getID());
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void SelectTimer(View view) {
        onCreateNotification();
    }
}

