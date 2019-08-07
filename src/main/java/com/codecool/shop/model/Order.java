package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

public class Order implements Orderable {

    private List<Product> products = new ArrayList<>();
    private Integer userId;
    private int id;
    private static int ID_COUNTER = 0;

    private Status status = Status.NEW;

    public Order() {
        this(null);
    }

    public Order(Integer userId) {
        this.userId = userId;
        this.id = ID_COUNTER++;
    }

    public void addItem(Product product) {
        products.add(product);
    }

    public Integer getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public int getProductsNumber() {
        return products.size();
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public boolean checkout() {
        if (status.equals(Status.NEW)) {
            status = Status.CHECKED;
            return true;
        }
        return false;
    }

    @Override
    public boolean pay() {
        if (status.equals(Status.CHECKED)) {
            status = Status.PAID;
            return true;
        }
        return false;
    }

    public Status getStatus() {
        return status;
    }

}
