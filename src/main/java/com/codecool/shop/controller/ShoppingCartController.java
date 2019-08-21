package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
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

    Integer USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OrderDao orderDataStore = new OrderDaoJDBC();

        Order order = orderDataStore.getActualOrderByUser(USER_ID);
        Map<Product, Integer> lineItem = new HashMap<>();
        float total = 0;

        if (!(order == null) && order.getProductsNumber() == 0) {
            new OrderDaoJDBC().remove(order.getId());
        } else if (!(order == null)) {
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

