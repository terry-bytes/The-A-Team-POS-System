/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

import ateam.Models.Inventory;
import java.util.List;

/**
 *
 * @author user
 */
public interface InventoryService {
    
    boolean addAnInventory(Inventory inventory, int addedByEmployeeId);
    Inventory getInventoryById(int inventory_ID);
    List<Inventory> getAllTheInventories();
    boolean updateTheInventory(Inventory inventory);
    boolean deleteAnInventory(int inventory_ID);
    
}
