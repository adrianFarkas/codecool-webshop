package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
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
    //private Integer USER_ID;
    private int cartSize;
    private int filterSuppId = -1;
    private int filterCatId = -1;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();

        setCartSize(getUserId(req));

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("category", productCategoryDataStore.getAll());
        context.setVariable("products", filterProducts(productCategoryDataStore, productDataStore, supplierDaoMem));
        context.setVariable(("suppliers"), supplierDaoMem.getAll());
        context.setVariable("filterCatId", req.getParameter("categories"));
        context.setVariable("filterSupptId", req.getParameter("suppliers"));
        context.setVariable("cartSize", cartSize);
        context.setVariable("USER_ID", getUserId(req));
        // // Alternative setting of the template context
        // Map<String, Object> params = new HashMap<>();
        // params.put("category", productCategoryDataStore.find(1));
        // params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        // context.setVariables(params);
        engine.process("product/index.html", context, resp.getWriter());
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setFilterID(req);
        doGet(req,resp);
    }

    private void setCartSize(Integer USER_ID) {
        Order cart = null;

        if (USER_ID != null) cart = OrderDaoMem.getInstance().getActualOrderByUser(USER_ID);

        if(cart != null) cartSize = cart.getProductsNumber();
        else cartSize = 0;
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

    private List<Product> filterProducts(ProductCategoryDao productCategoryDataStore, ProductDao productDataStore, SupplierDaoMem supplierDaoMem){

        List<Product> filteredList;
        if (filterCatId==-1){
            filteredList = productDataStore.getAll();
        }else
            filteredList = productDataStore.getBy(productCategoryDataStore.find(filterCatId));
        if (filterSuppId==-1)
            return filteredList;
        return filteredList.stream().filter(t -> t.getSupplier().equals(supplierDaoMem.find(filterSuppId))).collect(Collectors.toList());
    }

    private Integer getUserId(HttpServletRequest req){
        Integer userID=SessionController.getInstance().readIntegerAttributeFromSession(req, "USER_ID");
        if (userID==null)
            return -1;
        return userID;

    }
}
