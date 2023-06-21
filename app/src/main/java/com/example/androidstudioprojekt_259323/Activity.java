package com.example.androidstudioprojekt_259323;

import androidx.annotation.NonNull;

public class Activity {

        public String name;
        public String type;
        public int id;
        public String datetime;
        public int notification;
        public String beforedatetime;
        public int beforenotificaton;

        Activity(String name, String type, String datetime, int notification, String beforedatetime, int beforenotificaton, int id){
            this.name = name;
            this.type = type;
            this.id = id;
            this.datetime = datetime;
            this.notification = notification;
            this.beforedatetime = beforedatetime;
            this.beforenotificaton = beforenotificaton;
        }
        Activity(String name, String type, String datetime, int notification, String beforedatetime, int beforenotificaton){
            this.name = name;
            this.type = type;
            this.datetime = datetime;
            this.notification = notification;
            this.beforedatetime = beforedatetime;
            this.beforenotificaton = beforenotificaton;
        }

    @NonNull
    @Override
    public String toString() {
            String str = name + ", " + type + ", " + datetime + " "+id;
        return str;
    }
}
