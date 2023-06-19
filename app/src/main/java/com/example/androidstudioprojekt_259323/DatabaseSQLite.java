package com.example.androidstudioprojekt_259323;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;

public class DatabaseSQLite extends SQLiteOpenHelper {
    private static DatabaseSQLite databaseSQLite;
    private static final String DATABASE_NAME = "Planner";
    private static final int VERSION = 1;

    static final String DATABASE_TABLE_ACTIVITIES_TYPE = "TYPES";
    static final String TYPE_ID = "TYPE_ID";
    static final String DATABASE_TABLE_ACTIVITIES = "ACTIVITIES";
    static final String ACTIVITY_NAME = "ACTIVITY_NAME";
    static final String ACTIVITY_ID = "ACTIVITY_ID";
    static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    static final String ACTIVITY_NOTIFICATION = "ACTIVITY_NOTIFICATION";
    static final String ACTIVITY_TIME_BEFORE = "ACTIVITY_TIME_BEFORE";
    static final String ACTIVITY_NOTIFICATION_BEFORE = "ACTIVITY_NOTIFICATION_BEFORE";

    static final String ACTIVITY_DATETIME = "ACTIVITY_TIME";

    static final String CREATE_TABLE_ACTIVITY = "CREATE TABLE " +DATABASE_TABLE_ACTIVITIES + " ( "
            + ACTIVITY_NAME + " TEXT NOT NULL, "
            + ACTIVITY_TYPE + " TEXT NOT NULL, "
            + ACTIVITY_DATETIME + " TEXT NOT NULL, "
            + ACTIVITY_NOTIFICATION + " INTEGER NOT NULL, "
            + ACTIVITY_TIME_BEFORE + " TEXT, "
            + ACTIVITY_NOTIFICATION_BEFORE +  "INTEGER NOT NULL, "
            + ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)";

    static final String CREATE_TABLE_TYPES = "CREATE TABLE " + DATABASE_TABLE_ACTIVITIES_TYPE + " ( "
            + ACTIVITY_TYPE + " TEXT NOT NULL, "
            + TYPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT)";
    public DatabaseSQLite(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ACTIVITY);
        sqLiteDatabase.execSQL(CREATE_TABLE_TYPES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
