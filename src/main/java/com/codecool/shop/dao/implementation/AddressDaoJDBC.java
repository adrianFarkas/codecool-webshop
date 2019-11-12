package com.codecool.shop.dao.implementation;

import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.userdata.Address;
import com.codecool.shop.userdata.AddressType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressDaoJDBC {
    
    public void add(Address address) {
        String query = "INSERT INTO addresses (id, country, city, zip_code, address, address_type) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ){
            preparedStatement.setInt(1, address.getId());
            preparedStatement.setString(2, address.getCountry());
            preparedStatement.setString(3, address.getCity());
            preparedStatement.setInt(4, address.getZipCode());
            preparedStatement.setString(5, address.getAddress());
            preparedStatement.setString(6, String.valueOf(address.getType()));

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    public Address find(int id) {
        String query = "SELECT * FROM addresses WHERE id = ?";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                return new Address(resultSet.getInt("id"),
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getInt("zip_code"),
                        resultSet.getString("address"),
                        AddressType.valueOf(resultSet.getString("address_type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Address address) {

        String query = "UPDATE addresses SET country = ?, city = ?, zip_code = ?, address = ?" +
                        "WHERE id = ?";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setString(1, address.getCountry());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setInt(3, address.getZipCode());
            preparedStatement.setString(4, address.getAddress());
            preparedStatement.setInt(5, address.getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void remove(int id) {
        String query = "DELETE FROM addresses WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
