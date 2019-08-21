package com.codecool.shop.controller;

import javax.servlet.http.HttpServletRequest;

public class SessionController {
    private static SessionController instance;

    public static SessionController getInstance(){
        if (instance==null) instance=new SessionController();
        return instance;
    }

    String readStringAttributeFromSession(HttpServletRequest req, String attribute){
        String value = String.valueOf(req.getSession().getAttribute(attribute));
        if (value.equals("null"))
            return "";
        return value;
    }


    Integer readIntegerAttributeFromSession(HttpServletRequest req, String attribute){
        String value = String.valueOf(req.getSession().getAttribute(attribute));
        if (value.equals("null"))
            return -1;
        return Integer.parseInt(value);

    }

    private  SessionController(){

    }



}
