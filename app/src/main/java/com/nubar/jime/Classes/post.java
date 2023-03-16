package com.nubar.jime.Classes;

public class post {
    String post_title;
    String post_description;
    String random_num;
    String latitude;
    String longitude;
    String uid;

    public post() {
    }

    public post(String post_title, String post_description, String random_num, String latitude, String longitude, String uid) {
        this.post_title = post_title;
        this.post_description = post_description;
        this.random_num = random_num;
        this.latitude = latitude;
        this.longitude = longitude;
        this.uid=uid;
    }

    public String getPost_title() {
        return post_title;
    }

    public String getPost_description() {
        return post_description;
    }

    public String getRandom_num() {
        return random_num;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUid() {
        return uid;
    }
}
