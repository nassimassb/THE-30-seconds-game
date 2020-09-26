package com.assbinc.secondsGame;

import com.google.firebase.firestore.IgnoreExtraProperties;

import androidx.annotation.NonNull;

@IgnoreExtraProperties
public class User {
    private String username;
    private String email;
    private String password;
    private int profileScore;

    public User() {

    }

    public User(String username, String email, String password, int profileScore) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.profileScore = profileScore;
    }

    public String getUsername(){
        return this.username;
    }

    public String getEmail(){
        return this.email;
    }

    public int getProfileScore(){
        return this.profileScore;
    }

    @NonNull
    @Override
    public String toString() {
        return "Username: " + username + " | email: " + email + " | profileScore: " + profileScore + " | password: " + password;
    }
}