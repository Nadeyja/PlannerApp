package com.example.androidstudioprojekt_259323;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLDataException;

public class DatabaseOperations {
    private DatabaseSQLite db;
    private SQLiteDatabase database;
    private Context cont;

    public DatabaseOperations(Context cont) {
        this.cont = cont;
    }
    public DatabaseOperations openDatabase() throws SQLDataException {
        db = new DatabaseSQLite(cont);
        database = db.getWritableDatabase();
        return this;
    }
    public void closeDatabase(){
        db.close();
    }
    public void insertNewActivity(String name, String type, String datetime,int notification,
                                  String datetimebefore,int notificationbefore){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSQLite.ACTIVITY_NAME,name);
        contentValues.put(DatabaseSQLite.ACTIVITY_TYPE,type);
        contentValues.put(DatabaseSQLite.ACTIVITY_DATETIME,datetime);
        contentValues.put(DatabaseSQLite.ACTIVITY_NOTIFICATION,notification);
        contentValues.put(DatabaseSQLite.ACTIVITY_TIME_BEFORE,datetimebefore);
        contentValues.put(DatabaseSQLite.ACTIVITY_NOTIFICATION_BEFORE,notificationbefore);
        database.insert(DatabaseSQLite.DATABASE_TABLE_ACTIVITIES, null, contentValues);
    }
    public void insertNewType(String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSQLite.ACTIVITY_TYPE, type);
        database.insert(DatabaseSQLite.CREATE_TABLE_TYPES,null,contentValues);
    }
    public void deleteActivity (int id){
        database.delete(DatabaseSQLite.DATABASE_TABLE_ACTIVITIES, DatabaseSQLite.ACTIVITY_ID + " = " + id,null);
    }
    public void deleteType (int id){
        database.delete(DatabaseSQLite.DATABASE_TABLE_ACTIVITIES_TYPE, DatabaseSQLite.TYPE_ID + " = " + id,null);
    }
    public Cursor takeActivitiesFromDatabase(){
        String [] data = new String[] {DatabaseSQLite.ACTIVITY_TYPE, DatabaseSQLite.TYPE_ID,};
        Cursor cursor = database.query(DatabaseSQLite.DATABASE_TABLE_ACTIVITIES_TYPE,data,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor takeTypesFromDatabase(){
        String [] data = new String[] {DatabaseSQLite.ACTIVITY_NAME,DatabaseSQLite.ACTIVITY_TYPE,
                DatabaseSQLite.ACTIVITY_DATETIME,DatabaseSQLite.ACTIVITY_NOTIFICATION,
                DatabaseSQLite.ACTIVITY_TIME_BEFORE,DatabaseSQLite.ACTIVITY_NOTIFICATION_BEFORE,
                DatabaseSQLite.ACTIVITY_ID,};
        Cursor cursor = database.query(DatabaseSQLite.DATABASE_TABLE_ACTIVITIES,data,null,null,null,null,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
}
