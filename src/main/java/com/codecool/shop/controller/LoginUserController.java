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

import com.codecool.shop.dao.implementation.CustomerDaoJDBC;
import com.codecool.shop.userdata.Customer;
import com.google.gson.Gson;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(urlPatterns = {"/login"})
public class LoginUserController extends HttpServlet {

    CustomerDaoJDBC customerDataStore = new CustomerDaoJDBC();

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, String> responseData = new HashMap<>();

        String username = (String) requestData.get("username");
        String password = (String) requestData.get("password");
        Customer customer = customerDataStore.find(username);

        if (BCrypt.checkpw(password,customer.getPassword())) {
            responseData.put("success", "true");
            responseData.put("username", username);
            HttpSession session = req.getSession();
            session.setAttribute("USER_ID", customer.getId());
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

}

