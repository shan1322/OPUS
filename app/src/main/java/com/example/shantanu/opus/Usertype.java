package com.example.shantanu.opus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Usertype extends AppCompatActivity implements  View.OnClickListener{
    private RadioButton mcompany;
    private RadioButton mind;
    private RadioButton madmin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usertype);
        findViewById(R.id.signup).setOnClickListener(this);
        mcompany=findViewById(R.id.company);
        mind=findViewById(R.id.indiviual);
        madmin=findViewById(R.id.admin);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


    }
    private void writeNewUser(String userId, String usertype) {//push user type
        User user = new User(usertype);
        mDatabase.child(userId).child("Usertype").setValue(usertype);

    }
    public void onClick(View v) {
        int i = v.getId();
        Intent myIntent = new Intent(Usertype.this, Main2Activity.class);
        if (i == R.id.signup) {
            String user = FirebaseAuth.getInstance().getUid();
            if(mcompany.isChecked()) {
                writeNewUser(user, "Company");

                Usertype.this.startActivity(myIntent);
            }
            else if(mind.isChecked()) {
                writeNewUser(user, "Individual");
                Usertype.this.startActivity(myIntent);
            }
            else if(madmin.isChecked()) {
                writeNewUser(user, "Admin");
                Usertype.this.startActivity(myIntent);
            }


        }
    }

}
