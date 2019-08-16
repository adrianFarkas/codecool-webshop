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

@WebServlet(urlPatterns = {"/login"})
public class LoginUserController extends HttpServlet {
    private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, String> responseData = new HashMap<>();

        String username = (String)requestData.get("user-name");
        String password = (String)requestData.get("password");

        if ((username.equals("a") && (password.equals("a")))) {
            responseData.put("success", "true");
            responseData.put("username", "a");
            responseData.put("userid", "999");
        }
        else
            responseData.put("success", "false");
        responseData.put("type","Login");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();
    }
}