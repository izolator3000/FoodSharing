package com.example.foodsharing.model;

import androidx.annotation.NonNull;

public class FoodModel {
    String url = "https://wmpics.pics/dm-0FXF.jpg";
    String title;
    String address_title;
    String data;

    FoodModel(){

    }
    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress_title() {
        return address_title;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "FoodModel{" +
                "url='" + url + '\'' +
                ", title='" + title + '\'' +
                ", address_title='" + address_title + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public FoodModel(String title, String address_title, String data) {
        this.title = title;
        this.address_title = address_title;
        this.data = data;
    }
}
