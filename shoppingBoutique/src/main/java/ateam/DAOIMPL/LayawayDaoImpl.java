 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.LayawayDAO;
import ateam.Models.Layaway;
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
public class LayawayDaoImpl implements LayawayDAO{
    private final Connection connection;

    public LayawayDaoImpl(Connection connection) {
        this.connection = connection;
    }
    
    
    @Override
    public boolean addLayaway(Layaway layaway) {
        if(connection != null){
            String sql = "INSERT INTO layaways(employee_ID, start_date, expiry_date, layaway_status, customer_email, contact, product_ID, product_quantity, customer_name) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, layaway.getEmployee_ID());
                preparedStatement.setTimestamp(2, layaway.getStart_date());
                preparedStatement.setTimestamp(3, layaway.getExpiry_date());
                preparedStatement.setString(4, layaway.getLayaway_status());
                preparedStatement.setString(5, layaway.getCustomerEmail());
                preparedStatement.setString(6, layaway.getCustomerNumber());
                preparedStatement.setInt(7, layaway.getProductID());
                preparedStatement.setInt(8, layaway.getProductQuantity());
                preparedStatement.setString(9, layaway.getCustomerName());
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public Layaway getLayawayById(int layaway_ID) {
        Layaway layaway = new Layaway();
        if(connection != null){
            String sql = "SELECT employee_ID, start_date, expiry_date, layaway_status, customer_email, contact, product_ID, product_quantity"
                    + " FROM layaways"
                    + " WHERE layaway_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, layaway_ID);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        layaway.setLayaway_ID(layaway_ID);
                        layaway.setCustomerEmail(resultSet.getString("customer_email"));
                        layaway.setEmployee_ID(resultSet.getInt("employee_ID"));
                        layaway.setStart_date(resultSet.getTimestamp("start_date"));
                        layaway.setExpiry_date(resultSet.getTimestamp("expiry_date"));
                        layaway.setLayaway_status(resultSet.getString("layaway_status"));
                        layaway.setProductID(resultSet.getInt("product_ID"));
                        layaway.setProductQuantity(resultSet.getInt("product_quantity"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
        }
        return layaway;
    }

    @Override
    public List<Layaway> getAllLayaways() {
        List<Layaway> layaways = new ArrayList();
        if(connection != null){
            String sql = "SELECT layaway_ID, employee_ID, start_date, expiry_date, layaway_status, customer_email, contact, product_ID, product_quantity"
                    + " FROM layaways";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Layaway layaway = new Layaway();
                        layaway.setLayaway_ID(resultSet.getInt("layaway_ID"));
                        layaway.setCustomerEmail(resultSet.getString("customer_email"));
                        layaway.setEmployee_ID(resultSet.getInt("employee_ID"));
                        layaway.setStart_date(resultSet.getTimestamp("start_date"));
                        layaway.setExpiry_date(resultSet.getTimestamp("expiry_date"));
                        layaway.setLayaway_status(resultSet.getString("layaway_status"));
                        layaway.setProductID(resultSet.getInt("product_ID"));
                        layaway.setProductQuantity(resultSet.getInt("product_quantity"));
                        layaway.setCustomerNumber(resultSet.getString("contact"));
                        layaways.add(layaway);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }         
        }
        return layaways;
    }

    @Override
    public boolean updateLayaway(Layaway layaway) {
        if(connection != null){
            String sql = "UPDATE layaways SET customer_ID=?, employee_ID=?, start_date=?, expiry_date=?, layaway_status=?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, layaway.getCustomer_ID());
                preparedStatement.setInt(2, layaway.getEmployee_ID());
                preparedStatement.setTimestamp(3, layaway.getStart_date());
                preparedStatement.setTimestamp(4, layaway.getExpiry_date());
                preparedStatement.setString(5, layaway.getLayaway_status());
                
                if(preparedStatement.executeUpdate() > 0) return true; 
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean deleteLayaway(int layaway_ID) {
        if(connection != null){
            String sql = "DELETE FROM layaways WHERE layaway_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, layaway_ID);
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
    @Override
    public Layaway emailData(String customerEmail) {
        Layaway emailLayaway = new Layaway();
        if(connection != null) {
            String sql = "SELECT layaway_ID, start_date, expiry_date, product_ID, product_quantity FROM layaways WHERE customer_email = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setString(1, customerEmail);
                try(ResultSet resultSet  = preparedStatement.executeQuery()) {
                    if(resultSet.next()) {
                        emailLayaway.setLayaway_ID(resultSet.getInt("layaway_ID"));
                        emailLayaway.setStart_date(resultSet.getTimestamp("start_date"));
                        emailLayaway.setExpiry_date(resultSet.getTimestamp("expiry_date"));
                        emailLayaway.setProductID(resultSet.getInt("product_ID"));
                        emailLayaway.setProductQuantity(resultSet.getInt("product_quantity"));
                    }
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(LayawayDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return emailLayaway;
    }
}
