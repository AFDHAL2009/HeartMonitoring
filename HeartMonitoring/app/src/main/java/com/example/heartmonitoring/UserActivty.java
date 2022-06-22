package com.example.heartmonitoring;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmonitoring.model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserActivty extends AppCompatActivity {
    Button btnLogOut;
    ImageView add;
    private ListView dataListView;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_activty);
        dataListView = (ListView) findViewById(R.id.dataListView);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'patients' node
        mFirebaseDatabase = mFirebaseInstance.getReference("patients");
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,arrayList);
        dataListView.setAdapter( adapter);
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot data : dataSnapshot.getChildren()){
                    final Patient dailyItem = data.getValue(Patient.class);
                    arrayList.add(dailyItem.name);
                    adapter.notifyDataSetChanged();
                   Log.d("res1=",dailyItem.phone);
                   Log.d("tt=",dataSnapshot.getValue().toString());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"database error",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String s =  dataListView.getItemAtPosition(i).toString();

                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                PatientDialg(s);

                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                // write all the data entered by the user in SharedPreference and apply
                myEdit.putString("name", s);
                myEdit.apply();


            }
        });
        btnLogOut = (Button) findViewById(R.id.btnLogOut);

        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent I = new Intent(UserActivty.this, AddPatient.class);
                startActivity(I);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Intent I = new Intent(UserActivty.this, Home.class);
                startActivity(I);

            }
        });



    }
    private void PatientDialg(String name){
        Button cancel;
        Button supervised;
        Button consulted;
        Button deleted;
        TextView name1;
        AlertDialog.Builder builder = new AlertDialog.Builder(UserActivty.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.customview, viewGroup, false);
        name1=(TextView)dialogView.findViewById(R.id.name);
        name1.setText("Patient("+name+")");
        supervised=(Button)dialogView.findViewById(R.id.supervised);
        consulted=(Button)dialogView.findViewById(R.id.consulted);
        deleted=(Button)dialogView.findViewById(R.id.deleted);
        cancel = (Button)dialogView.findViewById(R.id.Exit);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        supervised.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(UserActivty.this, MainActivity.class);
                I.putExtra("name", name);
                startActivity(I);
            }
        });

        consulted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I = new Intent(UserActivty.this, FicheActivity.class);
                I.putExtra("name", name);
                startActivity(I);
            }
        });
        deleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "delete", Toast.LENGTH_LONG).show();
                Query applesQuery =  mFirebaseDatabase.orderByChild("name").equalTo(name);
                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("TAG", "onCancelled", databaseError.toException());
                    }
                });

                Intent I = new Intent(UserActivty.this, UserActivty.class);
                startActivity(I);
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }
}
