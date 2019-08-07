package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

public interface OrderDao {
    void add(Order order);
    List<Order> findAllByUserId(int userId);
    Order findById(int id);
    void remove(int id);


    List<Order> getAll();
}
