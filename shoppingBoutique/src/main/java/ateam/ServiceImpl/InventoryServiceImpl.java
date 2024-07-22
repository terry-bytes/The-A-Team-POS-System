package ateam.Services.impl;

import ateam.DAO.InventoryDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.Models.Inventory;
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
     public void replenishStock(int productId, int additionalStock, int employeeId, int storeId) throws SQLException {
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

        // Set other properties
        inventory.setReorder_point(0); // Modify as needed
        inventory.setLast_updated(new Timestamp(System.currentTimeMillis()));
        inventory.setUpdated_by_employee_ID(employeeId);

        try {
            // Log inventory transaction
            inventoryDAO.logInventoryTransaction(inventory);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Update product quantity
        int newQuantity = previousQuantity + additionalStock;
        try {
            inventoryDAO.updateProductQuantity(productId, newQuantity);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Inventory> getAll() throws Exception {
        
        return inventoryDAO.getAllInventories();
    }
}
