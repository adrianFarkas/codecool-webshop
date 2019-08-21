package com.codecool.shop.config;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
//        ProductDao productDataStore = new ProductDaoJDBC();
//        ProductCategoryDao productCategoryDataStore = new ProductCategoryDaoJDBC();
//        SupplierDao supplierDataStore = new SupplierDaoJDBC();
//
//        //setting up a new supplier
//        Supplier amazon = Supplier.create("Amazon", "Digital content and services");
//        supplierDataStore.add(amazon);
//        Supplier lenovo = Supplier.create("Lenovo", "Computers");
//        supplierDataStore.add(lenovo);
//        Supplier asus = Supplier.create("Asus", "Computers");
//        supplierDataStore.add(asus);
//        Supplier apple = Supplier.create("Apple", "Hardware products");
//        supplierDataStore.add(apple);
//
//        //setting up a new product category
//        ProductCategory tablet = ProductCategory.create("Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
//        productCategoryDataStore.add(tablet);
//        ProductCategory laptop = ProductCategory.create("Laptop", "Hardware", "A laptop computer is a small, portable personal computer (PC), typically having a thin LCD or LED computer screen mounted on the inside of the upper lid and an alphanumeric keyboard on the inside of the lower lid.");
//        productCategoryDataStore.add(laptop);
//        ProductCategory phone = ProductCategory.create("Phone", "Hardware", "A mobile phone, cell phone, cellphone, or hand phone, sometimes shortened to simply mobile, cell or just phone, is a portable telephone that can make and receive calls over a radio frequency link while the user is moving within a telephone service area.");
//        productCategoryDataStore.add(phone);
//        ProductCategory desktop = ProductCategory.create("Desktop", "Hardware", "A desktop computer is a personal computer designed for regular use at a single location on or near a desk or table due to its size and power requirements. ");
//        productCategoryDataStore.add(desktop);
//
//        //setting up products and printing it
//        productDataStore.add(Product.create("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
//        productDataStore.add(Product.create("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
//        productDataStore.add(Product.create("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
//        productDataStore.add(Product.create("Asus ROG Zephyrus", 1499, "USD", "ROG Zephyrus is a revolutionary gaming laptop born from ROG’s persistent dedication to innovation.", laptop, asus));
//        productDataStore.add(Product.create("Apple iPhone X", 1189.9f, "USD", "All-screen design. Longest battery life ever in an iPhone. Fastest performance Studio-quality photos.", phone, apple));
//        productDataStore.add(Product.create("ROG Phone", 649.9f, "USD", "Epic performance. Unbeatable visuals. Total control.", phone, asus));
//        productDataStore.add(Product.create("Mac Pro (Standard)", 4999, "USD", "Power to change everything. Say hello to a Mac that is extreme in every way.", desktop, apple));
//        productDataStore.add(Product.create("Mac Pro (Top-end)", 44999, "USD", "Power to change everything. Say hello to a Mac that is extreme in every way.", desktop, apple));
    }
}
