/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAO;

import ateam.DTO.InventoryData;
import ateam.DTO.ProductSalesData;
import ateam.DTO.SalesData;
import ateam.DTO.StorePerformance;
import ateam.Models.Sale;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author T440
 */
public interface ReportDAO {
    List<StorePerformance> fetchStorePerformanceData(LocalDate startDate, LocalDate endDate);
    List<InventoryData> getTotalInventory();
    List<SalesData> getTotalSoldItems();
    List<InventoryData> getTotalInventory(LocalDate startDate, LocalDate endDate);
    List<SalesData> getTotalSoldItems(LocalDate startDate, LocalDate endDate);
    List<ProductSalesData> getTopSellingProduct();
    
    Map<LocalDate, BigDecimal> getSalesByStoreAndDateRange(int storeId, LocalDate startDate, LocalDate endDate);
    Map<Integer, BigDecimal> getTotalInventoryValuePerStore(LocalDate startDate, LocalDate endDate);
    Map<Integer, BigDecimal> getTotalSalesAmountPerStore(LocalDate startDate, LocalDate endDate);
    List<Sale> getTodaysSales();
}
