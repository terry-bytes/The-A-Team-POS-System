/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author T440
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class InventoryData {
    private int storeId;
    private int productId;
    private int totalInventory;
    private int totalSold;
    
    public InventoryData(int storeId, int productId, int totalInventory) {
        this.storeId = storeId;
        this.productId = productId;
        this.totalInventory = totalInventory;
        this.totalSold = 0; // Initialize totalSold
    }
}
