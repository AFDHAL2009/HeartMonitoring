package com.example.heartmonitoring;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by velmmuru on 1/6/2018.
 */

public class Supervisor1Fragment extends Fragment {
    @Nullable
    private  List<Entry> lineEntries1 = new ArrayList<Entry>();
    RequestQueue queue;
    private int start=0;
    private TextView sup;
    //private float lastValue= (float) 0.0;
    private Handler handler = new Handler();
    private  float lastValue[]= new float[1000];
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private MyBroadcastReceiver myReceiver;
    private String userId;
    private Button add;
    private EditText inputField1, inputField2;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.supervisor1_fragment,container,false);

    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        LineChart chart = (LineChart) getView().findViewById(R.id.chart);
        sup=(TextView) getView().findViewById(R.id.supervised);
        // get reference to 'users' node
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("sensors");
        SharedPreferences sh = getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String s1 = sh.getString("name", "");
        Toast.makeText( getActivity(),"supervision numérique de patient ="+s1,
                Toast.LENGTH_SHORT).show();
        sup.setText("Patient("+s1+")");


        lineEntries1.add(new Entry(0, 0));
        drawLineChart();
        lastValue[start]= (float) 0.0;

      //  handler.postDelayed(runnable, 2000);
        getActivity().startService(new Intent(getActivity(),MyService.class));
        mFirebaseInstance = FirebaseDatabase.getInstance();


        }


    private void drawLineChart() {
        LineChart lineChart = getView().findViewById(R.id.chart);
        List<Entry> lineEntries =  lineEntries1;
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "bmp");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setHighlightEnabled(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setColor(Color.RED);
        lineDataSet.setCircleColor(Color.YELLOW);
        lineDataSet.setCircleRadius(6);
        lineDataSet.setCircleHoleRadius(3);
        lineDataSet.setDrawHighlightIndicators(true);
        lineDataSet.setHighLightColor(Color.RED);
        lineDataSet.setValueTextSize(12);
        lineDataSet.setValueTextColor(Color.DKGRAY);

        LineData lineData = new LineData(lineDataSet);
        lineChart.getDescription().setText("bmp");
        lineChart.getDescription().setTextSize(12);
        lineChart.setDrawMarkers(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        lineChart.animateY(1000);
        lineChart.getXAxis().setGranularityEnabled(true);
        lineChart.getXAxis().setGranularity(1.0f);
        lineChart.getXAxis().setLabelCount(lineDataSet.getEntryCount());
        lineChart.setData(lineData);
    }
    private List<Entry> getDataSet1() {
        List<Entry> lineEntries = new ArrayList<Entry>();
        lineEntries.add(new Entry(0, 1));
        lineEntries.add(new Entry(1, 2));
        lineEntries.add(new Entry(2, 3));
        lineEntries.add(new Entry(3, 4));
        lineEntries.add(new Entry(4, 2));
        lineEntries.add(new Entry(5, 3));
        lineEntries.add(new Entry(6, 1));
        lineEntries.add(new Entry(7, 5));
        lineEntries.add(new Entry(8, 7));
        lineEntries.add(new Entry(9, 6));
        lineEntries.add(new Entry(10, (float) 0.2));
        lineEntries.add(new Entry(11, 5));
        lineEntries.add(new Entry(12, 7));
        lineEntries.add(new Entry(13, 5));
        lineEntries.add(new Entry(14, 2));
        return lineEntries;
    }

    //
    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getContext(),"Supervision graphique",Toast.LENGTH_SHORT).show();
        myReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter("GPSLocationUpdates");
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(myReceiver, intentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        if(myReceiver != null) LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(myReceiver);
        myReceiver = null;
    }
///
public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Here you have the received broadcast
        // And if you added extras to the intent get them here too
        // this needs some null checks
        Bundle b = intent.getExtras();
        String yourValue = b.getString("bmp");
        Toast.makeText(getContext(),"bmp value="+yourValue,Toast.LENGTH_SHORT).show();
        start=start+1;
        lineEntries1.add(new Entry(start, Float.parseFloat(yourValue)));
        drawLineChart();

    }
}


}


