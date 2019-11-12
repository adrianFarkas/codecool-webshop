package com.codecool.shop.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.codecool.shop.Util;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.*;

import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductDaoTest {
    private ProductDao productDataStore = new ProductDaoJDBC();

    private ProductCategory tablet = new ProductCategory(1, "Tablet", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
    private ProductCategory phone = new ProductCategory(3, "Phone", "Hardware", "A mobile phone, cell phone, cellphone, or hand phone, sometimes shortened to simply mobile, cell or just phone, is a portable telephone that can make and receive calls over a radio frequency link while the user is moving within a telephone service area.");
    private Supplier amazon = new Supplier(1, "Amazon", "Digital content and services");


    @BeforeAll
    public static void init() {
        DataBaseHandler.setDatabase("test.properties");
    }

    @Test
    @Order(1)
    public void testFindProductById() {
        Product result = productDataStore.find(1);
        assertEquals("Amazon Fire", result.getName());
    }

    @Test
    @Order(2)
    public void testProductNotFounded() {
        Product result = productDataStore.find(-1);
        assertNull(result);
    }

    @Test
    @Order(3)
    public void testProductAddedToTable() {
        int id = Util.getNextIdFromTable("products");
        productDataStore.add(new Product(id, "Huawei P30", 500, "USD", "Good", phone, amazon));
        Product result = productDataStore.find(id);
        assertEquals("Huawei P30", result.getName());
    }

    @Test
    @Order(4)
    public void testRemoveProductById() {
        int id = Util.getNextIdFromTable("products")-1;
        productDataStore.remove(id);
        assertNull(productDataStore.find(id));
    }

    @Test
    @Order(5)
    public void testGetAllProducts() {
        List<Product> products = productDataStore.getAll();
        assertEquals(8, products.size());
    }

    @Test
    @Order(6)
    public void testGetProductsBySupplier() {
        List<Product> products = productDataStore.getBy(amazon);
        assertEquals(2, products.size());
    }

    @Test
    @Order(7)
    public void testGetProductsByCategory() {
        List<Product> products = productDataStore.getBy(tablet);
        assertEquals(3, products.size());
    }
}