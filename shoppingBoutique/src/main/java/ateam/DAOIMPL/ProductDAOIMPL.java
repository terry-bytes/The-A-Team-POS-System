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

    @Override
    public boolean addProduct(Product product) {
       
        return false;
    }

    @Override
    public List<Product> allProduct() {
        
        
            List<Product> allItems = new ArrayList();
            String statements =  "Select* from products ";
    
        try {
           
            PreparedStatement  preparedStatement = connection.prepareStatement(statements);
            
            
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                
               
                int ID = resultSet.getInt("product_ID");
                String productName = resultSet.getString("product_name");
                String description = resultSet.getString("product_description");
                Double price = resultSet.getDouble("product_price");
                int categoryId = resultSet.getInt("category_ID");
                String SKU = resultSet.getString("product_SKU");
                int quantity = resultSet.getInt("quantity_in_stock");
                String ImagePath = resultSet.getString("productImagePath");
                
                Product product = new Product(ID,productName,description,price,categoryId,SKU,quantity,ImagePath);
                
                allItems.add(product);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return allItems;
        
    }
}
