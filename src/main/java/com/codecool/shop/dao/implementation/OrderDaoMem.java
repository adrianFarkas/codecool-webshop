package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDaoMem implements OrderDao {

    private List<Order> data = new ArrayList<>();
    private static OrderDaoMem instance = null;

    private OrderDaoMem() {
    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        data.add(order);
    }

    @Override
    public List<Order> findAllByUserId(int userId) {
        return data.stream().filter(t -> t.getUserId() == userId).collect(Collectors.toList());
    }

    @Override
    public Order findById(int id) {
        return data.stream().filter(t -> t.getId() == id).findFirst().orElse(null);
    }


    @Override
    public void remove(int id) {
        data.remove(findById(id));
    }

    @Override
    public List<Order> getAll() {
        return data;
    }


    public Order getActualOrderByUser(int userId) {
        List<Order> orders = findAllByUserId(userId);
        Order actualOrder = orders.stream().filter(t -> !t.getStatus().equals(Status.PAID)).findFirst().orElse(null);
        return actualOrder;
    }

    public Order createOrder(int userId) {
        Order actual = getActualOrderByUser(userId);
        if(actual == null) {
            Order newOrder = new Order(userId);
            add(newOrder);
            return newOrder;
        }
        return actual;
    }
}
