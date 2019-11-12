package com.codecool.shop.userdata;

import com.codecool.shop.Util;

public class Userdata {

    private Integer id;
    private Integer orderId;
    private String name;
    private String email;
    private String phoneNumber;

    private Address billingAddress;
    private Address shippingAddress;

    public Userdata() {
        this(null,null,null,null,null, new Address(), new Address());
    }

    public Userdata(Integer id, Integer orderId, String name, String email, String phoneNumber, Address billingAddress, Address shippingAddress) {
        this.id = id;
        this.orderId = orderId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }
    public static Userdata create(Integer orderId) {
        return new Userdata(Util.getNextIdFromTable("delivery_information"), orderId,
                null, null, null, Address.create(AddressType.BILLING), Address.create(AddressType.SHIPPING));
    }

    public Integer getId() {
        return id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setAttributes(String name, String email, String phoneNumber){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}
