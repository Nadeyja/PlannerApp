package com.example.androidstudioprojekt_259323;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Calendar;

public class Notifications extends BroadcastReceiver {

    public static String ID = "id";
    public static String TITLE = "title";
    public static String CONTENT_TEXT = "content";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent openSavedActivities = new Intent(context, SavedActivities.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int id = intent.getIntExtra(ID, 0);
        String title = intent.getStringExtra(TITLE);
        String content = intent.getStringExtra(CONTENT_TEXT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, openSavedActivities, PendingIntent.FLAG_MUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Activity with id: " + id)
                .setSmallIcon(R.drawable.baseline_access_alarm_24)
                .setContentTitle(title).setContentText(content).setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL).setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
        Log.i("Id", "Notification id: " + id);
    }

}
