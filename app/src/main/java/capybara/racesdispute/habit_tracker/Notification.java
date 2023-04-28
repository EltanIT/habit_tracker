package capybara.racesdispute.habit_tracker;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.room.Database;

import java.util.ArrayList;
import java.util.List;

import capybara.racesdispute.habit_tracker.DataBase.RoomDB;
import capybara.racesdispute.habit_tracker.trackers_settings.Models;

public class Notification extends AppCompatActivity {

    private static final String CHANNEL_NAME = "channel";
    private static final String CHANNEL_ID = "channel_id";
    private int NOTIFY_ID;
    private String title;

    RoomDB database;
    Models models;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CreateNotificationChannel();

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            NOTIFY_ID = extras.getInt("id");
            database = RoomDB.getInstance(this);
            title = database.mainDAO().getFromId(NOTIFY_ID);
        }

        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, MainActivity.class);
        bundle.putInt("id", NOTIFY_ID);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 103, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent pendingIntentBad = PendingIntent.getActivity(this, 104, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_logo)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText("Как поживает твоя привычка?")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0,"Отлично", pendingIntent)
                .addAction(0,"Не очень", pendingIntentBad);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, builder.build());

        Intent intent2 = new Intent(this,MainActivity.class);
        finish();

    }

    private void CreateNotificationChannel(){
        CharSequence name = CHANNEL_NAME;
        String description = "Как поживает твоя привычка?";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        notificationManager.createNotificationChannel(channel);

    }

}
