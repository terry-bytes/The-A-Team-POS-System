/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DTO;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author T440
 */
public class StorePerfomanceInSales {
    private final int storeId;
    private final Map<Integer, Integer> totalInventoryMap;
    private final Map<Integer, Integer> totalSoldMap;

    public StorePerfomanceInSales(int storeId) {
        this.storeId = storeId;
        this.totalInventoryMap = new HashMap<>();
        this.totalSoldMap = new HashMap<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public void addTotalInventory(int productId, int totalInventory) {
        totalInventoryMap.merge(productId, totalInventory, Integer::sum);
    }

    public void addTotalSold(int productId, int totalSold) {
        totalSoldMap.merge(productId, totalSold, Integer::sum);
    }

    public int getTotalInventory() {
        return totalInventoryMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getTotalSold() {
        return totalSoldMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getPercentageSold() {
        int totalInventory = getTotalInventory();
        if (totalInventory == 0) {
            return 0.0; // Handle division by zero gracefully
        }
        int totalSold = getTotalSold();
        return (double) totalSold / totalInventory * 100.0;
    }
}
