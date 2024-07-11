package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ProductDAO;
import ateam.Models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductDAOIMPL implements ProductDAO {

    private static final String SQL_SELECT_PRODUCT_BY_SKU = "SELECT * FROM products WHERE product_SKU = ?";

    private Connection connection;

    public ProductDAOIMPL () {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public Product getProductBySKU(String productSKU) {
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_PRODUCT_BY_SKU);
            preparedStatement.setString(1, productSKU);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                product = new Product();
                product.setProduct_ID(resultSet.getInt("product_ID"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setProduct_price(resultSet.getDouble("product_price"));
                product.setCategory_ID(resultSet.getInt("category_ID"));
                product.setProduct_SKU(resultSet.getString("product_SKU"));
                product.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
                product.setProduct_image_path(resultSet.getString("productImagePath"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

        return product;
    }

    private void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
