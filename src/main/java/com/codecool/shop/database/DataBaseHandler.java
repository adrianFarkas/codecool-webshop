package com.codecool.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseHandler {

    private static final String DATABASE = System.getenv("DATABASE");
    private static final String DB_USER = System.getenv("DB_USER");
    private static final String DB_PASSWORD = System.getenv("PASSWORD");
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/" + DATABASE;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DB_URL,
                DB_USER,
                DB_PASSWORD);
    }

    public static void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
