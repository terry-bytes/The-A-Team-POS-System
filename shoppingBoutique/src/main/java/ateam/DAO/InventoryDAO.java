package ateam.DAO;

import ateam.Models.Inventory;
import ateam.Models.SalesItem;
import java.sql.SQLException;
import java.util.List;



public interface InventoryDAO {
    
    void addProductAndInventory(String barcode, int storeID, int quantity,int employeeID) throws SQLException;
    //void logInventoryTransaction(Inventory inventory) throws Exception;
    int getPreviousQuantity(int productId) throws Exception;
    
    void updateProductQuantity(int productId, int newQuantity) throws Exception;
    List<Inventory> getAllInventories() throws Exception;
    void decreaseInventoryQuantity(int productId, int storeId, int quantity) throws Exception;
    void decreaseProductQuantity(int productId, int quantity) throws Exception;
    int getStoreIdFromSales(int salesId) throws Exception;
    List<SalesItem> getSalesItems(int salesId) throws Exception;
    
}
