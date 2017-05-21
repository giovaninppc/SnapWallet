package com.example.gnp.smartwallet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityInfo {

    private String name;
    private Date data;
    private double latitude;
    private double longitude;

    public ActivityInfo(String n, long d, double lat, double longi){
        name = n;
        data = new Date((long)d);
        latitude = lat;
        longitude = longi;
    }
    public ActivityInfo(String n, long d){
        name = n;
        data = new Date((long)d);
    }
    public ActivityInfo(String s, Date d){
        name = s;
        data = d;
    }
    public ActivityInfo(String n){
        name = n;
        data = new Date();
        latitude = -22.81236021;
        longitude =-47.06176179;

    }

    @Override
    public String toString(){
        SimpleDateFormat ft = new SimpleDateFormat ("E dd.MM.yyyy 'at' hh:mm");
        return "" + name + ", \nregistrado em: " + ft.format(data) + "\nlatitude:" + latitude + " longitude: " + longitude;
    }
}
