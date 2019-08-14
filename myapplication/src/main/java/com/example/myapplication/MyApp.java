package com.example.myapplication;

import android.app.Application;

import com.example.reliablemessaging.ReliableMessaging;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ReliableMessaging.initialize(getApplicationContext());
    }
}
