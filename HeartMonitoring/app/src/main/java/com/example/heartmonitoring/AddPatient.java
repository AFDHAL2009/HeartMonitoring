package com.example.heartmonitoring;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heartmonitoring.model.Patient;
import com.example.heartmonitoring.model.Sensor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class AddPatient extends AppCompatActivity {
    public  EditText name1,lastname1,old1,email1,seek1,phone1;
     public Button btnAdd1;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);


        name1 = (EditText) findViewById(R.id.name);
        lastname1 = (EditText) findViewById(R.id.lastname);
        old1 = (EditText) findViewById(R.id.old);
        email1 = (EditText) findViewById(R.id.email);
        seek1 = (EditText) findViewById(R.id.seek);
        phone1 = (EditText) findViewById(R.id.phone);
        btnAdd1=(Button)findViewById(R.id.btnAdd);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'patients' node
        mFirebaseDatabase = mFirebaseInstance.getReference("patients");
        btnAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String name=name1.getText().toString();
               String lastname=lastname1.getText().toString();
               String  old= old1.getText().toString();
               String  email= email1.getText().toString();
               String seek=seek1.getText().toString();
               String phone=phone1.getText().toString();

                 if(name.length()==0||
                  lastname.length()==0||
                  old.length()==0||
                  email.length()==0||
                  seek.length()==0||
                  phone.length()==0){
                Toast.makeText(AddPatient.this,"Erreur:Veuillez validez les champs vides",Toast.LENGTH_SHORT).show();


                }
                else {
                  createPatient(name1.getText().toString() ,
                            lastname1.getText().toString(),
                            old1.getText().toString(),
                            email1.getText().toString(),
                           seek1.getText().toString(),
                            phone1.getText().toString()

                    );

                     Intent I = new Intent(AddPatient.this, UserActivty.class);
                     startActivity(I);

                }
            }
        });
    }

    /**
     * Creating new user node under 'users'
     */
    private void createPatient(String name, String lastname,String old, String email,String seek,String phone) {
        // TODO
        // In real apps this userId should be fetched
        // by implementing firebase auth

        patientId = mFirebaseDatabase.push().getKey();
        Patient patient = new Patient(name, lastname, old,  email, seek, phone);
        mFirebaseDatabase.child(patientId).setValue(patient);


    }
}