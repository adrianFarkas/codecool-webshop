package com.codecool.shop.controller;

import com.codecool.shop.Util;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.AddressDaoJDBC;
import com.codecool.shop.dao.implementation.CustomerDetailsDaoJDBC;

import com.codecool.shop.userdata.Address;
import com.codecool.shop.userdata.AddressType;
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

@WebServlet(urlPatterns = {"/edit-details"})
public class UserEditDetailsController extends HttpServlet {
    private int USER_ID = 2;
    private CustomerDetailsDaoJDBC customerDetailStore = new CustomerDetailsDaoJDBC();
    private AddressDaoJDBC addressDataStore = new AddressDaoJDBC();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Userdata userdata = customerDetailStore.find(USER_ID);
        boolean isSameAddress = true;
        if(userdata == null) userdata = new Userdata();
        else isSameAddress = checkSameAddresses(userdata.getBillingAddress(), userdata.getShippingAddress());

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("userDetails", userdata);
        context.setVariable("isSameAddress", isSameAddress);
        engine.process("/product/edit_personal_details.html", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Userdata userdata = customerDetailStore.find(USER_ID);
        if (userdata == null) addNewPersonalDetails(req);
        else updatePersonalDetails(req, userdata);
        resp.sendRedirect("/");
    }

    private void updatePersonalDetails(HttpServletRequest req, Userdata userdata) {
        Address billing = userdata.getBillingAddress();
        Address shipping = userdata.getShippingAddress();

        userdata.setAttributes(
                req.getParameter("name"),
                req.getParameter("email_address"),
                req.getParameter("phone_number")
        );

        billing.setAll(
            req.getParameter("billing-city"),
            req.getParameter("billing-country"),
            Integer.parseInt(req.getParameter("billing-zipcode")),
            req.getParameter("billing-address")
        );
        if (req.getParameter("cb") != null) {
            shipping.setAll(
                    billing.getCity(),
                    billing.getCountry(),
                    billing.getZipCode(),
                    billing.getAddress()
            );
        } else {
            shipping.setAll(
                    req.getParameter("shipping-city"),
                    req.getParameter("shipping-country"),
                    Integer.parseInt(req.getParameter("shipping-zipcode")),
                    req.getParameter("shipping-address")
            );
        }


        customerDetailStore.update(userdata);
    }

    private void addNewPersonalDetails(HttpServletRequest req) {
        Userdata newUserDetails;
        Address shippingAddress;
        Address billingAddress = createBillingAddress(req);
        addressDataStore.add(billingAddress);

        if (req.getParameter("cb") != null) {
            shippingAddress =  new Address();
            shippingAddress.setAll(
                    billingAddress.getCity(),
                    billingAddress.getCountry(),
                    billingAddress.getZipCode(),
                    billingAddress.getAddress()
            );
            shippingAddress.setType(AddressType.SHIPPING);
        } else {
            shippingAddress = createShippingAddress(req);
        }
        newUserDetails = new Userdata(
                Util.getNextIdFromTable("customers_information"),
                null,
                req.getParameter("name"),
                req.getParameter("email_address"),
                req.getParameter("phone_number"),
                billingAddress,
                shippingAddress
        );

        addressDataStore.add(shippingAddress);
        customerDetailStore.add(newUserDetails, USER_ID);
    }

    private Address createBillingAddress(HttpServletRequest req) {
        return new Address(
                Util.getNextIdFromTable("addresses"),
                req.getParameter("billing-country"),
                req.getParameter("billing-city"),
                Integer.parseInt(req.getParameter("billing-zipcode")),
                req.getParameter("billing-address"),
                AddressType.BILLING
        );
    }

    private Address createShippingAddress(HttpServletRequest req) {
        return new Address(
                Util.getNextIdFromTable("addresses"),
                req.getParameter("shipping-country"),
                req.getParameter("shipping-city"),
                Integer.parseInt(req.getParameter("shipping-zipcode")),
                req.getParameter("shipping-address"),
                AddressType.SHIPPING
        );
    }

    private boolean checkSameAddresses(Address billingAddress, Address shippingAddress){
        return  (billingAddress != null  &&  shippingAddress != null  &&
                Objects.equals(billingAddress.getCity(), shippingAddress.getCity())  &&
                Objects.equals(billingAddress.getCountry(), shippingAddress.getCountry())  &&
                Objects.equals(billingAddress.getZipCode(), shippingAddress.getZipCode()))  &&
                Objects.equals(billingAddress.getAddress(), shippingAddress.getAddress());
    }
}