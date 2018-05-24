package com.example.shantanu.opus;

public class User {

    public String usertype;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String usertype) {
        this.usertype = usertype;

    }

}
