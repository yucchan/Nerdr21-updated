package com.yulitka.nerdr21;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mAuth = FirebaseAuth.getInstance();


        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);
        AutoCompleteTextView countriesView = findViewById(R.id.reCountryAC);
        countriesView.setAdapter(countriesAdapter);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, CITIES);
        MultiAutoCompleteTextView citiesView = findViewById(R.id.reCityMAC);
        citiesView.setAdapter(citiesAdapter);

        final Button reRegistrationBtn = findViewById(R.id.reRegistrationBtn);
        reRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email=((EditText) findViewById(R.id.reEmail)).getText().toString();
                String password=((EditText) findViewById(R.id.rePasswordText)).getText().toString();

                createUser(email, password);
            }
        });

        final ImageView infoImgBtn = findViewById(R.id.reInfoImage);
        infoImgBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),InfoActivity.class);
                startActivity(i);
            }
        });
    }

    private void createUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("register", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("register", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, R.string.login_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    // At this point only Israel is supported
    private static final String[] COUNTRIES = new String[] {
            "Israel"
    };

    // TODO load cities from file
    private static final String[] CITIES = new String[] {
            "Tel Aviv", "Jerusalem", "Haifa", "Eilat"
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            // Save user info to Firebase database
            String email=((EditText) findViewById(R.id.reEmail)).getText().toString();
            String username=((EditText) findViewById(R.id.reUsernameText)).getText().toString();
            String gender;
            if(((RadioButton) findViewById(R.id.reFemaleBtn)).isChecked()) {
                gender = "Female";
            }
            else {
                gender = "Male";
            }
            String country=((AutoCompleteTextView) findViewById(R.id.reCountryAC)).getText().toString();
            String city=((AutoCompleteTextView) findViewById(R.id.reCityMAC)).getText().toString();
            User registeredUser = new User(email, username, gender, country, city);
            user.copyUser(registeredUser);
            user.storeUser();

            // Save username to shared preferences
            SharedPreferences ref = getSharedPreferences("connected",MODE_PRIVATE);
            SharedPreferences.Editor ed = ref.edit();
            ed.putString("username", username);
            ed.commit();

            // Redirect to profile creation screen
            Intent i = new Intent(getApplicationContext(), EditProfileActivity.class);
            startActivity(i);
        }
    }
}
