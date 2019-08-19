package com.codecool.shop.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/login"})
public class LoginUserController extends HttpServlet {
    // private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, String> responseData = new HashMap<>();


        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");

        if (checkLoginCredentials(username, password)) {
            responseData.put("success", "true");
            responseData.put("username", username);
            responseData.put("userid", getUserIdFromDB(username).toString());
            HttpSession session = req.getSession();
            session.setAttribute("USER_ID", getUserIdFromDB(username));
            session.setAttribute("USER_NAME", username);
        } else {
            responseData.put("success", "false");
            responseData.put("message", "Wrong username or password!");
        }


        responseData.put("type", "Login");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();
    }

    private Boolean checkLoginCredentials(String username, String password) {
        String storedPassword = getUserStoredPasswordFromDB(username);
        if (storedPassword == null)
            return false;
        return (BCrypt.checkpw(password, storedPassword));

    }

    private String getUserStoredPasswordFromDB(String username) {
        if (username.equals("aaaaaaaa"))
            return "$2a$10$dsZnAfEmXOPFK.CmB5QjCOmoFheKjCaDBJoIkNpi4bOOFYkjuFAeS";
        else if (username.equals("bbbbbbbb"))
            return "$2a$10$06VDqKsZQjZMDjW/v.p30.53BYiBQ2uHUMrd2ouUAt468vn6hes.e";
        else
            return null;
    }

    private Integer getUserIdFromDB(String username) {
        if (username.equals("aaaaaaaa"))
            return 999;
        else if (username.equals("bbbbbbbb"))
            return 990;
        else
            return -1;
    }
}