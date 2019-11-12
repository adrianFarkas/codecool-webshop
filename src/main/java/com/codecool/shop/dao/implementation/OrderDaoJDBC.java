package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoJDBC implements OrderDao {

    @Override
    public void add(Order order) {

        try (Connection conn = DataBaseHandler.getConnection()){

            String query = "INSERT INTO orders (id, customer_id, status) VALUES (?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, order.getId());
            preparedStatement.setInt(2, order.getUserId());
            preparedStatement.setString(3, order.getStatus().toString());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Order> findAllByUserId(int userId) {

        String query = "SELECT * FROM  orders WHERE customer_id = ?;";
        List<Order> orders = new ArrayList<>();

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Order actOrder = new Order(resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        Status.valueOf(resultSet.getString("status")));
                orders.add(actOrder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public Order findById(int id) {
        String query = "SELECT * FROM  orders WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order actOrder = new Order(resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        Status.valueOf(resultSet.getString("status")));
                return actOrder;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM orders WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            new DeliveryDetailsDaoJDBC().remove(id);

            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Order getActualOrderByUser(int userId) {
        String query = "SELECT * FROM orders WHERE customer_id = ?" +
                        "AND status in ('NEW', 'CHECKED') LIMIT 1;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Order actOrder = new Order(resultSet.getInt("id"),
                        resultSet.getInt("customer_id"),
                        Status.valueOf(resultSet.getString("status")));
                return actOrder;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Order createOrderIfNotExists(int userId) {
        Order actual = getActualOrderByUser(userId);
        if(actual == null) {
            Order newOrder = Order.create(userId);
            add(newOrder);
            return newOrder;
        }
        return actual;
    }
}
