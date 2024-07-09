/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

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

/**
 *
 * @author carme
 */
public class ProductDAOIMPL implements ProductDAO{
    
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public ProductDAOIMPL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addProduct(Product product) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO products (product_name, product_description, product_price, category_ID, product_SKU, quantity_in_stock, productImagePath) VALUES (?, ?, ?, ?, ?, ?, ?)");
   
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setString(2, product.getProduct_description());
            preparedStatement.setDouble(3, product.getProduct_price());
            preparedStatement.setInt(4, product.getCategory_ID());
            preparedStatement.setString(5, product.getProduct_SKU());
            preparedStatement.setInt(6, product.getQuantity_in_stock());
            preparedStatement.setString(7, product.getProduct_image_path());
            preparedStatement.executeUpdate();
            System.out.println("Product Stored in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Product getProductById(int product_ID) {
        Product Product = new Product();
        try {
            preparedStatement = connection.prepareStatement("SELECT product_ID, product_name, product_description, product_price, category_ID, product_SKU, quantity_in_stock, productImagePath FROM products WHERE product_ID = ?");
            preparedStatement.setInt(1, product_ID);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            Product.setProduct_ID(resultSet.getInt("Product_ID"));
            Product.setProduct_name(resultSet.getString("product_name"));
            Product.setProduct_description(resultSet.getString("product_description"));
            Product.setProduct_price(resultSet.getInt("product_price"));
            Product.setCategory_ID(resultSet.getInt("category_ID"));
            Product.setProduct_SKU(resultSet.getString("product_SKU"));
            Product.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
            Product.setProduct_image_path(resultSet.getString("productImagePath"));
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Product;
    }

    @Override
    public List<Product> getAllProducts() {
        Product allProduct = new Product();
        List<Product> allProducts = new ArrayList<>();
        
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM products");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
              allProduct.setProduct_ID(resultSet.getInt("product_ID"));
              allProduct.setProduct_name(resultSet.getString("product_name"));
              allProduct.setProduct_description(resultSet.getString("product_description"));
              allProduct.setProduct_price(resultSet.getDouble("product_price"));
              allProduct.setCategory_ID(resultSet.getInt("category_ID"));
              allProduct.setProduct_SKU(resultSet.getString("product_SKU"));
              allProduct.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
              allProduct.setProduct_image_path(resultSet.getString("productImagePath"));
              allProducts.add(allProduct);
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        return allProducts;
    }

    @Override
    public void updateProduct(Product product) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE products SET product_name = ?, product_description = ?, product_price = ?, category_ID = ?, product_SKU = ?, quantity_in_stock = ?, productImagePath = ? WHERE product_ID = ?");
            preparedStatement.setString(1, product.getProduct_name());
            preparedStatement.setString(2, product.getProduct_description());
            preparedStatement.setDouble(3, product.getProduct_price());
            preparedStatement.setInt(4, product.getCategory_ID());
            preparedStatement.setString(5, product.getProduct_SKU());
            preparedStatement.setInt(6, product.getQuantity_in_stock());
            preparedStatement.setInt(7, product.getProduct_ID());
            preparedStatement.executeUpdate();
            System.out.println("Product Updated in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteProduct(int product_ID) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM products WHERE product_ID = ?");
            preparedStatement.setInt(1, product_ID);
            preparedStatement.executeUpdate();
            System.out.println("Product Deleted in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Product getProductByBarcode(String product_SKU) {
        Product ProductByBarcode = new Product();
        try {
            preparedStatement = connection.prepareStatement("SELECT product_ID, product_name, product_description, product_price, category_ID, product_SKU, quantity_in_stock, productImagePath FROM products WHERE product_SKU = ?");
            preparedStatement.setString(1, product_SKU);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            ProductByBarcode.setProduct_ID(resultSet.getInt("Product_ID"));
            ProductByBarcode.setProduct_name(resultSet.getString("product_name"));
            ProductByBarcode.setProduct_description(resultSet.getString("product_description"));
            ProductByBarcode.setProduct_price(resultSet.getInt("product_price"));
            ProductByBarcode.setCategory_ID(resultSet.getInt("category_ID"));
            ProductByBarcode.setProduct_SKU(resultSet.getString("product_SKU"));
            ProductByBarcode.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
            ProductByBarcode.setProduct_image_path(resultSet.getString("productImagePath"));
        } catch (SQLException ex) {
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
           return ProductByBarcode;
    }
}
