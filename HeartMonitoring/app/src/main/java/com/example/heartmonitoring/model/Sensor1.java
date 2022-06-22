package com.example.heartmonitoring.model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sensor1 {

    public String avg;
    public String bmp;
    public String ir;

    // Default constructor required for calls to
    // DataSnapshot.getValue(Sensor1.class)
    public Sensor1() {
    }

    public Sensor1(String avg, String bmp,String ir) {
        this.avg = avg;
        this.bmp = bmp;
        this.ir=ir;
    }
}