package com.codecool.shop.userdata;

public class Address {
    private String country;
    private String city;
    private int zipCode;
    private String address;
    public Address(){
        this("","",0,"");
    }

    public Address(String country, String city, int zipCode, String address) {
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.address = address;
    }

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
        this.address = address;
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
        return address;
    }

    public void setAll(String city, String country, int zipCode, String address){
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
        this.address = address;
    }
}
