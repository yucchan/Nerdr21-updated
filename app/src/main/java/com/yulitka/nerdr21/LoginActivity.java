package com.yulitka.nerdr21;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_login);

        final Button loginBtn = findViewById(R.id.liLoginBtn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email=((EditText) findViewById(R.id.liUsernameText)).getText().toString();
                String password=((EditText) findViewById(R.id.liPasswordText)).getText().toString();
                attemptLogin(email,password);

            }
        });

        final TextView registrationBtn = findViewById(R.id.liRegistrationLink);
        registrationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegistrationActivity.class);
                startActivity(i);
            }
        });

        final ImageView infoImgBtn = findViewById(R.id.liInfoImage);
        infoImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,InfoActivity.class);
                finish();
                startActivity(i);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        // To enable automatic login
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);

        // We want the user to login each time
        mAuth.signOut();
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Signed in
            Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(i);
        }
    }

    private void attemptLogin(String email,String password){
        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("login", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("login", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, R.string.login_failed,
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        } catch (Exception e){
            Log.d("loginError","auth failed");
        }
    }


}
