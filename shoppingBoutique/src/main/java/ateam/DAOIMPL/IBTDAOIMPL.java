/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.IBTDAO;
import ateam.Models.IBT;
import ateam.Models.Store;
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
public class IBTDAOIMPL implements  IBTDAO{
    
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    
    public IBTDAOIMPL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<IBT> getAllProducts(int product_ID) {
        List<IBT> Stores = new ArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT product_ID, store_ID, inventory_quantity FROM inventory WHERE product_ID = ?");
            preparedStatement.setInt(1, product_ID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                IBT ibt = new IBT();
                ibt.setProductID(resultSet.getInt("product_ID"));
                ibt.setStoreID(resultSet.getInt("store_ID"));
                ibt.setQuantity(resultSet.getInt("inventory_quantity"));
                Stores.add(ibt);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return Stores;
    }

     
    @Override
    public boolean sendIBTRequest(int product_ID, int store_ID, String store_name, int product_quantity, String customerName, String customerNumber, String customerEmail, int StoreID) {
        boolean success = false;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO IBTRequest (store_ID, product_ID, requestFlag, requested_store, quantity, customer_name, customer_number, customer_email, ibt_requested) VALUES (?,?,?,?,?,?,?,?,?)");
            preparedStatement.setInt(1, store_ID);
            preparedStatement.setInt(2, product_ID);
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, store_name);
            preparedStatement.setInt(5, product_quantity);
            preparedStatement.setString(6, customerName);
            preparedStatement.setString(7, customerNumber);
            preparedStatement.setString(8, customerEmail);
            preparedStatement.setInt(9, StoreID);
            int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            success = true;
        }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }  finally {
            close(resultSet, preparedStatement);
        }
        
        return success;
    }
    
 
    @Override
    public List<IBT> receiveIBTRequest(int store_ID) {
        List<IBT> Stores = new ArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT request_ID, store_ID, product_id, requested_store, quantity FROM IBTRequest WHERE store_ID = ? AND requestFlag = 1");
            preparedStatement.setInt(1, store_ID);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                IBT ibt = new IBT();
                ibt.setProductID(resultSet.getInt("product_ID"));
                ibt.setStoreID(resultSet.getInt("store_ID"));
                ibt.setRequestedtore(resultSet.getString("requested_store"));
                ibt.setQuantity(resultSet.getInt("quantity"));
                ibt.setRequestID(resultSet.getInt("request_ID"));
                Stores.add(ibt);
            }
        } catch (SQLException ex) {
            Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return Stores;
    }

    @Override
    public boolean deleteRequestedIBT(int store_ID) {
        boolean success = false;
        try {
            preparedStatement = connection.prepareStatement("UPDATE ibtrequest SET requestFlag = 0 WHERE store_ID = ?");
            preparedStatement.setInt(1, store_ID);
             preparedStatement.execute();
            int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            success = true;
        }
        } catch (SQLException ex) {
            Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return success;
    }
    
    @Override
    public boolean checkForIBTNotification(int store_ID) {
    boolean success = false;
    try {
        preparedStatement = connection.prepareStatement("SELECT ibt_requested FROM IBTRequest WHERE ibt_requested = ?");
        preparedStatement.setInt(1, store_ID);
        resultSet = preparedStatement.executeQuery(); // Use executeQuery() instead of execute()

        if (resultSet.next()) {
            success = true; // If there's at least one matching row, set success to true
        }
    } catch (SQLException ex) {
        Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
    } 
    return success;
}
    
    @Override
    public String retrieveCustomerNumber(int layawayID) {
        String customerNumber = null;
        int receivingStoreID = 0;
        try {
            preparedStatement = connection.prepareStatement("SELECT customer_number, ibt_requested FROM ibtrequest WHERE request_ID = ?");
            preparedStatement.setInt(1, layawayID);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                customerNumber = resultSet.getString("customer_number");
                receivingStoreID = resultSet.getInt("ibt_requested");
            }
        } catch (SQLException ex) {
            Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return customerNumber;
    }
    
    @Override
    public int retrieveStoreID(int IBTID) {
        int IBT_ID = 0;
        try {
        preparedStatement = connection.prepareStatement("SELECT ibt_requested FROM ibtrequest WHERE request_ID = ?");
        preparedStatement.setInt(1, IBTID);
        resultSet = preparedStatement.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(IBTDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return IBT_ID;
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
