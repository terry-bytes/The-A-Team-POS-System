package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.InventoryDAO;
import ateam.Models.Inventory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOIMPL implements InventoryDAO {

    @Override
    public void logInventoryTransaction(Inventory inventory) throws Exception {
        String sql = "INSERT INTO inventory (product_ID, store_ID, inventory_quantity, previous_quantity, reorder_point, last_updated, updated_by_employee_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, inventory.getProduct_ID());
            pstmt.setInt(2, inventory.getStore_ID());
            pstmt.setInt(3, inventory.getInventory_quantity());
            pstmt.setInt(4, inventory.getPrevious_quantity());
            pstmt.setInt(5, inventory.getReorder_point());
            pstmt.setTimestamp(6, inventory.getLast_updated());
            pstmt.setInt(7, inventory.getUpdated_by_employee_ID());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error logging inventory transaction: " + ex.getMessage(), ex);
        }
    }

//    @Override
//    public int getPreviousQuantity(int productId) throws Exception {
//        String sql = "SELECT quantity_in_stock FROM products WHERE product_ID = ?";
//        try (Connection conn = new Connect().connectToDB();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setInt(1, productId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    return rs.getInt("quantity_in_stock");
//                } else {
//                    throw new Exception("Product not found.");
//                }
//            }
//        } catch (SQLException ex) {
//            throw new Exception("Error fetching previous quantity: " + ex.getMessage(), ex);
//        }
//    }
    
    @Override
    public int getPreviousStoreQuantity(int productId, int storeID) throws Exception {
         String sql = "SELECT inventory_quantity FROM inventory WHERE product_ID = ? AND store_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.setInt(2, storeID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("inventory_quantity");
                } else {
                    throw new Exception("Inventory not found for the specified product and store.");
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Error fetching previous store quantity: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void updateProductQuantity(int productId, int newQuantity) throws Exception {
        String sql = "UPDATE products SET quantity_in_stock = ? WHERE product_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error updating product quantity: " + ex.getMessage(), ex);
        }
    }
    
    
    @Override
     public List<Inventory> getAllInventories() throws Exception {
        String sql = "SELECT * FROM inventory";
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventory_ID(rs.getInt("inventory_ID"));
                inventory.setProduct_ID(rs.getInt("product_ID"));
                inventory.setStore_ID(rs.getInt("store_ID"));
                inventory.setInventory_quantity(rs.getInt("inventory_quantity"));
                inventory.setPrevious_quantity(rs.getInt("previous_quantity"));
                inventory.setReorder_point(rs.getInt("reorder_point"));
                inventory.setLast_updated(rs.getTimestamp("last_updated"));
                inventory.setUpdated_by_employee_ID(rs.getInt("updated_by_employee_ID"));
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }
     
}