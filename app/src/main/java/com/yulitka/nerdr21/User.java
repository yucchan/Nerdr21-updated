package com.yulitka.nerdr21;

import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {
    private String email;
    private String username;
    private String gender;
    private Bitmap profileImg;
    private String country;
    private String city;
    private String description="";

    public User(){
        this.refreshUser(null);
    }
//
//    public User() {
//    }

    public User(User user){
        this.copyUser(user);
    }

    public void copyUser(User user) {
        this.city = user.city;
        this.country = user.country;
        this.email = user.email;
        this.gender = user.gender;
        this.profileImg = user.profileImg;
        this.username = user.username;
        this.description = user.description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    // This constructor for initialization after registration
    public User(String email, String username, String gender, String country, String city) {
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.country = country;
        this.city = city;
    }

    public User(final String username) {
        refreshUser(username);
    }

    public void refreshUser(final String username){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                if(username !=null) {
                    User loaded = new User(dataSnapshot.child(username).getValue(User.class));
                    copyUser(loaded);

                    Log.d("firebaseRead", "User is: " + loaded.username);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("firebaseRead", "Failed to read user.", error.toException());
            }

        });

    }


    public void storeUser(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");

        if(this.username != null) {
            myRef.child(this.username).setValue(this);
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Bitmap getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(Bitmap profileImg) {
        this.profileImg = profileImg;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
