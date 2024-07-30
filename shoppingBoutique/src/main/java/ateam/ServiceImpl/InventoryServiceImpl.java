package ateam.Services.impl;

import ateam.DAO.InventoryDAO;
import ateam.DAO.ReturnDao;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.DAOIMPL.ReturnDaoImpl;
import ateam.Models.Inventory;
import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.InventoryService;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InventoryServiceImpl implements InventoryService {
    private final InventoryDAO inventoryDAO;
   

    public InventoryServiceImpl() {
        
        inventoryDAO = new InventoryDAOIMPL();
       
        
    }

    @Override
     public void replenishStock(String productSKU,int productId, int additionalStock, int employeeId, int storeId) throws SQLException {
    Inventory inventory = new Inventory();
        inventory.setProduct_ID(productId);
        inventory.setStore_ID(storeId);
        inventory.setInventory_quantity(additionalStock);

        // Get previous quantity
        int previousQuantity = 0;
        try {
            previousQuantity = inventoryDAO.getPreviousQuantity(productId);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        inventory.setPrevious_quantity(previousQuantity);
        
        

        // Set other properties before we add on inventory
        inventory.setReorder_point(5); 
        inventory.setLast_updated(new Timestamp(System.currentTimeMillis()));
        inventory.setUpdated_by_employee_ID(employeeId);
        
        

        try {
            // Log inventory transaction
            inventoryDAO.addProductAndInventory(productSKU, storeId, additionalStock,employeeId);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Update product quantity as I add on the inventory
        int newQuantity = previousQuantity + additionalStock;
        try {
            inventoryDAO.updateProductQuantity(productId, newQuantity);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
     
     @Override
    public void processSale(int salesId) throws SQLException {
        try {
            List<SalesItem> salesItems = inventoryDAO.getSalesItems(salesId);
            int storeId = inventoryDAO.getStoreIdFromSales(salesId);

            for (SalesItem salesItem : salesItems) {
                int productId = salesItem.getProduct_ID();
                int quantity = salesItem.getQuantity();

                // Decrease the inventory quantity for the specific store and product
                inventoryDAO.decreaseInventoryQuantity(productId, storeId, quantity);

                // Decrease the product quantity in the products table
                inventoryDAO.decreaseProductQuantity(productId, quantity);
            }
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw new SQLException("Error processing sale: ");
                    }
    }

    @Override
    public List<Inventory> getAll() throws Exception {
        
        return inventoryDAO.getAllInventories();
    }

    
    
   
}
