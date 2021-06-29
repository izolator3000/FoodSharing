package com.birchsapfestival.foodsharing.model;

import android.net.Uri;

import javax.annotation.Nullable;

public class User {
    private String name;
    private String email;
    private String phoneNumber;
    private boolean emailVerified;
    private String uid;
    private Uri photoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", uid='" + uid + '\'' +
                '}';
    }

    public User(String name, @Nullable String email, @Nullable String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getUid() {
        return uid;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrl(Uri url) {
        this.photoUrl = url;
    }
}
