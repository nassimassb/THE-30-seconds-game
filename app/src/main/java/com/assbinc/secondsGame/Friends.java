package com.assbinc.secondsGame;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class Friends {
    private String username;
    private String friends;

    public Friends() {

    }

    public Friends(String username, String friends) {
        this.username = username;
        this.friends = friends;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friend) {
        this.friends = friend;
    }

    public String getUsername() {
        return username;
    }
}
