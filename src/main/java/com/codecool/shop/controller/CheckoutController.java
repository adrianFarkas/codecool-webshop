package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.CustomerDetailsDaoJDBC;
import com.codecool.shop.dao.implementation.OrderDaoJDBC;
import com.codecool.shop.dao.implementation.DeliveryDetailsDaoJDBC;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Status;
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
import java.util.Objects;

@WebServlet(urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    private OrderDao orderDataStore = new OrderDaoJDBC();
    private DeliveryDetailsDaoJDBC deliveryDetailsStore = new DeliveryDetailsDaoJDBC();
    private CustomerDetailsDaoJDBC customerDetailsStore = new CustomerDetailsDaoJDBC();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int USER_ID = SessionController.getInstance().readIntegerAttributeFromSession(req, "USER_ID");
        Order order = orderDataStore.getActualOrderByUser(USER_ID);
        Userdata userdata;

        if (order != null) {
            if(order.getStatus().equals(Status.CHECKED)) userdata = deliveryDetailsStore.find(order.getId());
            else userdata = customerDetailsStore.find(USER_ID);

            TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
            WebContext context = new WebContext(req, resp, req.getServletContext());
            context.setVariable("sameAddress",checkSameAddresses(userdata.getBillingAddress(), userdata.getShippingAddress()));
            context.setVariable("userdata", userdata);
            engine.process("/product/checkout.html", context, resp.getWriter());
        } else
            resp.sendRedirect("/");
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int USER_ID = SessionController.getInstance().readIntegerAttributeFromSession(req, "USER_ID");
        Order order = orderDataStore.getActualOrderByUser(USER_ID);
        if (order != null) {
            insertUserDataIntoOrder(req, order);
            resp.sendRedirect("/payment");
        } else
            resp.sendRedirect("/");

    }

    private void insertUserDataIntoOrder(HttpServletRequest req, Order order) {
        Userdata userdata = deliveryDetailsStore.find(order.getId());
        userdata.setAttributes(req.getParameter("name"), req.getParameter("email_address"), req.getParameter("phone_number"));

        Address billingAddress = userdata.getBillingAddress();
        billingAddress.setAll(req.getParameter("billing-city"), req.getParameter("billing-country"),
                Integer.parseInt(req.getParameter("billing-zipcode")), req.getParameter("billing-address"));

        if (req.getParameter("cb") != null) {
            Address shippingAddress = userdata.getShippingAddress();
            shippingAddress.setAll(billingAddress.getCity(),billingAddress.getCountry(), billingAddress.getZipCode(), billingAddress.getAddress());
        } else {
            Address shippingAddress = userdata.getShippingAddress();
            shippingAddress.setAll(req.getParameter("shipping-city"), req.getParameter("shipping-country"),
                    Integer.parseInt(req.getParameter("shipping-zipcode")), req.getParameter("shipping-address"));
        }

        deliveryDetailsStore.update(userdata);
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