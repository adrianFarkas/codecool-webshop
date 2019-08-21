package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    private int filterSuppId = -1;
    private int filterCatId = -1;
    private ProductDao productDataStore = new ProductDaoJDBC();
    private ProductCategoryDao productCategoryDataStore = new ProductCategoryDaoJDBC();
    private SupplierDao supplierDaoMem = new SupplierDaoJDBC();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int USER_ID = SessionController.getInstance().readIntegerAttributeFromSession(req, "USER_ID");

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("products", filterProducts());
        context.setVariable(("suppliers"), supplierDaoMem.getAll());
        context.setVariable("filterCatId", req.getParameter("categories"));
        context.setVariable("filterSupptId", req.getParameter("suppliers"));
        context.setVariable("cartSize", getCartSize(USER_ID));
        context.setVariable("USER_ID", USER_ID);

        engine.process("product/index.html", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setFilterID(req);
        doGet(req,resp);
    }

    private int getCartSize(Integer USER_ID) {
        Order cart = null;

        if (USER_ID != null) cart = new OrderDaoJDBC().getActualOrderByUser(USER_ID);

        if(cart != null) return cart.getProductsNumber();
        return 0;
    }

    private void setFilterID(HttpServletRequest req){
        try {

            filterCatId = req.getParameter("categories") == null ? -1 : Integer.parseInt(req.getParameter("categories"));
        }
        catch (Exception e) { filterCatId = -1; }

        try {
            filterSuppId = req.getParameter("suppliers") == null ? -1 : Integer.parseInt(req.getParameter("suppliers"));
        }
        catch (Exception e) { filterSuppId = -1; }

    }

    private List<Product> filterProducts(){

        List<Product> filteredList;
        if (filterCatId==-1){
            filteredList = productDataStore.getAll();
        }else
            filteredList = productDataStore.getBy(productCategoryDataStore.find(filterCatId));
        if (filterSuppId==-1)
            return filteredList;
        return filteredList.stream().filter(t -> t.getSupplier().toString()
                .equals(supplierDaoMem.find(filterSuppId).toString())).collect(Collectors.toList());
    }
}
