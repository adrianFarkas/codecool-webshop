package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("category", productCategoryDataStore.getAll());
   //     context.setVariable("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        context.setVariable("products", filterProducts(req.getParameter("categories"), req.getParameter("suppliers")));
        context.setVariable(("suppliers"), supplierDaoMem.getAll());
        context.setVariable("filterCatId", req.getParameter("categories"));
        context.setVariable("filterSupptId", req.getParameter("suppliers"));
        // // Alternative setting of the template context
        // Map<String, Object> params = new HashMap<>();
        // params.put("category", productCategoryDataStore.find(1));
        // params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
        // context.setVariables(params);

        System.out.println(req.getParameter("categories"));
        engine.process("product/index.html", context, resp.getWriter());
    }

    private List<Product> filterProducts(String catId, String suppId){

        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDaoMem supplierDaoMem = SupplierDaoMem.getInstance();
        List<Product> temp = productDataStore.getAll();
        if ((catId==null) || catId.equals("-1")){
          //  return productDataStore.getAll();
            temp = productDataStore.getAll();
        }else
            temp = productDataStore.getBy(productCategoryDataStore.find(Integer.valueOf(catId)));
           // return productDataStore.getBy(productCategoryDataStore.find(Integer.valueOf(catId)));

        if ((suppId==null) || (suppId.equals("-1")))
            return temp;
        return temp.stream().filter(t -> t.getSupplier().equals(supplierDaoMem.find(Integer.valueOf(suppId)))).collect(Collectors.toList());
    }

}
