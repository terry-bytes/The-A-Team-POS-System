package ateam.DAO;


import ateam.Models.Inventory;
import java.util.List;

public interface InventoryDAO {

    void addInventory(Inventory inventory);

    Inventory getInventoryById(int inventory_ID); 

    List<Inventory> getAllInventories();

    void updateInventory(Inventory inventory);

    void deleteInventory(int inventory_ID); 
}
