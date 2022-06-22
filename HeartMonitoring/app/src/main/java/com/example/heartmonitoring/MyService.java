package com.example.heartmonitoring;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.heartmonitoring.model.Sensor;
import com.example.heartmonitoring.model.Sensor1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

public class MyService extends Service {
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
     String PatientName;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"service supervision commencée",Toast.LENGTH_SHORT).show();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("name", "");
        Toast.makeText(getApplicationContext(),"service supervision de patient ="+s1+" est commencé",
                Toast.LENGTH_SHORT).show();
        PatientName= s1;
        ListenerForRequestDone();
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("RESTART_SERVICE");
        sendBroadcast(intent);
    }


    public void ListenerForRequestDone(){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("appareils");
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference("sensors");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("sensor");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Sensor sensor = dataSnapshot.getValue(Sensor.class);

                //Log.d(TAG, "User name: " + user.name + ", email " + user.email);
              // Toast.makeText(getApplicationContext(),user.name,Toast.LENGTH_SHORT).show();
                test();
              Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    /**
     * Creating new user node under 'users'
     */
    private void createSensor(String avg, String bmp,String ir) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth
        mFirebaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference("sensors");
       // userId = mDatabase1.push().getKey();
        userId=PatientName+mDatabase1.push().getKey();
        Sensor1 sensor = new Sensor1(avg,bmp,ir);
        mDatabase1.child(userId).setValue(sensor);


    }


    private  void test() {
       // DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("sensors");
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("sensor");
        Query query = db.orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                   // Log.d("User key", child.getKey());
                   // Log.d("User val", child.child("field1").getValue().toString());
                    // Toast.makeText(getApplicationContext(),child.child("field1").getValue().toString(),Toast.LENGTH_SHORT).show();
                   //   Toast.makeText(getApplicationContext(),child.child("bmp").getValue().toString(),Toast.LENGTH_SHORT).show();

                   if(child.getKey().toString().equals("bmp")) {
                       Toast.makeText(getApplicationContext(), child.getValue().toString(), Toast.LENGTH_SHORT).show();

                       Intent intent = new Intent("GPSLocationUpdates");
                       Bundle bundle = new Bundle();

                       String avg = "0.0";
                       String bmp=child.getValue().toString();
                       String ir = "0.0";

                       // Check for already existed userId
                       createSensor(avg, bmp,ir);
                       //bundle.putString("field1", child.child("field1").getValue().toString());
                       bundle.putString("bmp",  child.getValue().toString());
                       intent.putExtras(bundle);
                       LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);


                   }



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: Handle errors.
            }
        });
    }


}