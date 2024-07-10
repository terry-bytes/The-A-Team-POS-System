/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.SaleDAO;
import ateam.Models.Sale;
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
 * @author Train 01
 */
public class SaleDaoImpl implements SaleDAO{
    private final Connection connection;

    public SaleDaoImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public boolean addSale(Sale sale) {
        if(connection != null){
            String sql = "INSERT INTO sales(total_amount, payment_method, customer_ID, employee_ID, store_ID, layaway_ID) VALUES(?, ?, ?, ?, ?, ?)";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setDouble(1, sale.getTotal_amount());
                ps.setString(2, sale.getPayment_method());
                ps.setInt(3, sale.getCustomer_ID());
                ps.setInt(4, sale.getEmployee_ID());
                ps.setInt(5, sale.getStore_ID());
                ps.setInt(6, sale.getLayaway_ID());
                
                if(ps.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(SaleDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public Sale getSaleById(int sales_ID) {
        Sale sale = new Sale();
        if(connection != null){
            String sql = "SELECT sales_date, total_amount, payment_method, customer_ID, employee_ID, store_ID, layaway_ID"
                    + " FROM sales"
                    + " WHERE sales_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, sales_ID);
                
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        sale.setSales_date(resultSet.getTimestamp("sales_date"));
                        sale.setPayment_method(resultSet.getString("payment_method"));
                        sale.setCustomer_ID(resultSet.getInt("customer_ID"));
                        sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                        sale.setStore_ID(resultSet.getInt("store_ID"));
                        sale.setLayaway_ID(resultSet.getInt("layaway_ID"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sale;
    }

    @Override
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        if(connection != null){
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, customer_ID, employee_ID, store_ID, layaway_ID"
                    + " FROM sales";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Sale sale = new Sale();
                        sale.setSales_ID(resultSet.getInt("sales_ID"));
                        sale.setSales_date(resultSet.getTimestamp("sales_date"));
                        sale.setPayment_method(resultSet.getString("payment_method"));
                        sale.setCustomer_ID(resultSet.getInt("customer_ID"));
                        sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                        sale.setStore_ID(resultSet.getInt("store_ID"));
                        sale.setLayaway_ID(resultSet.getInt("layaway_ID"));
                        sales.add(sale);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sales;
    }

    @Override
    public boolean updateSale(Sale sale) {
        if(connection != null){
            String sql = "UPDATE sales SET total_amount=?, payment_method=?, customer_ID=?, employee_ID=?, store_ID=?, layaway_ID=?"
                    + " WHERE sales_ID = ?";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setDouble(1, sale.getTotal_amount());
                ps.setString(2, sale.getPayment_method());
                ps.setInt(3, sale.getCustomer_ID());
                ps.setInt(4, sale.getEmployee_ID());
                ps.setInt(5, sale.getStore_ID());
                ps.setInt(6, sale.getLayaway_ID());
                ps.setInt(7, sale.getSales_ID());
                
                if(ps.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(SaleDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean deleteSale(int sales_ID) {
        if(connection != null){
            String sql = "DELETE FROM sales WHERE sales_ID=?";
            try(PreparedStatement ps = connection.prepareStatement(sql)){
                ps.setDouble(1, sales_ID);
                
                if(ps.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(SaleDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
}
