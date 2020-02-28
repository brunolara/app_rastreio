package com.lara.bru.rastreio;


import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.lara.bru.rastreio.DbContext.DBHelper;
import com.lara.bru.rastreio.DbContext.PostContract;
import com.lara.bru.rastreio.DbContext.PostDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;

public class MyService  extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private DBHelper db = null;


    private Socket mSocket;
    {
        try {
            IO.Options opts = new IO.Options();

            opts.reconnection = true;
            mSocket = IO.socket("http://191.252.191.81:3000", opts);
            Log.e(TAG, "Conectou socket");
        } catch (URISyntaxException e) {}
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        public String convert(Location location) throws JSONException {
            JSONObject data = new JSONObject();
            data.put("lan", location.getLatitude());
            data.put("lng", location.getLongitude());

            return data.toString();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            Cordinate cor = new Cordinate("TOKEN", "" + location.getLatitude(), "" +location.getLongitude() );
            db.saveCordinates(cor);
            List<Cordinate> aux = db.getAll();
            for (Cordinate item: aux) {
                System.out.println(item.token +" - "+ item.lan +" - "+ item.lng +" - "+ item.data);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");

        mSocket.connect();
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {

        db = new DBHelper(this);

        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationManager manager = getSystemService(NotificationManager.class);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "messages")
                    .setContentText("está rodando em back")
                    .setContentTitle("Flutter back")
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark);
            startForeground(101,builder.build());
        }

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");

        super.onDestroy();
        mSocket.disconnect();

        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}