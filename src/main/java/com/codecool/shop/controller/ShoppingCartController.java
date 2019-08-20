package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCartController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OrderDaoMem orderDataStore = OrderDaoMem.getInstance();


        Order order = orderDataStore.getActualOrderByUser(SessionController.getInstance().readIntegerAttributeFromSession(req,"USER_ID"));
        Map<Product, Integer> lineItem = new HashMap<>();
        float total = 0;


        if(order != null) {
            lineItem = order.getProductsPartitionByNum();
            total = order.getTotalPrice();
        }


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("items", lineItem);
        context.setVariable("total", total);

        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }
}

