package com.codecool.shop.model;

import com.codecool.shop.Util;
import com.codecool.shop.dao.implementation.ProductDaoJDBC;
import com.codecool.shop.database.DataBaseHandler;

import java.sql.*;
import java.util.*;

public class Order implements Orderable {

    private Integer id;
    private Integer userId;
    private Status status;

    public Order(Integer id, Integer userId, Status status) {
        this.id = id;
        this.userId = userId;
        this.status = status;
    }

    public static Order create(Integer userId) {
        return new Order(Util.getNextIdFromTable("orders"), userId, Status.NEW);
    }

    public void addItem(Product product) {
        String query = "INSERT INTO order_item (order_id, product_id) VALUES (?, ?);";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, this.id);
            preparedStatement.setInt(2, product.getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMultipleItem(Product product, int num) {
        for(int i = 0; i < num; i++) {
            addItem(product);
        }
    }

    public void remove(Product product) {
        String query = "DELETE FROM order_item WHERE id =" +
                        " (SELECT id FROM order_item WHERE product_id = ? AND order_id = ? LIMIT 1);";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setInt(2, this.id);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        String query = "SELECT COUNT(*) as products_num FROM order_item WHERE order_id = ?;";
        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return resultSet.getInt("products_num");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> getProducts() {
        String query = "SELECT products.id, name, description, default_price, default_currency, category_id, supplier_id " +
                        "FROM products " +
                        "JOIN order_item ON products.id = order_item.product_id " +
                        "JOIN orders ON order_item.order_id = orders.id " +
                        "WHERE orders.id = ?;";

        List<Product> products = new ArrayList<>();

        try(Connection conn = DataBaseHandler.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return new ProductDaoJDBC().getProductsFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public boolean checkout() {
        if (status.equals(Status.NEW)) {
            String query = "UPDATE orders SET status = ? WHERE id = ?;";
            try (Connection conn = DataBaseHandler.getConnection();
                PreparedStatement preparedStatement = conn.prepareStatement(query)
            ){
                preparedStatement.setString(1, String.valueOf(Status.CHECKED));
                preparedStatement.setInt(2, this.id);
                preparedStatement.execute();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean pay() {
        if (status.equals(Status.CHECKED)) {
            String query = "UPDATE orders SET status = ? WHERE id = ?;";
            try (Connection conn = DataBaseHandler.getConnection();
                 PreparedStatement preparedStatement = conn.prepareStatement(query)
            ){
                preparedStatement.setString(1, String.valueOf(Status.PAID));
                preparedStatement.setInt(2, this.id);
                preparedStatement.execute();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public Status getStatus() {
        return status;
    }

    public Map<Product, Integer> getProductsPartitionByNum() {
        LinkedHashMap<Product, Integer> lineItem = new LinkedHashMap<>();

        String query = "SELECT products.id, name, description, default_price, default_currency, category_id, supplier_id, COUNT(*) " +
                        "FROM products " +
                        "JOIN order_item oi ON products.id = oi.product_id " +
                        "JOIN orders ON oi.order_id = orders.id " +
                        "WHERE orders.id = ? " +
                        "GROUP BY products.id;";

        try (Connection conn = DataBaseHandler.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query)
        ) {
            preparedStatement.setInt(1, this.id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                lineItem.put(new ProductDaoJDBC().createProductFromResultSet(resultSet), resultSet.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lineItem;
    }

    public float getTotalPrice() {
        float total = (float) getProducts().stream().mapToDouble(Product::getDefaultPrice).sum();
        return total;
    }

    public float getTotalPriceForProduct(Product product) {
        float total = 0;
        for(Product prod : getProducts()) {
            if(prod.getId() == (product.getId())) total += prod.getDefaultPrice();
        }
        return total;
    }

}
