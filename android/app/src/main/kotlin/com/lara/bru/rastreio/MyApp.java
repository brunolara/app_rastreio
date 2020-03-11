package com.lara.bru.rastreio;

import android.content.Context;


import io.flutter.app.FlutterApplication;

public class MyApp extends FlutterApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();


        MyApp.context = getApplicationContext();

    }
    public static Context getAppContext() {
        return MyApp.context;
    }
}
