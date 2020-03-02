package com.lara.bru.rastreio;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class RequestHelper {
    private String endPoint = "http://191.252.191.81:3000/sendCord";
    Map<String, String> params;
    RequestQueue queue;

    public RequestHelper(Context context){
        queue = Volley.newRequestQueue(context);
    }

    public void setData(Map<String, String> data){
        params = data;
    }
    public void setData(String data){
        Map<String, String>  aux = new HashMap<String, String>();
        aux.put("data", data);
        params = aux;
    }

    public StringRequest makeRequest(){
        // Request a string response from the provided URL.
        StringRequest postRequest = new StringRequest(Request.Method.POST, endPoint,
                response -> {
                    // response
                    Log.d("Response", response);
                },
                error -> {
                    // error
                    Log.d("Error.Response", error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(postRequest);

        return postRequest;
    }
}
