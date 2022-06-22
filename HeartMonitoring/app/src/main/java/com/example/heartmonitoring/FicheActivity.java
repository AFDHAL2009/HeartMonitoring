package com.example.heartmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmonitoring.model.Patient;
import com.example.heartmonitoring.model.Sensor1;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FicheActivity extends AppCompatActivity {
    private TextView ficheId1;
    private TextView ficheName;
    private TextView ficheLastname;
    private TextView ficheOld;
    private TextView ficheEmail;
    private TextView ficheSeek;
    private TextView fichePhone;
    private Button Exit1;
    private Button Send;
    private TableLayout mTableLayout;

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference mFirebaseDatabase1;
    private FirebaseDatabase mFirebaseInstance;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList=new ArrayList<>();
    private List<String> where = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fiche);
        ficheId1=(TextView)findViewById(R.id.ficheId);
        ficheName=(TextView)findViewById(R.id.name1);
        ficheLastname=(TextView)findViewById(R.id.lastname);
        ficheOld=(TextView)findViewById(R.id.old);
        ficheEmail=(TextView)findViewById(R.id.email);
        ficheSeek=(TextView)findViewById(R.id.seek);
        fichePhone=(TextView)findViewById(R.id.phone);
        Exit1=(Button)findViewById(R.id.Exit);
        Send=(Button)findViewById(R.id.export);

        // setup the table

        mTableLayout = (TableLayout) findViewById(R.id.tableSensors);
        mTableLayout.setStretchAllColumns(true);

        // data columns
      /*  TableRow row1 = new TableRow(FicheActivity.this);
        row1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tv1 = new TextView(FicheActivity.this);
        tv1.setText("bmp");
        tv1.setTextSize(20);
        tv1.setTypeface(null, Typeface.BOLD);
        row1.addView(tv1);
        mTableLayout.addView(row1);*/



        final TextView tv = new TextView(this);
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setGravity(Gravity.LEFT);
        tv.setPadding(5, 15, 0, 15);


        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                String s1 = sh.getString("name", "");
                Toast.makeText(getApplicationContext(),"name="+s1,
                        Toast.LENGTH_SHORT).show();

             sendEmail();
                Toast.makeText(getApplicationContext(),"tab="+where,
                        Toast.LENGTH_SHORT).show();
            }

        });
        Exit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent I = new Intent(FicheActivity.this, UserActivty.class);
                    startActivity(I);

                }

        });


        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase1 = mFirebaseInstance.getReference("sensors");
        mFirebaseDatabase = mFirebaseInstance.getReference("patients");
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b!=null)
        {
            String j =(String) b.get("name");
            ficheId1.setText("Fiche patient("+j+")");
        }
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final Patient dailyItem = data.getValue(Patient.class);
                    String getname =(String) b.get("name");
                   if(dailyItem.name.equals(getname)) {
                       ;
                       Log.d("res3=",dailyItem.phone);

                       ficheName.setText(dailyItem.name);
                       ficheLastname.setText(dailyItem.lastname);
                       ficheOld.setText(dailyItem.old);
                       ficheEmail.setText(dailyItem.email);
                       ficheSeek.setText(dailyItem.seek);
                       fichePhone.setText(dailyItem.phone);
                   }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"database error",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //
        mFirebaseDatabase1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final Sensor1 dailyItem = data.getValue(Sensor1.class);
                    String tt  = data.getKey().toString();
                    String getname =(String) b.get("name");
                    boolean isFound = tt.indexOf(getname) !=-1? true: false; //true
                    Log.d("bmp=",dailyItem.bmp);
                    if(isFound) {
                        Log.d("bmp1=", tt);
                        where.add(dailyItem.bmp);

                               TableRow row = new TableRow(FicheActivity.this);
                                String value0 = dailyItem.avg;
                                String value1 = dailyItem.bmp;
                                String value2 = dailyItem.ir;

                                TextView avg1 = new TextView(FicheActivity.this);
                                TextView bmp1 = new TextView(FicheActivity.this);
                                TextView ir1 = new TextView(FicheActivity.this);
                                avg1.setText(value0);
                                bmp1.setText(value1);
                                ir1.setText(value2);
                                row.addView(avg1);
                                row.addView(bmp1);
                                row.addView(ir1);
                                mTableLayout.addView(row);




                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"database error",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
    @SuppressLint("LongLogTag")
    protected void sendEmail() {
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String name = sh.getString("name", "");


        Log.i("Send email", "");
        String[] TO = {"nejibafdhal@yahoo.fr"};
        String[] CC = {"afdhalnejib@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Rélevement cardiaque supervision data");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Bonjour,cher Patient "+name+" \n " +
                "Voici liste des relevements cardiaque prise par votre medeçin mesuré en (BMP) lors de votre supervision :\n" +
                where+"\n"+
           "Créé par HeartMonitoring app " +
                "developée par Nejib Afdhal & Chaima Massouda\n"
                +"tous droits reservé\n" +
                "contactez nous:nejibafdhal@yahoo.fr/chaima.benmessaoud.2000@gmail.com"
        );

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email..", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FicheActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    //
    }




