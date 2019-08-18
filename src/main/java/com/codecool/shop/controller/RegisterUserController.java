package com.codecool.shop.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/register"})
public class RegisterUserController extends HttpServlet {
 //   private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, String> responseData = new HashMap<>();

        String username = (String)requestData.get("username");
        String password = (String)requestData.get("password");
        String hashedPassword = BCrypt.hashpw(password,BCrypt.gensalt());


      /*  if ((username.equals("a") && (password.equals("a")))) {

            responseData.put("success", "true");
        }*/
       if (checkUsernameExists(username))
           responseData.put("success", "false");
        else {
            //save data to the database
           SessionController sessionController = SessionController.getInstance();
           sessionController.addAttributeToSession(req,SessionAttributeName.USER_ID, getUserIdFromDB(username));
           sessionController.addAttributeToSession(req,SessionAttributeName.USER_NAME,username);
           responseData.put("success", "true");
           responseData.put("username", username);
           responseData.put("userid", getUserIdFromDB(username).toString());

           //req.getSession().setAttribute("USER_ID", getUserIdFromDB(username));
           //req.getSession().setAttribute("username", username);
       }
        responseData.put("type","Registration");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();
        System.out.println("REgister");
    }

    //check in database the user name
    private Boolean checkUsernameExists(String username){
        return username.equals("c") || (username.equals("d"));
    }

    //get user id from database
    private Integer getUserIdFromDB(String username) {
        if (username.equals("a"))
            return 999;
        else if (username.equals("b"))
            return 990;
        else
            return -1;
    }
    }