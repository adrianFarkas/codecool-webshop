package com.codecool.shop.userdata;

import com.codecool.shop.Util;
import com.codecool.shop.dao.implementation.AddressDaoJDBC;

public class Address {

    private Integer id;
    private String country;
    private String city;
    private Integer zipCode;
    private String address;
    private AddressType type;

    public Address() {
        this(Util.getNextIdFromTable("addresses"), null, null, null, null, null);
    }

    public Address(Integer id, String country, String city, Integer zipCode, String address, AddressType type) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.zipCode = zipCode;
        this.address = address;
        this.type = type;
    }

    public static Address create(AddressType type) {
        Address newAddress = new Address(Util.getNextIdFromTable("addresses"), null, null, 0, null, type);
        new AddressDaoJDBC().add(newAddress);
        return newAddress;
    }

    public Integer getId() {
        return id;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public String getAddress() {
        return address;
    }

    public AddressType getType() {
        return type;
    }

    public void setAll(String city, String country, int zipCode, String address){
        this.city = city;
        this.country = country;
        this.zipCode = zipCode;
        this.address = address;
    }

    public void setType(AddressType type) {
        this.type = type;
    }
}
