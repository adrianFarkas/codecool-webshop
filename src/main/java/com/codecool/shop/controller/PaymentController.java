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

@WebServlet(urlPatterns = {"/payment"})
public class PaymentController extends HttpServlet {
   // private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDaoMem orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getActualOrderByUser(SessionController.getInstance().readIntegerAttributeFromSession(req, SessionAttributeName.USER_ID.getAttribute()));
        if (order!=null) {
            Map<Product, Integer> lineItem = new HashMap<>();
            float total = 0;
            lineItem = order.getProductsPartitionByNum();
            total = order.getTotalPrice();

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());
            context.setVariable("items", lineItem);
            context.setVariable("total", total);
            engine.process("product/payment.html", context, resp.getWriter());
        }else
        {
            resp.sendRedirect("/");
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        OrderDaoMem orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getActualOrderByUser(SessionController.getInstance().readIntegerAttributeFromSession(req, SessionAttributeName.USER_ID.getAttribute()));
        order.pay();
        resp.sendRedirect("/");
    }


}