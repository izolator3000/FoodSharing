package com.example.foodsharing.model;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private boolean emailVerified;
    private String uid;
    private Uri photoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    public Uri getPhotoUrl(){return photoUrl;}

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setUrl(Uri url){this.photoUrl = url;}
}
