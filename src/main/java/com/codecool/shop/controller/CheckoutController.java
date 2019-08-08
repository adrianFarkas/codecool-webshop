package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.OrderDaoMem;
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
        Order order = OrderDaoMem.getInstance().getActualOrderByUser(USER_ID);
        if (order!= null) {
            WebContext context = new WebContext(req, resp, req.getServletContext());
            Userdata userdata = order.getUserdata();
            if (userdata==null) {
                userdata = new Userdata();
                userdata.setBillingAddress(new Address());
                userdata.setShippingAddress(new Address());
                order.setUserdata(userdata);
            }
            context.setVariable("userdata",userdata);
            engine.process("/product/checkout.html", context, resp.getWriter());
        } else
            resp.sendRedirect("/");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = OrderDaoMem.getInstance().getActualOrderByUser(USER_ID);
        if (order != null) {
            insertUserDataIntoOrder(req, order);
            resp.sendRedirect("/payment");
        } else
            resp.sendRedirect("/");

    }

    private void insertUserDataIntoOrder(HttpServletRequest req, Order order) {
      //  Userdata userdata = new Userdata();
        Userdata userdata = order.getUserdata();
        userdata.setName(req.getParameter("name"));
        userdata.setEmail(req.getParameter("email_address"));
        userdata.setPhoneNumber(req.getParameter("phone_number"));

        Address billingAddress = userdata.getBillingAddress();
        billingAddress.setAddress(req.getParameter("billing-country"));
        billingAddress.setCity(req.getParameter("billing-city"));
        billingAddress.setCountry(req.getParameter("billing-country"));
        billingAddress.setZipCode(Integer.parseInt(req.getParameter("billing-zipcode")));

        userdata.setBillingAddress(billingAddress);
        if (req.getParameter("cb") != null) {
            userdata.setShippingAddress(billingAddress);
        } else {
            Address shippingAddress = userdata.getShippingAddress();
            shippingAddress.setAddress(req.getParameter("shipping-country"));
            shippingAddress.setCity(req.getParameter("shipping-city"));
            shippingAddress.setCountry(req.getParameter("shipping-country"));
            shippingAddress.setZipCode(Integer.parseInt(req.getParameter("shipping-zipcode")));

            userdata.setShippingAddress(shippingAddress);
        }
        order.setUserdata(userdata);
        order.checkout();
    }

}