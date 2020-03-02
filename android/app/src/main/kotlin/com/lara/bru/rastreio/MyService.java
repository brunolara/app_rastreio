package com.lara.bru.rastreio;


import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.toolbox.StringRequest;
import com.lara.bru.rastreio.DbContext.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyService  extends Service {
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private DBHelper db = null;
    RequestHelper requestHelper = null;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        public String convert(List<Cordinate> location) throws JSONException {
            JSONArray data = new JSONArray();
            for(Cordinate item: location){
                JSONObject aux = new JSONObject();
                aux.put("lan", item.lan);
                aux.put("lng", item.lng);
                aux.put("data", item.data);
                aux.put("token", item.token);
                data.put(aux);
            }
            return data.toString();
        }

        //se for apenas um elemento, adiciona no array, e envia
        public String convert(Cordinate location) throws JSONException {
            JSONArray data = new JSONArray();
            JSONObject aux = new JSONObject();
            aux.put("lan", location.lan);
            aux.put("lng", location.lng);
            aux.put("data", location.data);
            aux.put("token", location.token);
            data.put(aux);
            return data.toString();
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            Cordinate cor = new Cordinate("TOKEN", "" + location.getLatitude(), "" +location.getLongitude());

            try {
                String jsonData = convert(cor);
                requestHelper.setData(jsonData);
                StringRequest response = requestHelper.makeRequest();
                System.out.println(response.deliverError());
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            db.saveCordinates(cor);
//            List<Cordinate> aux = db.getAll();
//            for (Cordinate item: aux) {
//                System.out.println(item.token +" - "+ item.lan +" - "+ item.lng +" - "+ item.data);
//            }
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
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCreate() {

        requestHelper = new RequestHelper(this);

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