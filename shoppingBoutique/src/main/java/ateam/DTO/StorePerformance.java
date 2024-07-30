/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DTO;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author T440
 */
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StorePerformance {
    private int storeID;
    private BigDecimal totalSales;
    private BigDecimal totalStockValue;
    private BigDecimal salesEfficiency;
    private int totalQuantitySold;
    private BigDecimal stockTurnover;
    private BigDecimal currentPeriodSales;
    private BigDecimal previousPeriodSales;
    private BigDecimal salesGrowth;
    
    private BigDecimal normalizedTotalSales;
    private BigDecimal normalizedSalesEfficiency;
    private BigDecimal normalizedStockTurnover;
    private BigDecimal normalizedSalesGrowth;
    private BigDecimal compositeScore;
}
