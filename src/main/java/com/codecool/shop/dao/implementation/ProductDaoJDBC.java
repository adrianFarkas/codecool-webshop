package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private ProductCategoryDao productCategoryDao = new ProductCategoryDaoJDBC();
    private SupplierDao supplierDao = new SupplierDaoJDBC();

    @Override
    public void add(Product product) {

        String query = "INSERT INTO products (id, name, description, default_price, default_currency, category_id, supplier_id)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){

            preparedStatement.setInt(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getDescription());
            preparedStatement.setFloat(4, product.getDefaultPrice());
            preparedStatement.setString(5, String.valueOf(product.getDefaultCurrency()));
            preparedStatement.setInt(6, product.getProductCategory().getId());
            preparedStatement.setInt(7, product.getSupplier().getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Product find(int id) {
        String query = "SELECT * FROM products WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                Product result = createProductFromResultSet(resultSet);
                return result;
            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {

        String query = "DELETE FROM products WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ) {

            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<Product> getAll() {

        String query = "SELECT * FROM products";

        List<Product> products = new ArrayList<>();

        try (Connection conn = DataBaseHandler.getConnection();
             Statement statement = conn.createStatement();
        ){
            ResultSet resultSet = statement.executeQuery(query);

            products = getProductsFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getBy(Supplier supplier) {

        String query = "SELECT * FROM products WHERE supplier_id = ?";

        List<Product> products = new ArrayList<>();

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, supplier.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            products = getProductsFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT * FROM products WHERE category_id = ?";

        List<Product> products = new ArrayList<>();

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, productCategory.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            products = getProductsFromResultSet(resultSet);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public List<Product> getProductsFromResultSet(ResultSet resultSet) {
        List<Product> products = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Product actProduct = createProductFromResultSet(resultSet);
                products.add(actProduct);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    public Product createProductFromResultSet(ResultSet resultSet) {
        try {
            return new Product(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getFloat("default_price"),
                    resultSet.getString("default_currency"),
                    resultSet.getString("description"),
                    productCategoryDao.find(resultSet.getInt("category_id")),
                    supplierDao.find(resultSet.getInt("supplier_id")));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
