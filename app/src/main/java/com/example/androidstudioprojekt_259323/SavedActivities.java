package com.example.androidstudioprojekt_259323;

import androidx.appcompat.app.AppCompatActivity;

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
    DatabaseOperations db;
    ArrayList<String> activities;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_activities);
        db = new DatabaseOperations(this);
        activities = new ArrayList<>();
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
                int id = Integer.parseInt((activities.get(i)).substring((activities.get(i)).lastIndexOf(" ")+1));
                db.deleteActivity(id);
                reloadListView();

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
                Log.e("Activity",act.toString());
            }while (cursor.moveToNext());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,android.R.layout.simple_list_item_1,activities);
        list.setAdapter(adapter);
    }

}