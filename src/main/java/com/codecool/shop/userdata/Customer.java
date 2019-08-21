package com.codecool.shop.userdata;

import com.codecool.shop.Util;

public class Customer {
    private Integer id;
    private String userName;

    public Customer(Integer id, String userName, String password, String email) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    private String password;
    private String email;



}
