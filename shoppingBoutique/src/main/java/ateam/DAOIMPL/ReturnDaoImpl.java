/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ReturnDao;
import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class ReturnDaoImpl implements ReturnDao{
    
    

    public ReturnDaoImpl() {
       
        new Connect().connectToDB();
    }
    
    /**
     *
     * @param saleId
     * @param newTotalAmount
     */
     
    public void updateSaleTotalAmount(int sales_ID, double newTotalAmount) {
        String query = "UPDATE sales SET total_amount = ? WHERE sales_ID = ?";

        try (Connection conn = new Connect().connectToDB(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, newTotalAmount);
            stmt.setInt(2, sales_ID);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
public void insertReturn(Return returnRecord) {
    try (Connection conn = new Connect().connectToDB(); 
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO returns (sales_ID, product_ID, quantity, return_date, email, reason) VALUES (?, ?, ?, ?, ?, ?)")) {
        stmt.setInt(1, returnRecord.getSales_ID());
        stmt.setInt(2, returnRecord.getProduct_ID());
        stmt.setInt(3, returnRecord.getQuantity());
        stmt.setTimestamp(4, returnRecord.getReturn_date());
        stmt.setString(5, returnRecord.getEmail());
        stmt.setString(6, returnRecord.getReason());

        System.out.println("Executing query: " + stmt.toString());
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace(); // Print the stack trace to understand the error
    }
}

    @Override
    public Sale getSaleById(int saleId) {
        Sale sale = null;
        try (Connection conn = new Connect().connectToDB(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sales WHERE sales_ID = ?")) {
            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                sale = new Sale();
                sale.setSales_ID(rs.getInt("sales_ID"));
                sale.setSales_date(rs.getTimestamp("sales_date"));
                sale.setTotal_amount(rs.getBigDecimal("total_amount"));
                sale.setPayment_method(rs.getString("payment_method"));
                sale.setEmployee_ID(rs.getInt("employee_ID"));
                sale.setStore_ID(rs.getInt("store_ID"));
            }
        } catch (SQLException e) {
        }
        return sale;
    }

    @Override
    public List<SalesItem> getSalesItemsBySaleId(int saleId) {
        List<SalesItem> salesItems = new ArrayList<>();
        try (Connection conn = new Connect().connectToDB(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sales_items WHERE sales_ID = ?")) {
            stmt.setInt(1, saleId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SalesItem item = new SalesItem();
                item.setSales_item_ID(rs.getInt("sales_item_ID"));
                item.setSales_ID(rs.getInt("sales_ID"));
                item.setProduct_ID(rs.getInt("product_ID"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnit_price(rs.getBigDecimal("unit_price"));
                salesItems.add(item);
            }
        } catch (SQLException e) {
        }
        return salesItems;
    }

    @Override
    public SalesItem getSalesItemById(int saleItemId) {
        SalesItem item = new SalesItem();
        try (Connection conn = new Connect().connectToDB(); PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sales_items WHERE sales_ID = ?")) {
            stmt.setInt(1, saleItemId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                
                item.setSales_item_ID(rs.getInt("sales_item_ID"));
                item.setSales_ID(rs.getInt("sales_ID"));
                item.setProduct_ID(rs.getInt("product_ID"));
                item.setQuantity(rs.getInt("quantity"));
                item.setUnit_price(rs.getBigDecimal("unit_price"));
                
            }
        } catch (SQLException e) {
        }
       
        return item;
    }
    
    @Override
    public BigDecimal getUnitPrice(int salesId, int productId) {
            BigDecimal unitPrice = null;
    String sql = "SELECT unit_price FROM sales_items WHERE sales_ID = ? AND product_ID = ?";

    try (Connection conn = new Connect().connectToDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setInt(1, salesId);
        pstmt.setInt(2, productId);

        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                unitPrice = rs.getBigDecimal("unit_price");
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
    }

    return unitPrice;
    }

    @Override
    public void updateSalesTotal(int salesId, BigDecimal totalReturnAmount) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Product> getProductsByPrice(BigDecimal price) {
         
        List<Product> products = new ArrayList<>();
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Connection conn = new Connect().connectToDB();
            String sql = "Select * from Products where product_price = ? ";

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setBigDecimal(1, price);
            

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                product = new Product();
                product.setProduct_ID(resultSet.getInt("product_ID"));
                product.setProduct_name(resultSet.getString("product_name"));
                product.setProduct_description(resultSet.getString("product_description"));
                product.setProduct_price(resultSet.getDouble("product_price"));
                product.setCategory_ID(resultSet.getInt("category_ID"));
                product.setProduct_SKU(resultSet.getString("product_SKU"));
                product.setQuantity_in_stock(resultSet.getInt("quantity_in_stock"));
                product.setProduct_image_path(resultSet.getString("productImagePath"));
                
                products.add(product);
            }
        } catch (SQLException e) {
            // Log the SQL exception
            
        } catch (IllegalArgumentException e) {
            // Log the invalid argument exception
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            throw e; // Rethrow to be handled by servlet
        } 

        return products;
    }


    @Override
    public Product getProductById(int productId) {
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
    
        
     try {
            Connection conn = new Connect().connectToDB();
            String sql = "Select * from Products where product_ID = ? ";

            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
        } catch (SQLException e) 
        {
            
        }
        return product;     
    }

    
    
    public int  getProductIdBySKU(String productSKU) {
        
        Product product = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Validate the barcode format
            String[] parts = productSKU.split("-");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Invalid barcode format: " + productSKU);
            }

            String sku = parts[0];
            String size = parts[1];
            String color = parts[2];

           String sql = "SELECT * from Products where product_SKU =?";
           Connection conn = new Connect().connectToDB();
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, sku);
           
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
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
            // Log the SQL exception
            
        } catch (IllegalArgumentException e) {
            // Log the invalid argument exception
            Logger.getLogger(ProductDAOIMPL.class.getName()).log(Level.SEVERE, e.getMessage(), e);
            throw e; // Rethrow to be handled by servlet
        } 
        return product.getProduct_ID();
    }
    
    
    
}
