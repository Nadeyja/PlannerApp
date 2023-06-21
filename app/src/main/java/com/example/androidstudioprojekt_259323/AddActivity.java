package com.example.androidstudioprojekt_259323;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.sql.SQLDataException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    DatabaseOperations db;
    EditText nameText;
    EditText typeText;
    EditText timehoursText;
    EditText timedaysText;
    Switch notificationsSwitch;
    Switch notificationsbeforeSwitch;
    EditText timebeforeText;
    Calendar calendar;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        nameText = findViewById(R.id.editTextTextPersonName4);
        typeText = findViewById(R.id.editTextTextPersonName);
        timehoursText = findViewById(R.id.editTextTime2);
        timedaysText = findViewById(R.id.editTextDate);
        notificationsSwitch = findViewById(R.id.switch2);
        timebeforeText = findViewById(R.id.editTextNumber);
        notificationsbeforeSwitch = findViewById(R.id.switch1);


        db = new DatabaseOperations(this);
        try {
            db.openDatabase();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }
        timehoursText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR);
            int minute = c.get(Calendar.MINUTE);


            TimePickerDialog timePickerDialog = new TimePickerDialog(

                    AddActivity.this,
                    (view, hourPicked, minutePicked) -> {
                        timehoursText.setText(hourPicked + ":" + minutePicked);
                    },
                    hour, minute,true);
            timePickerDialog.show();
        });
        timedaysText.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(

                    AddActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        timedaysText.setText(year1 + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                    },
                    year, month, day);
            datePickerDialog.show();

        });


    }


    public void onClick(View v){
        takeData();
    }
    public void takeData(){
        String name = nameText.getText().toString();
        String type = typeText.getText().toString();
        String datetime = timedaysText.getText().toString() + " " + timehoursText.getText().toString();
        int first = datetime.indexOf("/");
        int year = Integer.parseInt(datetime.substring(0,first));
        int second = datetime.indexOf("/",first+1);
        int month = Integer.parseInt(datetime.substring(first+1,second))-1;
        int day = Integer.parseInt(datetime.substring(second+1,datetime.indexOf(" ")));
        int hour = Integer.parseInt(datetime.substring(datetime.indexOf(" ")+1,datetime.indexOf(":")));
        int minute = Integer.parseInt(datetime.substring(datetime.indexOf(":")+1));
        Log.i("Data",year + " " + month + " " +day + " " +hour + " " +minute);
        String timebefore = timebeforeText.getText().toString();
        int notifications;
        int notificationsbefore;
        calendar = Calendar.getInstance();
        calendar.set(year,month,day,hour,minute,0);
        long notificationTime = calendar.getTimeInMillis();
        if (notificationsSwitch.isChecked()){
            notifications = 1;

        }else{
            notifications = 0;
        }
        if (notificationsbeforeSwitch.isChecked()){
            notificationsbefore = 1;
        }else{
            notificationsbefore = 0;
            timebefore = "";
        }
        Log.i("name",name);
        Log.i("type",type);
        Log.i("datetime",datetime);
        Log.i("timebefore",timebefore);
        Log.i("noti",String.valueOf(notifications));
        Log.i("notibef",String.valueOf(notificationsbefore));
        Log.i("Data10s",String.valueOf(System.currentTimeMillis()+10000));
        Log.i("DataCalendar",String.valueOf(calendar.getTimeInMillis()));
        db.insertNewActivity(name,type,datetime,notifications,timebefore,notificationsbefore);
        Cursor cursor = db.takeActivitiesFromDatabase();
        if (notifications==1){
            int id = 0;
            if(cursor.moveToLast()){
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_ID)));
                Log.i("Id","Notification id: " + id);
            }
            createNotificationChannel(id);
            addAlarm(id,notificationTime,name, name+ " z kategorii " + type);
        }
        if (notificationsbefore==1){
            notificationTime = calendar.getTimeInMillis()-((Long.parseLong(timebefore))*60000);
            int id = 0;
            if(cursor.moveToLast()){
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(DatabaseSQLite.ACTIVITY_ID)));
                id = -id;
                Log.i("Id","Notification id: " + id);
            }
            createNotificationChannel(id);
            addAlarm(id,notificationTime,name, name+ " z kategorii " + type + " za " + timebefore + " minut!");
        }
        Toast toast = Toast.makeText(this, "Dodano nową aktywność", Toast.LENGTH_SHORT);
        toast.show();



    }
    public void addAlarm(int intentID,long alarmTime,String title,String content){
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(AddActivity.this, Notifications.class);
        intent.putExtra(Notifications.ID, intentID);
        intent.putExtra(Notifications.TITLE,title);
        intent.putExtra(Notifications.CONTENT_TEXT,content);
        pendingIntent = PendingIntent.getBroadcast(AddActivity.this, intentID, intent, PendingIntent.FLAG_MUTABLE);
        Log.i("DataCalendar",String.valueOf(calendar.getTimeInMillis()));
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime,
                pendingIntent);
        Log.i("Notify!","Notification id: " + intentID);
    }
    private void createNotificationChannel(int channelId) {

        CharSequence name = "alarm";
        String desc = "Channel for Alarm Manager";
        int imp = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel("Activity with id: "+channelId, name, imp);
        channel.setDescription(desc);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}