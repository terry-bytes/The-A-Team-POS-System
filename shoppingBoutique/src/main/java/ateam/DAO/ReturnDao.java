/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.DAO;

import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author user
 */
public interface ReturnDao {
    
   
    BigDecimal getUnitPrice(int salesId, int productId);
    
    
    void updateSalesTotal(int salesId, BigDecimal totalReturnAmount);
    List<Product> getProductsByPrice(BigDecimal price);
    
    Product getProductById(int productId);
    int getProductIdBySKU(String productSKU);
    
    void insertReturn(Return returnRecord);
    
    Sale getSaleById(int saleId);
    List<SalesItem> getSalesItemsBySaleId(int saleId);
    
    
}
