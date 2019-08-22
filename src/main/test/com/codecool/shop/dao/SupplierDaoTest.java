package com.codecool.shop.dao;

import com.codecool.shop.Util;
import com.codecool.shop.dao.implementation.SupplierDaoJDBC;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SupplierDaoTest {
    private SupplierDaoJDBC supplierDaoDataStore =  new SupplierDaoJDBC();

    @BeforeAll
    static void setup(){
        DataBaseHandler.setDatabase("test.properties");
    }

    @Test
    @Order(1)
    void testAddSupplierToTable() {
        int id = Util.getNextIdFromTable("suppliers");
        Supplier supplier = new Supplier(id, "Supplier name", "Supplier description");
        supplierDaoDataStore.add(supplier);
        assertNotNull(supplierDaoDataStore.find(id));

    }

    @Test
    @Order(2)
    void testFindSupplierById() {
        int id = Util.getNextIdFromTable("suppliers") - 1;
        assertNotNull(supplierDaoDataStore.find(id));

    }

    @Test
    @Order(3)
    void testSupplierNotFounded(){
        assertNull(supplierDaoDataStore.find(-1));
    }

    @Test
    @Order(4)
    void testRemoveSupplierById() {
        int id = Util.getNextIdFromTable("suppliers") - 1;
        supplierDaoDataStore.remove(id);
        assertNull(supplierDaoDataStore.find(id));
    }

    @Test
    @Order(5)
    void testGetAllSuppliers() {
        assertEquals(4, supplierDaoDataStore.getAll().size());

    }
}