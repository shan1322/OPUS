package com.example.shantanu.opus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText mEmailField;
    private EditText mPasswordField;
    private TextView msignup;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference mDatabase;
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailField = findViewById(R.id.Email);
        mPasswordField = findViewById(R.id.password);
        findViewById(R.id.signin).setOnClickListener(this);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        msignup=findViewById(R.id.signup);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        msignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Home.class);
                MainActivity.this.startActivity(myIntent);
            }
        });


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void signIn(String email, String password) {//email password login
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            String usertype="";
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                             DatabaseReference root =
                                    FirebaseDatabase.getInstance().getReference();
                             DatabaseReference key=root.child(FirebaseAuth.getInstance().getUid()).child("Usertype");
                            key.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String usertype=dataSnapshot.getValue(String.class);
                                   if(usertype.equalsIgnoreCase("Individual")) {
                                       try {

                                           Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                                           MainActivity.this.startActivity(myIntent);
                                       }
                                       catch (Exception e)
                                       {
                                           Toast.makeText(MainActivity.this, "User type=."+e,
                                                   Toast.LENGTH_SHORT).show();
                                           Log.d("test",""+e);
                                       }
                                   }
                                    else if(usertype.equalsIgnoreCase("Admin")) {
                                        Intent myIntent = new Intent(MainActivity.this, admin.class);
                                        MainActivity.this.startActivity(myIntent);
                                    }
                                    else{
                                       Toast.makeText(MainActivity.this, "User type=."+usertype,
                                               Toast.LENGTH_SHORT).show();
                                   }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Authentication failed22.", Toast.LENGTH_SHORT).show();

                        }

                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {//gmail
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            DatabaseReference root =
                                    FirebaseDatabase.getInstance().getReference();
                            DatabaseReference key=root.child(FirebaseAuth.getInstance().getUid()).child("Usertype");
                            key.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String usertype=dataSnapshot.getValue(String.class);
                                    if(usertype.equalsIgnoreCase("Individual")) {
                                        try {

                                            Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);
                                            MainActivity.this.startActivity(myIntent);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(MainActivity.this, "User type=."+e,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("test",""+e);
                                        }
                                    }
                                    else if(usertype.equalsIgnoreCase("Admin")) {
                                        try {


                                            Intent myIntent = new Intent(MainActivity.this, admin.class);
                                            MainActivity.this.startActivity(myIntent);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(MainActivity.this, "User type=."+e,
                                                    Toast.LENGTH_SHORT).show();
                                            Log.d("test",""+e);
                                        }

                                    }
                                    else{
                                        Toast.makeText(MainActivity.this, "User type=."+usertype,
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                           // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }
    private void signIn() {

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {


            findViewById(R.id.Email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            findViewById(R.id.signin).setVisibility(View.VISIBLE);


        } else {


            findViewById(R.id.Email).setVisibility(View.VISIBLE);
            findViewById(R.id.password).setVisibility(View.VISIBLE);
            findViewById(R.id.signin).setVisibility(View.VISIBLE);
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
       if (i == R.id.signin) {//sigin button click
            signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
        if (i == R.id.sign_in_button) {//google sign in
            signIn();
        }
    }
}
