package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.Order;
import com.codecool.shop.userdata.Address;
import com.codecool.shop.userdata.Userdata;
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {
  //  private int USER_ID = 2;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        Order order = OrderDaoMem.getInstance().getActualOrderByUser(SessionController.getInstance().readIntegerAttributeFromSession(req, SessionAttributeName.USER_ID));
        if (order != null) {
            WebContext context = new WebContext(req, resp, req.getServletContext());
            Userdata userdata = order.getUserdata();
            if (userdata == null) {
                userdata = new Userdata();
                userdata.setBillingAddress(new Address());
                userdata.setShippingAddress(new Address());
                order.setUserdata(userdata);
            }
            context.setVariable("sameAddress",checkSameAddresses(userdata.getBillingAddress(), userdata.getShippingAddress()));
            context.setVariable("userdata", userdata);
            engine.process("/product/checkout.html", context, resp.getWriter());
        } else
            resp.sendRedirect("/");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = OrderDaoMem.getInstance().getActualOrderByUser(SessionController.getInstance().readIntegerAttributeFromSession(req, SessionAttributeName.USER_ID));
        if (order != null) {
            insertUserDataIntoOrder(req, order);
            resp.sendRedirect("/payment");
        } else
            resp.sendRedirect("/");

    }

    private void insertUserDataIntoOrder(HttpServletRequest req, Order order) {
        Userdata userdata = order.getUserdata();
        userdata.setAttributes(req.getParameter("name"), req.getParameter("email_address"), req.getParameter("phone_number"));

        Address billingAddress = userdata.getBillingAddress();
        billingAddress.setAll(req.getParameter("billing-city"), req.getParameter("billing-country"),
                Integer.parseInt(req.getParameter("billing-zipcode")), req.getParameter("billing-address"));

        if (req.getParameter("cb") != null) {
            userdata.getShippingAddress().setAll(billingAddress.getCity(),billingAddress.getCountry(), billingAddress.getZipCode(), billingAddress.getAddress());
        } else {
            Address shippingAddress = userdata.getShippingAddress();
            shippingAddress.setAll(req.getParameter("shipping-city"), req.getParameter("shipping-country"),
                    Integer.parseInt(req.getParameter("shipping-zipcode")), req.getParameter("shipping-address"));
        }
        order.setUserdata(userdata);
        order.checkout();
    }

    private boolean checkSameAddresses(Address billingAddress, Address shippingAddress){
        return  (billingAddress != null  &&  shippingAddress != null  &&
                Objects.equals(billingAddress.getCity(), shippingAddress.getCity())  &&
                Objects.equals(billingAddress.getCountry(), shippingAddress.getCountry())  &&
                Objects.equals(billingAddress.getZipCode(), shippingAddress.getZipCode()))  &&
                Objects.equals(billingAddress.getAddress(), shippingAddress.getAddress());
    }

}