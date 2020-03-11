package com.lara.bru.rastreio;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lara.bru.rastreio.DbContext.DBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLocationWorker extends Worker {

    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1500;
    private static final float LOCATION_DISTANCE = 35f;
    //dbvar
    private DBHelper db = null;
    //request vars
    private String endPoint = "http://191.252.191.81:3000/sendCord";
    Map<String, String> params;
    RequestQueue queue;


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        public String convert(List<Cordinate> location) throws JSONException {
            JSONArray data = new JSONArray();
            for (Cordinate item : location) {
                JSONObject aux = new JSONObject();
                aux.put("id", item.id);
                aux.put("lan", item.lan);
                aux.put("lng", item.lng);
                aux.put("data", item.data);
                aux.put("token", item.token);
                data.put(aux);
            }
            return data.toString();
        }

        public void sendData() {
            StringRequest postRequest = new StringRequest(Request.Method.POST, endPoint,
                    response -> {
                        try {
                            JSONArray data = new JSONArray(response);
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject a = data.getJSONObject(i);
                                //o server vai retornar os objetos que foram enviados
                                //ele salva os status no bd
                                db.changeStatus(a.getInt("id"), 1);
                            }
                        } catch (JSONException e) {
                            System.out.println(Log.e(TAG, "Apenas Salvo no bd, sem conexao"));
                            e.printStackTrace();
                        }
                        System.out.println(Log.e(TAG, "Salvo e enviado"));
                        // response
                        Log.d("Sucesso", response);
                    },
                    error -> {
                        // error
                        System.out.println("Exectuando o evento de erro");
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    return params;
                }
            };

            // Add the request to the RequestQueue.
            queue.add(postRequest);

        }

        //se for apenas um elemento, adiciona no array, e envia
        public String convert(Cordinate location) throws JSONException {
            JSONArray data = new JSONArray();
            JSONObject aux = new JSONObject();
            aux.put("id", location.id);
            aux.put("lan", location.lan);
            aux.put("lng", location.lng);
            aux.put("data", location.data);
            aux.put("token", location.token);
            data.put(aux);
            return data.toString();
        }

        @Override
        public void onLocationChanged(Location location) {
            Cordinate cor = new Cordinate("TOKEN", "" + location.getLatitude(), "" + location.getLongitude());

            try {
                db.saveCordinates(cor);

                List<Cordinate> aux = db.getUnsync();
                String jsonData = convert(aux);

                params = new HashMap<String, String>();
                params.put("data", jsonData);
                sendData();

//                System.out.println(response);
            } catch (Exception ex) {
                System.out.println("erro ao salvar no bd: " + ex);
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

    public MyLocationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        queue = Volley.newRequestQueue(getApplicationContext());
        db = new DBHelper(getApplicationContext());


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

        return Result.success();
    }
}
