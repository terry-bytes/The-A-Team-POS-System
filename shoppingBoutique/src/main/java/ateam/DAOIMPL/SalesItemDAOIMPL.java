
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

/**
 *
 * @author Train
 */


import ateam.BDconnection.Connect;
import ateam.DAO.SalesItemDAO;
import ateam.Models.SalesItem;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesItemDAOIMPL implements SalesItemDAO {

    private static final String SQL_INSERT_SALES_ITEM = "INSERT INTO sales_items (sales_ID, product_ID, quantity, unit_price) VALUES (?, ?, ?, ?)";
    private static final String SQL_DELETE_SALES_ITEM = "DELETE FROM sales_items WHERE sales_item_ID = ?";

    private Connection connection;

    public SalesItemDAOIMPL() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public boolean addItemToSale(int salesId, SalesItem item) {
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_SALES_ITEM)) {
            stmt.setInt(1, salesId);
            stmt.setInt(2, item.getProduct_ID());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getUnit_price());
            int rowsInserted = stmt.executeUpdate();
            success = rowsInserted > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    @Override
    public boolean removeSaleItem(int salesItemId) {
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_DELETE_SALES_ITEM)) {
            stmt.setInt(1, salesItemId);
            int rowsDeleted = stmt.executeUpdate();
            success = rowsDeleted > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
}
