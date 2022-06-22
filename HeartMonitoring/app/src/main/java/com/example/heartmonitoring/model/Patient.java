package com.example.heartmonitoring.model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Patient {

    public String name;
    public String lastname;
    public String old;
    public String email;
    public String seek;
    public String phone;


    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public Patient() {
    }

    public Patient(String name, String lastname,String old, String email,String seek,String phone) {
        this.name = name;
        this.lastname = lastname;
        this.old = old;
        this.email = email;
        this.seek = seek;
        this.phone = phone;


    }
}