package com.lara.bru.rastreio;

import java.util.HashMap;
import java.util.Map;

public class Cordinate {

    public int id = 0;
    public String token;
    public String lan;
    public String lng;
    public String data;
    public int status;

    public Cordinate(int id, String token, String lan, String lng, String data, int status){
        this.id = id;
        this.token = token;
        this.lan = lan;
        this.lng = lng;
        this.data = data;
        this.status = status;
    }

    public Cordinate(String token, String lan, String lng, String data, int status){
        this.token = token;
        this.lan = lan;
        this.lng = lng;
        this.data = data;
        this.status = status;
    }

    public Cordinate(String token, String lan, String lng){
        this.token = token;
        this.lan = lan;
        this.lng = lng;
        this.status = 0;
        this.data = "" + (System.currentTimeMillis() / 1000L);
    }

    public Map<String, String> toParam(){
        Map<String, String>  params = new HashMap<String, String>();

        params.put("lan", lan);
        params.put("lng", lng);
        params.put("token", token);
        params.put("data", data);
        return params;
    }
}
