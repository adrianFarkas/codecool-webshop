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

        if ((username.equals("abcd") && (BCrypt.checkpw(password,"$2a$10$dsZnAfEmXOPFK.CmB5QjCOmoFheKjCaDBJoIkNpi4bOOFYkjuFAeS")))) {
            responseData.put("success", "true");
            responseData.put("username", username);
            responseData.put("userid", "999");
        }
        else
            responseData.put("success", "false");
//Hash for password a : $2a$10$dsZnAfEmXOPFK.CmB5QjCOmoFheKjCaDBJoIkNpi4bOOFYkjuFAeS
    //    System.out.println(BCrypt.hashpw("a",BCrypt.gensalt()));


        responseData.put("type","Login");
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();
    }
}