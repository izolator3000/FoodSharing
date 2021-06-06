package com.example.foodsharing.model;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class FoodModel implements Serializable {
    String url = "https://wmpics.pics/dm-0FXF.jpg";
    String title;
    double latitude, longitude;
    String data;
    String email;

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public FoodModel() {

    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Double> getAddress() {
        return new ArrayList<>(Arrays.asList(latitude, longitude));
    }

    public String getData() {
        return data;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAddress(LatLng coordinates) {
        latitude = coordinates.latitude;
        longitude = coordinates.longitude;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", data='" + data + '\'' +
                '}';
    }

    public FoodModel(String email, String title, @Nullable Double latitude, @Nullable Double longitude, String data) {
        this.email = email;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
    }
}
