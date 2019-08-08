package com.codecool.shop.userdata;

public class Address {
    private String country;
    private String city;
    private int zipCode;
    private String Address;


    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public int getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return Address;
    }
}
