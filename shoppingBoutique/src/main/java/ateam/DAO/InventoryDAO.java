package ateam.DAO;

import ateam.Models.Inventory;
import java.sql.SQLException;
import java.util.List;


public interface InventoryDAO {
    
    
    Inventory getInventoryByProductAndStore(int product_ID, int store_ID) throws SQLException;
    void updateInventory(Inventory inventory) throws SQLException;
    List<Inventory> getInventoryByStore(int store_ID) throws SQLException;
    
}
