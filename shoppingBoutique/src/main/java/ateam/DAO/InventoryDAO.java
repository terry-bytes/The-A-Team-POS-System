package ateam.DAO;

import ateam.Models.Inventory;
import java.util.List;



public interface InventoryDAO {
    
    
    void logInventoryTransaction(Inventory inventory) throws Exception;
    int getPreviousQuantity(int productId) throws Exception;
    void updateProductQuantity(int productId, int newQuantity) throws Exception;
    List<Inventory> getAllInventories() throws Exception;
    
}
