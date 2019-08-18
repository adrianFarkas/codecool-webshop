package com.codecool.shop.controller;

public enum SessionAttributeName {

        USER_ID ("USER_ID"),
        USER_NAME ("USER_NAME");

        String attribute;

    SessionAttributeName(String attribute){
            this.attribute = attribute;
        }

        public String getAttribute(){
            return attribute;
        }


    }

