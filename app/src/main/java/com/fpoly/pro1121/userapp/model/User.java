package com.fpoly.pro1121.userapp.model;

public class User {
    private String id;
    private String name;
    private String location;
    private String phoneNumber;
    private String urlImage;
    private boolean isAdmin;


    public User(String id, String name, String location, String phoneNumber, String urlImage, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.urlImage = urlImage;
        this.isAdmin = isAdmin;
    }

    public User(String id, String name, String location, String phoneNumber, String urlImage) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.urlImage = urlImage;
    }

    public User(String name) {
        this.name = name;
    }

    public void setData(String name, String location, String phoneNumber, String urlImage) {
        this.name = name;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.urlImage = urlImage;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
