package com.codecool.shop.model;

import com.codecool.shop.Util;

import java.util.ArrayList;
import java.util.List;

public class ProductCategory extends BaseModel {
    private String department;
    private List<Product> products;

    public ProductCategory(Integer id, String name, String department, String description) {
        super(name, description);
        this.id = id;
        this.department = department;
        this.products = new ArrayList<>();
    }

    public static ProductCategory create(String name, String department, String description) {
        return new ProductCategory(Util.getNextIdFromTable("categories"), name, department, description);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public List<Product> getProducts() {
        return this.products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public String toString() {
        return String.format(
                "id: %1$d," +
                        "name: %2$s, " +
                        "department: %3$s, " +
                        "description: %4$s",
                this.id,
                this.name,
                this.department,
                this.description);
    }
}