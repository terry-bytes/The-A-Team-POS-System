/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.Service.InventoryService;
import ateam.DAO.InventoryDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.Models.Inventory;
import java.util.List;

/**
 *
 * @author user
 */
public class InventoryServiceIMPL  implements InventoryService{

    private InventoryDAO inventoryDao;
    
    public InventoryServiceIMPL(InventoryDAO inventoryDao) {
        this.inventoryDao = inventoryDao;
        inventoryDao = new InventoryDAOIMPL();
        
        
    }
    
    
    

    public boolean addAnInventory(Inventory inventory, int addedByEmployeeId) {
        
     return inventoryDao.addInventory(inventory, addedByEmployeeId);
    }
    

    public Inventory getInventoryById(int inventory_ID) {
     
        return inventoryDao.getInventoryById(inventory_ID);
    }

    
    public List<Inventory> getAllTheInventories() {
        
     return inventoryDao.getAllInventories();
    }

    public boolean updateTheInventory(Inventory inventory) {
    
     return inventoryDao.updateInventory(inventory);
    }

    public boolean deleteAnInventory(int inventory_ID) {
        
     return inventoryDao.deleteInventory(inventory_ID);
    }
    
}
