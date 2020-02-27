package com.lara.bru.rastreio;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.app.FlutterApplication;

public class MyApp extends FlutterApplication {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();


        MyApp.context = getApplicationContext();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("messages","Messages", NotificationManager.IMPORTANCE_LOW);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    public static Context getAppContext() {
        return MyApp.context;
    }
}
