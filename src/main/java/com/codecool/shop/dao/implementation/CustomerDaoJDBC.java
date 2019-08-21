package com.codecool.shop.dao.implementation;

import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.userdata.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDaoJDBC {
    public void add(Customer customer) {
        String query = "INSERT INTO customers (user_name, password, email) VALUES (?,?,?);";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ){
            preparedStatement.setString(1, customer.getUserName());
            preparedStatement.setString(2, customer.getPassword());
            preparedStatement.setString(3, customer.getEmail());


            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Customer find(String userName) {
        String query = "SELECT * FROM customers WHERE user_name = ?";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ){
            preparedStatement.setString(1, userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new Customer(resultSet.getInt("id"),
                        resultSet.getString("user_name"),
                        resultSet.getString("password"),
                        resultSet.getString("email"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}