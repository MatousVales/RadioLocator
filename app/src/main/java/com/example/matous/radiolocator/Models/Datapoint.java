package com.example.matous.radiolocator.Models;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Matous on 16.02.2016.
 */
public class Datapoint {

    private double latitude;
    private double longitude;
    private double angle;
    private Date datetime;
    SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss DD.mm.yyyy");

    public Datapoint(String ti,double la, double lo, double a){
        this.latitude = la;
        this.longitude = lo;
        this.angle = a;
        try {
            this.datetime = dateFormat.parse(ti);
            Log.d("datetime",datetime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Datapoint(){
    }

    public String getDate(){
        String dTime = dateFormat.format(datetime);
        Log.d("datetime", datetime.toString());
        return dTime.substring(8);
    }

    public String getTime() {
        String dTime = dateFormat.format(datetime);
        Log.d("datetime", datetime.toString());
        return dTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAngle() {
        return angle;
    }

    public void setTime(String time) {
        try {
            this.datetime = dateFormat.parse(time);
            Log.d("datetime", datetime.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}

