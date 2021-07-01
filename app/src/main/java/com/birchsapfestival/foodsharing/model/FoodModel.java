package com.birchsapfestival.foodsharing.model;

import androidx.annotation.Nullable;

import com.birchsapfestival.foodsharing.utils.Randomizer;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


public class FoodModel implements Serializable {
    String url = "https://i0.wp.com/mr-shkaff.ru/800/600/https/33-podelki.ru/wp-content/uploads/2017/12/derevo-trafaret-dlya-vyrezaniya-6.png";
    //"https://reports.exodus-privacy.eu.org/en/reports/79702/icon/"; //"https://wmpics.pics/dm-0FXF.jpg";
    String title;
    double latitude, longitude;
    String data;
    String email;
    String phoneNumber;
    String type;


    public void setPhoneNumber(@Nullable String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    Long id = Randomizer.random.nextLong();

    public void setEmail(@Nullable String email) {
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

    public Long getId() {
        return id;
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
    public void setType(String type) { this.type = type; }
    public String getType() { return type; }

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

    public FoodModel(@Nullable String email, @Nullable String phoneNumber, String title, @Nullable Double latitude, @Nullable Double longitude, String data, Long id, String type) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
        this.id = id;
        this.type = type;
    }
}
