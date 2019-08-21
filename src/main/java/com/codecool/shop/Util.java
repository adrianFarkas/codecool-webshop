package com.codecool.shop;

import com.codecool.shop.database.DataBaseHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {

    public static int getNextIdFromTable(String table) {

        String query = "SELECT MAX(id) as max_id FROM " + table + ";";

        try (Connection conn = DataBaseHandler.getConnection();
             Statement statement = conn.createStatement();
        ){
            ResultSet resultSet = statement.executeQuery(query);
            if(resultSet.next()) {
                return resultSet.getInt("max_id") + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
