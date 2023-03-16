package com.nubar.jime.Classes;

import android.net.Uri;

public class User {
    String username;
    String email;
    String uid;
    String photourl;

    public User() {
    }

    public User(String username, String email, String uid,String photourl) {
        this.username = username;
        this.email = email;
        this.uid=uid;
        this.photourl=photourl;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotourl() {
        return photourl;
    }
}
