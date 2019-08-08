package com.codecool.shop.model;

import com.codecool.shop.userdata.Userdata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Order implements Orderable {

    private List<Product> products = new ArrayList<>();
    private Integer userId;
    private int id;
    private Userdata userdata;
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

    public void addMultipleItem(Product product, int num) {
        for(int i = 0; i < num; i++) {
            addItem(product);
        }
    }

    public void remove(Product product) {
        products.remove(product);
    }

    public void removeMultiple(Product product, int num) {
        for(int i = 0; i < Math.abs(num); i++) {
            remove(product);

        }
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

    public HashMap<Product, Integer> getProductsPartitionByNum() {
        HashMap<Product, Integer> lineItem = new HashMap<>();

        for (Product prod : getProducts()) {
            if (lineItem.containsKey(prod)) {
                Integer productNum = lineItem.get(prod);
                lineItem.put(prod, productNum + 1);
            } else {
                lineItem.put(prod, 1);
            }
        }
        return lineItem;
    }

    public float getTotalPrice() {
        float total = (float) products.stream().mapToDouble(Product::getDefaultPrice).sum();
        return total;
    }

    public float getTotalPriceForProduct(Product product) {
        float total = 0;
        for(Product prod : products) {
            if(prod.equals(product)) total += prod.getDefaultPrice();
        }
        return total;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }
}
