package com.codecool.shop.database;

import java.awt.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBaseHandler {

    private static String propertyFile;
    private static String DATABASE;
    private static String DB_USER = System.getenv("DB_USER");
    private static String DB_PASSWORD = System.getenv("PASSWORD");
    private static String DB_URL = "jdbc:postgresql://localhost:5432/" + DATABASE;

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

    public static void setPropertyFile(String propertyFile) {
        DataBaseHandler.propertyFile = propertyFile;
    }

    public static void setDatabaseName() {
        try {
            Properties props = new Properties();
            FileInputStream in = new FileInputStream("src/main/resources/"+propertyFile);
            props.load(in);
            in.close();

            DATABASE = props.getProperty("database");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
