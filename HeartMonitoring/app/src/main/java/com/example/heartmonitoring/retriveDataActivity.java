package com.example.heartmonitoring;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.RequestQueue;
import com.example.heartmonitoring.model.Sensor;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.macroyau.thingspeakandroid.ThingSpeakChannel;
import com.macroyau.thingspeakandroid.ThingSpeakLineChart;


public class retriveDataActivity extends AppCompatActivity {
    private final String API_KEY = "BBFF-e7a4f1eb5949332c20f91863212ff0df8fe";
    private final String VARIABLE_ID = "621d11f3cfe51213fc3399c7";
    private ThingSpeakChannel tsChannel;
    private ThingSpeakLineChart tsChart;
    private final String url = "https://api.thingspeak.com/channels/829296/feeds.json?api_key=XHA1BB868NHQE9SN&results=2";

    private final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    RequestQueue queue;
   private Handler handler = new Handler();
   private TextView field;
   private TextView sup;
   private Button start;
   private Button pause;
   private ImageButton home;
   private ImageButton save;
   private DatabaseReference mFirebaseDatabase;
   private DatabaseReference mFirebaseDatabase1;
   private FirebaseDatabase mFirebaseInstance;
   private String userId;
   private EditText inputName, inputEmail;
   private MyBroadcastReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_data);
        field = (TextView) findViewById(R.id.field2);
        sup=(TextView) findViewById(R.id.supervised);
        start = (Button) findViewById(R.id.start);
        pause = (Button) findViewById(R.id.pause);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        // get reference to 'users' node
        mFirebaseDatabase1 = mFirebaseInstance.getReference("sensors");

        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("name", "");
        Toast.makeText(getApplicationContext(),"supervision numérique de patient ="+s1,
                Toast.LENGTH_SHORT).show();
        sup.setText("Patient("+s1+")");

        startService(new Intent(this, MyService.class));

        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
               Toast.makeText(retriveDataActivity.this,"Moniteur est commencé",Toast.LENGTH_SHORT).show();
               // handler.postDelayed(runnable, 2000);


            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // your handler code here
               Toast.makeText(retriveDataActivity.this,"Moniteur est arreté",Toast.LENGTH_SHORT).show();
                stopService(new Intent(getApplicationContext(), MyService.class));
            }
        });


        }

//Start
@Override
protected void onResume() {
    super.onResume();
    Toast.makeText(retriveDataActivity.this,"resume",Toast.LENGTH_SHORT).show();
    myReceiver = new  MyBroadcastReceiver();
    final IntentFilter intentFilter = new IntentFilter("GPSLocationUpdates");
    LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver, intentFilter);

}

    @Override
    public void onPause(){
        super.onPause();
        if(myReceiver != null) LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        myReceiver = null;
    }

    /**
     * Creating new user node under 'users'
     */
    private void createSensor(String field1, String field2) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth


        userId = mFirebaseDatabase1.push().getKey();
        Sensor sensor = new Sensor(field1, field2);
        mFirebaseDatabase1.child(userId).setValue(sensor);


    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Here you have the received broadcast
            // And if you added extras to the intent get them here too
            // this needs some null checks
            Bundle b = intent.getExtras();
            String yourValue = b.getString("bmp");
            Toast.makeText(retriveDataActivity.this,"bmp value="+yourValue,Toast.LENGTH_SHORT).show();
            field.setText(yourValue);
            // double someDouble = b.getDouble("doubleName");
            ///do something with someDouble
        }
    }



}


