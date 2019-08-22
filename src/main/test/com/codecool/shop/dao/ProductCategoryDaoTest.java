package com.codecool.shop.dao;

import com.codecool.shop.Util;
import com.codecool.shop.dao.implementation.ProductCategoryDaoJDBC;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.ProductCategory;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductCategoryDaoTest {
    private ProductCategoryDaoJDBC productCategoryDataStore = new ProductCategoryDaoJDBC();

    @BeforeAll
    private static void init() {
        DataBaseHandler.setDatabase("test.properties");
    }

    @Test
    @Order(1)
    void testAddCategoryToTable() {
        int id = Util.getNextIdFromTable("categories");
        ProductCategory productCategory = new ProductCategory(id, "Test category", "Testing", "Testing");
        productCategoryDataStore.add(productCategory);
        assertNotNull(productCategoryDataStore.find(id));
    }

    @Test
    @Order(2)
    void testFindCategoryById() {
        int id = Util.getNextIdFromTable("categories") - 1;
        assertNotNull(productCategoryDataStore.find(id));
    }

    @Test
    @Order(4)
    void testCategoryNotFounded() {
        assertNull(productCategoryDataStore.find(-1));
    }

    @Test
    @Order(5)
    void testRemoveCategoryById() {
        int id = Util.getNextIdFromTable("categories") - 1;
        productCategoryDataStore.remove(id);
        assertNull(productCategoryDataStore.find(id));
    }

    @Test
    @Order(6)
    void testGetAllCategories() {
        assertEquals(4, productCategoryDataStore.getAll().size());

    }

}