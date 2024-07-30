/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

import ateam.Models.Inventory;
import ateam.Models.Product;
import ateam.Models.Sale;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author user
 */
public interface InventoryService {
    
   void replenishStock(String productSKU,int product_ID, int store_ID, int quantity, int employee_ID) throws SQLException;
   List<Inventory>getAll() throws Exception;
   void processSale(int salesId) throws SQLException;
   
}
