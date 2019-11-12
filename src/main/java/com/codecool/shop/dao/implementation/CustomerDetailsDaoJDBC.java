package com.codecool.shop.dao.implementation;

import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.userdata.Userdata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDetailsDaoJDBC {
    private AddressDaoJDBC addressDataStore = new AddressDaoJDBC();

    public void add(Userdata userdata, Integer customerId) {

        String query = "INSERT INTO customers_information (id, customer_id, name, email, phone_number, shipping_address_id, billing_address_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {

            preparedStatement.setInt(1, userdata.getId());
            preparedStatement.setInt(2, customerId);
            preparedStatement.setString(3, userdata.getName());
            preparedStatement.setString(4, userdata.getEmail());
            preparedStatement.setString(5, userdata.getPhoneNumber());
            preparedStatement.setInt(6, userdata.getShippingAddress().getId());
            preparedStatement.setInt(7, userdata.getBillingAddress().getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Userdata find(int customerId) {
        String query = "SELECT * FROM customers_information WHERE customer_id = ?;";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, customerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Userdata(resultSet.getInt("id"),
                        null,
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone_number"),
                        addressDataStore.find(resultSet.getInt("billing_address_id")),
                        addressDataStore.find(resultSet.getInt("shipping_address_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Userdata userdata) {

        String query = "UPDATE customers_information SET name = ?, email = ?, phone_number = ? " +
                "WHERE id = ?;";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setString(1, userdata.getName());
            preparedStatement.setString(2, userdata.getEmail());
            preparedStatement.setString(3, userdata.getPhoneNumber());
            preparedStatement.setInt(4, userdata.getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            addressDataStore.update(userdata.getBillingAddress());
            addressDataStore.update(userdata.getShippingAddress());
        }
    }

    public void remove(int orderId) {
        String query = "DELETE FROM customers_information WHERE order_id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            Userdata userdata = find(orderId);

            preparedStatement.setInt(1, orderId);
            preparedStatement.execute();

            addressDataStore.remove(userdata.getBillingAddress().getId());
            addressDataStore.remove(userdata.getShippingAddress().getId());


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

