/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

import ateam.DTO.TopProductSellEmployee;
import ateam.Models.SalesItem;
import java.util.List;

/**
 *
 * @author Admin
 */
public interface SaleItemsService {
    List<SalesItem> getSalesItemsByProductId(int productId);
    List<TopProductSellEmployee> getTotalSalesPerProduct();
}
