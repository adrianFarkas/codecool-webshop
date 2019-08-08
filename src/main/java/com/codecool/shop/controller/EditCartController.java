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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/edit-cart"})
public class EditCartController extends HttpServlet {

    private int USER_ID = 2;


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map requestData = new Gson().fromJson(req.getReader(), Map.class);
        Map<String, Float> responseData = new HashMap<>();

        int prodId = Integer.parseInt((String) requestData.get("productId"));
        int num = Integer.parseInt((String) requestData.get("num"));


        Product product = ProductDaoMem.getInstance().find(prodId);
        Order order = OrderDaoMem.getInstance().createOrder(USER_ID);

        Map<Product, Integer> products = order.getProductsPartitionByNum();
        int productNum = products.get(product);
        int newItemsNum = Math.abs(num - productNum);

        if (productNum < num) order.addMultipleItem(product, newItemsNum);
        else if (productNum > num) order.removeMultiple(product, newItemsNum);


        responseData.put("prodTotalPrice", order.getTotalPriceForProduct(product));
        responseData.put("totalPrice", order.getTotalPrice());

        PrintWriter out = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        out.write(new Gson().toJson(responseData));
        out.flush();

    }
}
