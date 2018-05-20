package com.example.shantanu.opus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById(R.id.signout).setOnClickListener(this);
    }
    private void signOut() {
        mAuth.signOut();
        Intent myIntent = new Intent(Main2Activity.this, MainActivity.class);
        Main2Activity.this.startActivity(myIntent);

    }
    public void onClick(View v) {
        int i = v.getId();
         if (i == R.id.signout) {
            signOut();
        }
    }
}


