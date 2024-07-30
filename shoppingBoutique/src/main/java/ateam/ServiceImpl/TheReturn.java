/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.InventoryDAO;
import ateam.DAO.ProductDAO;
import ateam.DAO.ReturnDao;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.DAOIMPL.ReturnDaoImpl;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Services.impl.InventoryServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */

public class TheReturn {

    private final ReturnDao returnDao;
    private final InventoryDAO inventoryDAO;
    private ProductDAO productDAO ;
    private SaleDAO saleDao;
    private SalesItemDAO saleItems;
    private SalesItemDAO salesItemDao = new SalesItemDAOIMPL();
    
    public TheReturn() {
        this.returnDao= new ReturnDaoImpl();
        this.inventoryDAO = new InventoryDAOIMPL();
        this.productDAO = new ProductDAOIMPL();
        this.saleDao = new SaleDAOIMPL();
    }
    
    
    
   
    public List<SalesItem> getSalesItemsBySaleId(int salesId) {
        
      return returnDao.getSalesItemsBySaleId(salesId);
    }

    
   public void addReturn(Return returns) {
    returnDao.insertReturn(returns);
}
    
   public boolean decreaseItems(int quantity, int salesItemId){
       
       return salesItemDao.decreaseItem(quantity, salesItemId);
   }
   
    public BigDecimal updateSaleTotalAmount(int sales_ID, BigDecimal returnAmount,int quantity,BigDecimal totalA) {
       
        
        BigDecimal intAsBigDecimal = BigDecimal.valueOf(quantity);
        boolean success = false;
        BigDecimal newTotalAmount =BigDecimal.ZERO;
        
            
            newTotalAmount = totalA.subtract(returnAmount.multiply(intAsBigDecimal));
            
            if(saleDao.updateTotalAmount(sales_ID, newTotalAmount)){
                success =true;
                System.out.println(success);
               
            }
            System.out.println( "new total R"+newTotalAmount);
            return newTotalAmount;
            
        
       
    }
    
    public BigDecimal getUnitPrice(int salesId, int productId){
        
        return returnDao.getUnitPrice(salesId, productId);
    }
    
    
    public SalesItem getSalesItemById(int salesItemId) {
        
        
     return  returnDao.getSalesItemById(salesItemId);
    }
    
    public Sale getSaleById(int sales_ID) {
        
        return returnDao.getSaleById(sales_ID);
    }
    
    
     
    public void updateProductQuantity(int productId, int quantity) {
        
        // Get previous quantity
        int previousQuantity = 0;
        try {
            previousQuantity = inventoryDAO.getPreviousQuantity(productId);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        int newQuantity = previousQuantity + quantity;
        
     try {
            inventoryDAO.updateProductQuantity(productId, newQuantity);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }   
     
    }
}
