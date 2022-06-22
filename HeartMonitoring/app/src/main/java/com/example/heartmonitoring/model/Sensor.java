package com.example.heartmonitoring.model;



import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Sensor {

    public String field1;
    public String field2;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Sensor() {
    }

    public Sensor(String field1, String field2) {
        this.field1 = field1;
        this.field2 = field2;
    }
}