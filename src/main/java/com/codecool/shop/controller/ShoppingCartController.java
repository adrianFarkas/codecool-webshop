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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/shopping-cart"})
public class ShoppingCartController extends HttpServlet {

    Integer USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        OrderDaoMem orderDataStore = OrderDaoMem.getInstance();

        Map<Product, Integer> lineItem = createLineItem(orderDataStore);

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("items", lineItem);

        engine.process("product/shopping-cart.html", context, resp.getWriter());
    }

    private Boolean isInLineItem(Product product, Map<Product, Integer> items)  {
        return items.containsKey(product);
    }

    private Map<Product, Integer> createLineItem(OrderDaoMem orderDataStore){
        Map<Product, Integer> lineItem = new HashMap<>();
        Order order = orderDataStore.getActualOrderByUser(USER_ID);
        if(order != null) {
           for (Product prod : order.getProducts()) {
                if (isInLineItem(prod, lineItem)) {
                    Integer productNum = lineItem.get(prod);
                    lineItem.put(prod, productNum + 1);
                } else {
                    lineItem.put(prod, 1);
                }
            }
        }
        return lineItem;
    }

}

