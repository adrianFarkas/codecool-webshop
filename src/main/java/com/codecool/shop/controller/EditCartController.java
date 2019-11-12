package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/edit-cart"})
public class EditCartController extends HttpServlet {

    private OrderDao orderDataStore = new OrderDaoJDBC();
    private ProductDao productDataStore = new ProductDaoJDBC();


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int USER_ID = SessionController.getInstance().readIntegerAttributeFromSession(req, "USER_ID");
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, Float> responseData = new HashMap<>();

        int prodId = Integer.parseInt((String) requestData.get("productId"));
        int newQuantity = Integer.parseInt((String) requestData.get("newQuantity"));
        int actQuantity = Integer.parseInt((String) requestData.get("actQuantity"));

        Product product = productDataStore.find(prodId);
        Order order = orderDataStore.getActualOrderByUser(USER_ID);

        int newItemsNum = Math.abs(newQuantity - actQuantity);

        if (actQuantity < newQuantity) order.addMultipleItem(product, newItemsNum);
        else if (actQuantity > newQuantity) order.removeMultiple(product, newItemsNum);

        responseData.put("prodTotalPrice", order.getTotalPriceForProduct(product));
        responseData.put("totalPrice", order.getTotalPrice());

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();

    }
}
