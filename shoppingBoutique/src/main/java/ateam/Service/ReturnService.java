/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

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
public interface ReturnService {
  
    Sale getSaleById(int salesId);
    BigDecimal getUnitPrice(int salesId, int productId);
    void updateProductQuantity(int productId, int quantity);
    void addReturn(Return returns);
   void handleCustomerOptions(int salesId, BigDecimal remainingAmount, String customerChoice);
   void handleCustomerOptions(int salesId, BigDecimal remainingAmount, String customerChoice, int selectedProductId);
   BigDecimal processReturn(int salesId, int productId, int quantity,String email, String reason);
    List<Product> getProductsByPrice(BigDecimal price);
    void addSalesItem(SalesItem sales);
    Product getProductById(int productId);
    int getProductIdBySKU(String productSKU);
    List<SalesItem> getSalesItemsBySaleId(int salesId);
    
}
