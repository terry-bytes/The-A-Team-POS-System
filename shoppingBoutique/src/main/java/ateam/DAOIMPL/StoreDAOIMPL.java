
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.StoreDAO;
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
 * @author Train
 */
public class StoreDAOIMPL implements StoreDAO{
    
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;
    
    public StoreDAOIMPL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addStore(Store store) {
        boolean success = false;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO store(store_name, store_address, store_city, store_province, store_zipcode, store_phone, store_email) VALUES(?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, store.getStore_name());
            preparedStatement.setString(2, store.getStore_address());
            preparedStatement.setString(3, store.getStore_city());
            preparedStatement.setString(4, store.getStore_province());
            preparedStatement.setInt(5, store.getStore_zipcode());
            preparedStatement.setString(6, store.getStore_phone());
            preparedStatement.setString(7, store.getStore_email());
            preparedStatement.execute();
        
            int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            success = true;
        }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return success;
    }

    @Override
    public Store getStoreById(int store_ID) {
         Store store = new Store();
         
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM store WHERE store_ID = ?");
            preparedStatement.setInt(1, store_ID);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
            store.setStore_ID(resultSet.getInt("store_ID"));
            store.setStore_name(resultSet.getString("store_name"));
            store.setStore_address(resultSet.getString("store_address"));
            store.setStore_city(resultSet.getString("store_city"));
            store.setStore_province(resultSet.getString("store_province"));
            store.setStore_zipcode(resultSet.getInt("store_zipcode"));
            store.setStore_phone(resultSet.getString("store_phone"));
            store.setStore_email(resultSet.getString("store_email"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return store;
    }

    @Override
    public List<Store> getAllStores() {
        List<Store> stores = new ArrayList();
        
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM store");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                Store store = new Store();
                store.setStore_ID(resultSet.getInt("store_ID"));
                store.setStore_name(resultSet.getString("store_name"));
                store.setStore_address(resultSet.getString("store_address"));
                store.setStore_city(resultSet.getString("store_city"));
                store.setStore_province(resultSet.getString("store_province"));
                store.setStore_zipcode(resultSet.getInt("store_zipcode"));
                store.setStore_phone(resultSet.getString("store_phone"));
                store.setStore_email(resultSet.getString("store_email"));
                stores.add(store);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
         return stores;
    }

    @Override
    public boolean updateStore(Store store) {
        boolean success = false;
        try {
            preparedStatement = connection.prepareStatement("UPDATE store SET store_name = ?, store_address = ?, store_city = ?, store_province = ?, store_zipcode = ?, store_phone = ?, store_email = ? WHERE store_ID = ? ");
            preparedStatement.setString(1, store.getStore_name());
            preparedStatement.setString(2, store.getStore_address());
            preparedStatement.setString(3, store.getStore_city());
            preparedStatement.setString(4, store.getStore_province());
            preparedStatement.setInt(5, store.getStore_zipcode());
            preparedStatement.setString(6, store.getStore_phone());
            preparedStatement.setString(7, store.getStore_email());
            preparedStatement.setInt(8, store.getStore_ID());
            preparedStatement.execute();
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
    public boolean deleteStore(int store_ID) {
        boolean success = false;
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM store WHERE store_ID = ?");
            preparedStatement.setInt(1, store_ID);
            preparedStatement.execute();
            int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            success = true;
        }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close(resultSet, preparedStatement);
        }
        return success;
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
