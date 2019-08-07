package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.userdata.Address;
import com.codecool.shop.userdata.Userdata;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
   private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        engine.process("checkout.html", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = OrderDaoMem.getInstance().getActualOrderByUser(USER_ID);
        Userdata userdata = new Userdata();
        userdata.setName(req.getParameter("name"));
        userdata.setEmail(req.getParameter("email"));
        userdata.setPhoneNumber(req.getParameter("phone"));
        Address address = new Address();
        address.setAddress(req.getParameter("billing-country"));
        address.setCity(req.getParameter("billing-city"));
        address.setCountry(req.getParameter("billing-country"));
        address.setZipCode(Integer.parseInt(req.getParameter("billing-zipcode")));

        userdata.setBillingAddress(address);

        address.setAddress(req.getParameter("shipping-country"));
        address.setCity(req.getParameter("shipping-city"));
        address.setCountry(req.getParameter("shipping-country"));
        address.setZipCode(Integer.parseInt(req.getParameter("shipping-zipcode")));

        userdata.setShippingAddress(address);

        order.setUserdata(userdata);

    }
}