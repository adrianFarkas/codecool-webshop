package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.database.DataBaseHandler;
import com.codecool.shop.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao {
    @Override
    public void add(ProductCategory category) {
        try (Connection conn = DataBaseHandler.getConnection()){

            String query = "INSERT INTO categories (id, name, department, description) VALUES (?, ?, ?, ?);";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, category.getId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setString(3, category.getDepartment());
            preparedStatement.setString(4, category.getDescription());

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {

        String query = "SELECT * FROM categories WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                ProductCategory result = createCategoryFromResultSet(resultSet);
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM categories WHERE id = ?;";

        try (Connection conn = DataBaseHandler.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
        ){
            preparedStatement.setInt(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public List<ProductCategory> getAll() {
        String query = "SELECT * FROM categories;";

        List<ProductCategory> productCategories = new ArrayList<>();

        try (Connection conn = DataBaseHandler.getConnection();
             Statement statement = conn.createStatement();
        ){
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                ProductCategory actCategory = createCategoryFromResultSet(resultSet);
                productCategories.add(actCategory);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productCategories;
    }

    private ProductCategory createCategoryFromResultSet(ResultSet resultSet) {
        try {
            return new ProductCategory(resultSet.getInt("id"),
                    resultSet.getString("name"),
                    resultSet.getString("department"),
                    resultSet.getString("description"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
