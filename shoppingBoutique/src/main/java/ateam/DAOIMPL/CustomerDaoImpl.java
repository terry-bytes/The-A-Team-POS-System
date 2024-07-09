/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.CustomerDAO;
import ateam.Models.Customer;
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
public class CustomerDaoImpl implements CustomerDAO{
    private Connection connection;

    public CustomerDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addCustomer(Customer customer) {
        if(connection != null){
            String sql = "INSERT INTO customers(customer_name, customer_surname, customer_email, customer_password) VALUES(?, ?, ?, ?)";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, customer.getCustomer_name());
                preparedStatement.setString(2, customer.getCustomer_surname());
                preparedStatement.setString(3, customer.getCustomer_email());
                preparedStatement.setString(4, customer.getCustomer_password());
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public Customer getCustomerById(int customer_ID) {
        Customer customer = new Customer();
        if(connection != null){
            String sql ="SELECT customer_name, customer_surname, customer_email, customer_password FROM customers";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, customer_ID);
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        customer.setCustomer_ID(customer_ID);
                        customer.setCustomer_name(resultSet.getString("customer_name"));
                        customer.setCustomer_surname(resultSet.getString("customer_surname"));
                        customer.setCustomer_email(resultSet.getString("customer_email"));
                        customer.setCustomer_password(resultSet.getString("customer_password"));
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return customer;
    }

    @Override
    public List<Customer> getAllCustomers() {
       List<Customer> customers = new ArrayList<>(); 
        if(connection != null){
            String sql ="SELECT customer_ID, customer_name, customer_surname, customer_email, customer_password FROM customers";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Customer customer = new Customer();
                        customer.setCustomer_ID(resultSet.getInt("customer_ID"));
                        customer.setCustomer_name(resultSet.getString("customer_name"));
                        customer.setCustomer_surname(resultSet.getString("customer_surname"));
                        customer.setCustomer_email(resultSet.getString("customer_email"));
                        customer.setCustomer_password(resultSet.getString("customer_password"));
                        
                        customers.add(customer);
                    }
                } 
            } catch (SQLException ex) {
               Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
        return customers;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        if(connection != null){
            String sql = "UPDATE customers SET customer_name=?, customer_surname=?, customer_email=?, customer_password=? WHERE customer_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setString(1, customer.getCustomer_name());
                preparedStatement.setString(2, customer.getCustomer_surname());
                preparedStatement.setString(3, customer.getCustomer_email());
                preparedStatement.setString(4, customer.getCustomer_password());
                preparedStatement.setInt(5, customer.getCustomer_ID());
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean deleteCustomer(int customer_ID) {
        if(connection != null){
            String sql = "DELETE FROM customers WHERE customer_ID = ?";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                preparedStatement.setInt(1, customer_ID);
                
                if(preparedStatement.executeUpdate() > 0) return true;
            } catch (SQLException ex) {
                Logger.getLogger(CustomerDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
    
}
