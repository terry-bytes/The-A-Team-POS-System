package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ProductDAO;
import ateam.Models.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductDAOIMPL implements ProductDAO {

    private static final String SQL_SELECT_PRODUCT_BY_SKU = "SELECT * FROM products WHERE product_SKU = ?";

    private Connection connection;

    public ProductDAOIMPL() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public List<Product> getProductBySKU(String productSKU) {
        List<Product> getProduct = new ArrayList<>();
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Decode the barcode
            String[] parts = productSKU.split("-");
            if (parts.length != 3) {
                // Invalid barcode format
                throw new IllegalArgumentException("Invalid barcode format");
            }

            String sku = parts[0];
            String size = parts[1];
            String color = parts[2];

            String sql = "SELECT p.*, pv.size, pv.color FROM products p "
                    + "JOIN productvariants pv ON p.product_SKU = pv.product_SKU "
                    + "WHERE p.product_SKU = ? AND pv.size = ? AND pv.color = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, sku);
            preparedStatement.setString(2, size);
            preparedStatement.setString(3, color);

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
                product.setSize(resultSet.getString("size"));
                product.setColor(resultSet.getString("color"));
                getProduct.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

        return getProduct;
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

    @Override
    public boolean addProduct(Product product) {

        String insert = "Insert into product(product_name, product_description,product_price,category_ID,product_SKU,quantity_in_stock,productImagePath) values(?,?,?,?,?,?,?)";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(insert);

            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setString(2, product.getProduct_description());
            preparedStatement.setDouble(3, product.getProduct_price());
            preparedStatement.setInt(4, product.getCategory_ID());
            preparedStatement.setString(5, product.getProduct_SKU());
            preparedStatement.setInt(6, product.getQuantity_in_stock());
            preparedStatement.setString(7, product.getProduct_image_path());

            if (preparedStatement.executeUpdate() > 0) {
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    @Override
    public List<Product> allProduct() {

        List<Product> allItems = new ArrayList();
        String statements = "Select* from products ";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(statements);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                Product product = new Product();
                product.setProduct_ID(resultSet.getInt("product_ID"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setProduct_price(resultSet.getDouble("product_price"));
                product.setCategory_ID(resultSet.getInt("category_ID"));
                product.setProduct_SKU(resultSet.getString("product_SKU"));
                product.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
                product.setProduct_image_path(resultSet.getString("productImagePath"));

                allItems.add(product);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return allItems;

    }
}
