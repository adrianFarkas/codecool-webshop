package com.codecool.shop.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class SessionController {
    static SessionController instance;

    public static SessionController getInstance(){
        if (instance==null) instance=new SessionController();
        return instance;
    }

    Boolean addAttributeToSession(HttpServletRequest req, String attibute, String value){
        if (attibute!=null){
            req.getSession().setAttribute(attibute, value);
            return true;
        }
        return false;
    }

    Boolean addAttributeToSession(HttpServletRequest req, String attibute, Integer value){
        if (attibute!=null){
            req.getSession().setAttribute(attibute, value);
            return true;
        }
        return false;
    }


    Boolean addAttributeToSession(HttpServletRequest req, SessionAttributeName attibute, String value){
        if (attibute!=null){
            req.getSession().setAttribute(attibute.getAttribute(), value);
            return true;
        }
        return false;
    }

    Boolean addAttributeToSession(HttpServletRequest req, SessionAttributeName attibute, Integer value){
        if (attibute!=null){
            req.getSession().setAttribute(attibute.getAttribute(), value);
            return true;
        }
        return false;
    }


    String readStringAttributeFromSession(HttpServletRequest req, String attribute){
        String value = String.valueOf(req.getSession().getAttribute(attribute));
        if (value.equals("null"))
            return "";
        return value;
    }

    String readStringAttributeFromSession(HttpServletRequest req, SessionAttributeName attribute){
        String value = String.valueOf(req.getSession().getAttribute(attribute.getAttribute()));
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

    Integer readIntegerAttributeFromSession(HttpServletRequest req, SessionAttributeName attribute){
        String value = String.valueOf(req.getSession().getAttribute(attribute.getAttribute()));
        if (value.equals("null"))
            return -1;
        return Integer.parseInt(value);

    }

    private  SessionController(){

    }



}
