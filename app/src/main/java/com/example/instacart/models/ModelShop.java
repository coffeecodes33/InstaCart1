package com.example.instacart.models;

public class ModelShop {

    private String uid,email,password,name,shopName,phone,deliveryFee,country,state,city,address,
            timestamp,accountType,online,shopOpen;

    public ModelShop() {

    }

    public ModelShop(String uid, String email, String password, String name, String shopName, String phone, String deliveryFee, String country, String state, String city, String address, String timestamp, String accountType, String online, String shopOpen) {
        this.uid = uid;
        this.email = email;
        this.password = password;
        this.name = name;
        this.shopName = shopName;
        this.phone = phone;
        this.deliveryFee = deliveryFee;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.timestamp = timestamp;
        this.accountType = accountType;
        this.online = online;
        this.shopOpen = shopOpen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(String deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(String shopOpen) {
        this.shopOpen = shopOpen;
    }
}



