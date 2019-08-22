package com.codecool.shop.dao;

import com.codecool.shop.database.DataBaseHandler;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductCategoryDaoTest {

    @BeforeAll
    private void setup(){
        DataBaseHandler.setDatabase("test.properties");
    }

    @Test
    public void testAddCategoryToTable() {

    }

    @Test
    public void testFindCategoryById() {

    }

    @Test
    public void testRemoveCategoryById() {

    }

    @Test
    public void testGetAllCategories() {

    }

}