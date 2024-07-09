package ateam.DAO;

import ateam.Models.Inventory;
import java.util.List;

public interface InventoryDAO {

    boolean addInventory(Inventory inventory, int addedByEmployeeId);

    Inventory getInventoryById(int inventory_ID);

    List<Inventory> getAllInventories();

    boolean updateInventory(Inventory inventory);

    boolean deleteInventory(int inventory_ID);
}
