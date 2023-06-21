package com.example.androidstudioprojekt_259323;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class SavedActivities extends AppCompatActivity {

    ListView list;
    AlertDialog.Builder alertDialog;
    DatabaseOperations db;
    ArrayList<String> activities;
    ArrayList<Activity> activitiesAct;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_activities);
        db = new DatabaseOperations(this);
        activities = new ArrayList<>();
        activitiesAct = new ArrayList<>();
        alertDialog = new AlertDialog.Builder(this);
        //db.removeDatabase(this);
        list = findViewById(R.id.listview);
        try {
            db.openDatabase();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
        Log.e("Activity",DatabaseSQLite.CREATE_TABLE_ACTIVITY);
        reloadListView();
        //db.closeDatabase();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int notificationbefore = activitiesAct.get(i).beforenotificaton;
                int notification = activitiesAct.get(i).notification;
                int activityID = activitiesAct.get(i).id;
                alertDialog.setTitle("Usuń aktywność").setMessage("Czy na pewno chcesz usunąć aktywnośc z numerem id: "+activityID)
                                .setCancelable(true).setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteActivity(activityID);
                                if(notification==1){
                                    removeAlarm(activityID);
                                }
                                if(notificationbefore==1){
                                    removeAlarm(-activityID);
                                }

                                reloadListView();
                            }
                        }).setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }
    private void reloadListView(){
        activities.clear();
        Cursor cursor = db.takeActivitiesFromDatabase();
        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_NAME));
                String type = cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_TYPE));
                String datetime = cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_DATETIME));
                String timebefore = cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_TIME_BEFORE));
                int notification = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_NOTIFICATION)));
                int notificationbefore = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_NOTIFICATION_BEF)));
                int id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_ID)));
                Activity act = new Activity(name,type,datetime,notification,timebefore,notificationbefore,id);
                activities.add(act.toString());
                activitiesAct.add(act);
                Log.e("Activity",act.toString());
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,activities);
        list.setAdapter(adapter);
    }
    public void removeAlarm(int id){
        Intent intent = new Intent(SavedActivities.this, Notifications.class);
        pendingIntent = PendingIntent.getBroadcast(SavedActivities.this, id, intent, PendingIntent.FLAG_MUTABLE);
        if (alarmManager == null){
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannel("Activity with id: "+id);
    }

}