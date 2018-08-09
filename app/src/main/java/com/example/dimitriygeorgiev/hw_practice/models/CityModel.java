package com.example.dimitriygeorgiev.hw_practice.models;

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class CityModel {
    private String imageUrl;
    private String city;
    private String description;
    private String timeStamp;
    private int state;
    private int likes;
    private List<Object> users = new ArrayList<>();

    public CityModel(){}

    public CityModel(String imageUrl, String city, String description) {
        this.imageUrl = imageUrl;
        this.city = city;
        this.description = description;
        this.state = 0;
        this.timeStamp = getCurrentTime();
        this.likes = 0;
    }

    private String getCurrentTime() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        return ts;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<Object> getUsers() {
        return users;
    }

    public void setUsers(List<Object> users) {
        this.users = users;
    }

    public boolean incrementLikes(String user) {
        if (!users.contains(user)) {
            likes += 1;
            users.add(user);
            return true;
        } else {
            likes -= 1;
            users.remove(user);
            return false;
        }
    }
}
