package ateam.DAOIMPL;

import ateam.Models.Inventory;
import ateam.BDconnection.Connect;
import ateam.DAO.InventoryDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAOIMPL implements InventoryDAO {

    private final Connection connection;

    public InventoryDAOIMPL() {
        Connect connect = new Connect();
        this.connection = connect.connectToDB();
    }


    @Override
    public Inventory getInventoryByProductAndStore(int product_ID, int store_ID) throws SQLException {
        String query = "SELECT * FROM inventory WHERE product_ID = ? AND store_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, product_ID);
            stmt.setInt(2, store_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Inventory(
                        rs.getInt("inventory_ID"),
                        rs.getInt("product_ID"),
                        rs.getInt("store_ID"),
                        rs.getInt("inventory_quantity"),
                        rs.getInt("reorder_point"),
                        rs.getTimestamp("last_updated"),
                        rs.getInt("added_by_employee_ID")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public void updateInventory(Inventory inventory) throws SQLException {
        String query = "UPDATE inventory SET inventory_quantity = ?, last_updated = ?, added_by_employee_ID = ? WHERE inventory_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, inventory.getInventory_quantity());
            stmt.setTimestamp(2, inventory.getLast_updated());
            stmt.setInt(3, inventory.getAdded_by_employee_ID());
            stmt.setInt(4, inventory.getInventory_ID());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Inventory> getInventoryByStore(int store_ID) throws SQLException {
        List<Inventory> inventoryList = new ArrayList<>();
        String query = "SELECT * FROM inventory WHERE store_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, store_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Inventory inventory = new Inventory(
                        rs.getInt("inventory_ID"),
                        rs.getInt("product_ID"),
                        rs.getInt("store_ID"),
                        rs.getInt("inventory_quantity"),
                        rs.getInt("reorder_point"),
                        rs.getTimestamp("last_updated"),
                        rs.getInt("added_by_employee_ID")
                    );
                    inventoryList.add(inventory);
                }
            }
        }
        return inventoryList;
    }
}


