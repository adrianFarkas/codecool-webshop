package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.google.gson.Gson;

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

@WebServlet(urlPatterns = {"/add-to-cart"})
public class AddToCartController extends HttpServlet {

    //private int USER_ID = 2;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, Integer> responseData = new HashMap<>();

        int productId = Integer.parseInt((String) requestData.get("productId"));
        Product product = ProductDaoMem.getInstance().find(productId);
        Order order = OrderDaoMem.getInstance().createOrder(SessionController.getInstance().readIntegerAttributeFromSession(req, SessionAttributeName.USER_ID));
        order.addItem(product);

        responseData.put("cartSize", order.getProductsNumber());
        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();

    }
}
