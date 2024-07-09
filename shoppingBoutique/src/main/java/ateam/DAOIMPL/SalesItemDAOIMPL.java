/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.DAO.SalesItemDAO;
import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
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
public class SalesItemDAOIMPL implements SalesItemDAO{
    
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    
    public SalesItemDAOIMPL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void createSalesItem(SalesItem salesItem) {
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO sales_items (sales_ID, product_ID, quantity, unit_price) VALUES(?,?,?,?)");
            preparedStatement.setInt(1, salesItem.getSales_ID());
            preparedStatement.setInt(2, salesItem.getProduct_ID());
            preparedStatement.setInt(3, salesItem.getQuantity());
            preparedStatement.setDouble(4, salesItem.getUnit_price());
             preparedStatement.executeUpdate();
            System.out.println("Sales Item Stored in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public SalesItem getSalesItemById(int sales_item_ID) {
        SalesItem SaleItemById = new SalesItem();
        try {
            preparedStatement = connection.prepareStatement("SELECT sale_item_id, sales_ID, product_ID, quantity, unit_price WHERE sales_item_ID = ?");
            preparedStatement.setInt(1, sales_item_ID);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
            SaleItemById.setSales_item_ID(resultSet.getInt("sale_item_id"));
            SaleItemById.setSales_ID(resultSet.getInt("sales_ID"));
            SaleItemById.setProduct_ID(resultSet.getInt("product_ID"));
            SaleItemById.setQuantity(resultSet.getInt("quantity"));
            SaleItemById.setUnit_price(resultSet.getDouble("unit_price"));
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return SaleItemById;
    }

    @Override
    public List<SalesItem> getSalesItemsBySaleId(int sales_ID) {
        SalesItem allSalesItem = new SalesItem();
        List<SalesItem> allSalesItems = new ArrayList<>();
        
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM sales_items");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                allSalesItem.setSales_item_ID(resultSet.getInt("sales_item_ID"));
                allSalesItem.setSales_ID(resultSet.getInt("sales_ID"));
                allSalesItem.setProduct_ID(resultSet.getInt("product_ID"));
                allSalesItem.setQuantity(resultSet.getInt("quantity"));
                allSalesItem.setUnit_price(resultSet.getInt("unit_price"));
                allSalesItems.add(allSalesItem);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return allSalesItems;
    }

    @Override
    public void updateSalesItem(SalesItem salesItem, Sale sale, Product product) {
        try {
            preparedStatement = connection.prepareStatement("UPDATE sales_items SET sales_ID = ?, product_ID = ?, quantity = ?, unit_price = ? WHERE sales_item_ID = ?");
            preparedStatement.setInt(1, sale.getSales_ID());
            preparedStatement.setInt(2, product.getProduct_ID());
            preparedStatement.setInt(3, salesItem.getQuantity());
            preparedStatement.setDouble(4, salesItem.getUnit_price());
            preparedStatement.executeUpdate();
            System.out.println("Sales Item Updated in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void deleteSalesItem(int sales_item_ID) {
        try {
            preparedStatement = connection.prepareStatement("DELETE FROM sales_items WHERE sales_item_ID = ?");
            preparedStatement.setInt(1, sales_item_ID);
            preparedStatement.executeUpdate();
            System.out.println("Sales Item Deleted in DB Successfully");
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
