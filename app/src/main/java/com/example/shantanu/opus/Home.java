package com.example.shantanu.opus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class Home extends AppCompatActivity implements  View.OnClickListener{
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;



    private static final String TAG = "Home";

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mEmailField = findViewById(R.id.Email);
        mPasswordField = findViewById(R.id.password);
        findViewById(R.id.signup).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }
    private void createAccount(String email, String password) {//create account
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                           Intent myIntent = new Intent(Home.this, Usertype.class);
                            Home.this.startActivity(myIntent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Home.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {


            findViewById(R.id.Email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            findViewById(R.id.signup).setVisibility(View.VISIBLE);


        } else {


            findViewById(R.id.Email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            findViewById(R.id.signup).setVisibility(View.VISIBLE);
        }
    }
     private boolean validateForm() {
         boolean valid = true;

         String email = mEmailField.getText().toString();
         if (TextUtils.isEmpty(email)) {
             mEmailField.setError("Required.");
             valid = false;
         } else {
             mEmailField.setError(null);
         }

         String password = mPasswordField.getText().toString();
         if (TextUtils.isEmpty(password)) {
             mPasswordField.setError("Required.");
             valid = false;
         } else {
             mPasswordField.setError(null);
         }

         return valid;
     }


     public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signup) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());



        }
    }
}
