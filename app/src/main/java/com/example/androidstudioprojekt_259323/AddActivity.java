package com.example.androidstudioprojekt_259323;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Switch;

import java.sql.SQLDataException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {

    DatabaseOperations db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = new DatabaseOperations(this);
        try {
            db.openDatabase();
        } catch (SQLDataException e) {
            throw new RuntimeException(e);
        }


    }


    public void onClick(View v){
        takeData();
    }
    public void takeData(){
        EditText nameText = findViewById(R.id.editTextTextPersonName4);
        EditText typeText = findViewById(R.id.editTextTextPersonName);
        EditText timehoursText = findViewById(R.id.editTextTime2);
        EditText timedaysText = findViewById(R.id.editTextDate);
        Switch notificationsSwitch = findViewById(R.id.switch2);
        Switch notificationsbeforeSwitch = findViewById(R.id.switch1);
        EditText timebeforeText = findViewById(R.id.editTextNumber);
        String name = nameText.getText().toString();
        String type = typeText.getText().toString();
        String datetime = timedaysText.getText().toString() + " " + timehoursText.getText().toString();
        String timebefore = timebeforeText.getText().toString();

        int notifications;
        int notificationsbefore;
        if (notificationsSwitch.isChecked()){
            notifications = 1;
        }else{
            notifications = 0;
        }
        if (notificationsbeforeSwitch.isChecked()){
            notificationsbefore = 1;
        }else{
            notificationsbefore = 0;
        }
        Log.i("name",name);
        Log.i("type",type);
        Log.i("datetime",datetime);
        Log.i("timebefore",timebefore);
        Log.i("noti",String.valueOf(notifications));
        Log.i("notibef",String.valueOf(notificationsbefore));
        db.insertNewActivity(name,type,datetime,notifications,timebefore,notificationsbefore);


    }

}