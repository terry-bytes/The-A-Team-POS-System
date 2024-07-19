package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.Models.Sale;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.logging.Level;
import java.util.logging.Logger;

// Corrected class name: SaleDAOImpl (not SaleDAOlMPL)
public class SaleDAOIMPL implements SaleDAO {
    private Connection connection;




    
    public SaleDAOIMPL(){

        this.connection = new Connect().connectToDB();
    }
    @Override
    public int saveSale(Sale sale) {
        String sql = "INSERT INTO sales (sales_date, total_amount, payment_method, employee_ID, store_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new Connect().connectToDB();  
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, sale.getSales_date());  
            pstmt.setBigDecimal(2, sale.getTotal_amount()); 
            pstmt.setString(3, sale.getPayment_method()); 
            pstmt.setInt(4, sale.getEmployee_ID());   
            pstmt.setInt(5, sale.getStore_ID());    

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); 
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return -1; 
    }


    @Override
    public BigDecimal calculateTotalAmount(int salesId)  {
        BigDecimal totalAmount = BigDecimal.ZERO;
        try (PreparedStatement stmt = connection.prepareStatement("")) {
            stmt.setInt(1, salesId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalAmount = rs.getBigDecimal("total_amount");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalAmount;
    }

    @Override
    public boolean updateTotalAmount(int salesId, BigDecimal totalAmount){
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement("")) {
            stmt.setBigDecimal(1, totalAmount);
            stmt.setInt(2, salesId);
            int rowsUpdated = stmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    @Override
    public boolean finalizeSale(int salesId, String paymentMethod)  {
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement("")) {
            stmt.setString(1, paymentMethod);
            stmt.setInt(2, salesId);
            int rowsUpdated = stmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    @Override
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        if(connection != null){
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID, store_ID FROM sales";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Sale sale = new Sale();
                        sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                        sale.setPayment_method(resultSet.getString("payment_method"));
                        sale.setSales_ID(resultSet.getInt("sales_ID"));
                        sale.setSales_date(resultSet.getTimestamp("sales_date"));
                        sale.setStore_ID(resultSet.getInt("store_ID"));
                        sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));
                        
                        sales.add(sale);
                    }
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sales;
    }


    @Override
    public int startNewSale(int employeeId, int storeId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Sale> getSalesbyStoreId(int storeId) {
        List<Sale> sales = new ArrayList<>();
        if(connection != null){
            String sql ="SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID "
                    + "FROM sales "
                    + "WHERE store_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, storeId);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Sale sale = new Sale();
                        sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                        sale.setPayment_method(resultSet.getString("payment_method"));
                        sale.setSales_ID(resultSet.getInt("sales_ID"));
                        sale.setSales_date(resultSet.getTimestamp("sales_date"));
                        
                        sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));
                        
                        sales.add(sale);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sales;
    }


}
