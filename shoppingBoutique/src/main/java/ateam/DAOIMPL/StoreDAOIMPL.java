/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.StoreDAO;
import ateam.Models.Product;
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
public class StoreDAOIMPL implements StoreDAO {
    
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public StoreDAOIMPL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void addStore(Store store) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO store (store_name, store_address, store_city, store_province, store_zipcode, store_phone, store_email) VALUES (?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, store.getStore_name());
            preparedStatement.setString(2, store.getStore_address());
            preparedStatement.setString(3, store.getStore_city());
            preparedStatement.setString(4, store.getStore_province());
            preparedStatement.setInt(5, store.getStore_zipcode());
            preparedStatement.setString(6, store.getStore_phone());
            preparedStatement.setString(7, store.getStore_email());
            preparedStatement.executeUpdate();
            System.out.println("Store Stored in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Store getStoreById(int store_ID) {
        Store stores = new Store();
        try {
            preparedStatement = connection.prepareStatement("SELECT store_ID, store_name, store_address, store_city, store_province, store_zipcode, store_phone, store_email FROM store WHERE store_ID = ?");
            preparedStatement.setInt(1, store_ID);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            stores.setStore_ID(resultSet.getInt("store_ID"));
            stores.setStore_name(resultSet.getString("store_name"));
            stores.setStore_address(resultSet.getString("store_address"));
            stores.setStore_city(resultSet.getString("store_city"));
            stores.setStore_province(resultSet.getString("store_province"));
            stores.setStore_zipcode(resultSet.getInt("store_zipcode"));
            stores.setStore_phone(resultSet.getString("store_phone"));
            stores.setStore_email(resultSet.getString("store_email"));
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return stores;
    }

    @Override
    public List<Store> getAllStores() {
        Store allStore = new Store();
        List<Store> allStores = new ArrayList<>();
         
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM store");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                allStore.setStore_ID(resultSet.getInt("store_ID"));
                allStore.setStore_name(resultSet.getString("store_name"));
                allStore.setStore_address(resultSet.getString("store_address"));
                allStore.setStore_city(resultSet.getString("store_city"));
                allStore.setStore_province(resultSet.getString("store_province"));
                allStore.setStore_zipcode(resultSet.getInt("store_zipcode"));
                allStore.setStore_phone(resultSet.getString("store_phone"));
                allStore.setStore_email(resultSet.getString("store_email"));
                allStores.add(allStore);
            }
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allStores;
    }

    @Override
    public void updateStore(Store store) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE store SET store_name = ?, store_address = ?, store_city = ?, store_province = ?, store_zipcode = ?, store_phone = ?, store_email = ? WHERE store_ID = ?");
            preparedStatement.setString(1, store.getStore_name());
            preparedStatement.setString(2, store.getStore_address());
            preparedStatement.setString(3, store.getStore_city());
            preparedStatement.setString(4, store.getStore_province());
            preparedStatement.setInt(5, store.getStore_zipcode());
            preparedStatement.setString(6, store.getStore_phone());
            preparedStatement.setString(7, store.getStore_email());
            preparedStatement.setInt(8, store.getStore_ID());
            preparedStatement.executeUpdate();
            System.out.println("Store Updated in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteStore(int store_ID) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM products WHERE product_ID = ?");
            preparedStatement.setInt(1, store_ID);
            preparedStatement.executeUpdate();
            System.out.println("Store Deleted in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(StoreDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
