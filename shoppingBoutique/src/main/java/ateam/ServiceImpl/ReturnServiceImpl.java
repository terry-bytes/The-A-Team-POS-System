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
import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.ReturnService;
import ateam.Services.impl.InventoryServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
// ReturnServiceImpl.java
public class ReturnServiceImpl implements ReturnService {
    private final ReturnDao returnDao;
    private final InventoryDAO inventoryDAO;
    private ProductDAO productDAO ;
    private SaleDAO saleDao;
    private SalesItemDAO saleItems;
    

    public ReturnServiceImpl() {
        this.returnDao= new ReturnDaoImpl();
        this.inventoryDAO = new InventoryDAOIMPL();
        this.productDAO = new ProductDAOIMPL();
        this.saleDao = new SaleDAOIMPL();
        
    }
    
    @Override
   public BigDecimal processReturn(int salesId, int productId, int quantity,String email, String reason) {
        Sale sale = getSaleById(salesId);

        if (sale != null) {
            BigDecimal unitPrice = getUnitPrice(salesId, productId);
            BigDecimal totalReturnAmount = unitPrice.multiply(new BigDecimal(quantity));

            // Update the product quantity
            updateProductQuantity(productId, quantity);
            
            Return returns = new Return();
            returns.setSales_ID(salesId);
            returns.setProduct_ID(productId);
            returns.setQuantity(quantity);
            returns.setEmail(email);
            returns.setReason(reason);
            // Add to the returns table
            addReturn(returns);
            

            // Update the sales total amount
            updateSaleTotalAmount(salesId, totalReturnAmount);

            return totalReturnAmount;
        } else {
            throw new IllegalArgumentException("Sale not found for ID: " + salesId);
        }
    }
   
    @Override
    public void handleCustomerOptions(int salesId, BigDecimal remainingAmount, String customerChoice) {
        handleCustomerOptions(salesId, remainingAmount, customerChoice, -1);
    }

    @Override
    public void handleCustomerOptions(int salesId, BigDecimal remainingAmount, String customerChoice, int selectedProductId) {
        switch (customerChoice) {
            case "Select-New-Item":
                
                if (selectedProductId != -1) {
                    Product selectedProduct = getProductById(selectedProductId);
                    SalesItem sales = new SalesItem();
                    sales.setSales_ID(salesId);
                    double item =selectedProduct.getProduct_price(); 
                    sales.setUnit_price(BigDecimal.valueOf(item));
                    sales.setProduct_ID(selectedProduct.getProduct_ID());
                    addSalesItem(sales);
                    updateProductQuantity(selectedProduct.getProduct_ID(), -1);
                }
                break;

            case "Receive-Change":
                BigDecimal big = getUnitPrice(salesId,selectedProductId);
                // Implement logic to provide change to the customer
                break;

            default:
                throw new IllegalArgumentException("Invalid customer choice: " + customerChoice);
        }
    }
    
    @Override
    public Sale getSaleById(int sales_ID) {
        //String storeNam = storeServi.getStoreById
        return returnDao.getSaleById(sales_ID);
    }

   

    public void updateSaleTotalAmount(int sales_ID, BigDecimal returnAmount) {
        Sale sale = getSaleById(sales_ID);
        boolean success = false;
        BigDecimal newTotalAmount =BigDecimal.ZERO;
        if (sale != null) {
            
            newTotalAmount = (saleDao.calculateTotalAmount(sales_ID).subtract(returnAmount));
            
            if(saleDao.updateTotalAmount(sales_ID, newTotalAmount)){
                success =true;
                System.out.println(success);
            }
        }
    }

    @Override
    public BigDecimal getUnitPrice(int salesId, int productId) {
        
     return returnDao.getUnitPrice(salesId, productId);
    }

    @Override
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
    @Override
    public void addReturn(Return returns) {
        
         returnDao.insertReturn(returns);
    }

    

    @Override
    public List<Product> getProductsByPrice(BigDecimal price) {
       
        return returnDao.getProductsByPrice(price);
    }

    @Override
    public void addSalesItem(SalesItem sales) {
        
      saleItems.saveSalesItem(sales);
    }
    

    @Override
    public Product getProductById(int productId) {
        
     return returnDao.getProductById(productId);
    }
    @Override
    public SalesItem getSalesItemById(int salesItemId) {
        
        
     return  returnDao.getSalesItemById(salesItemId);
    }
    @Override
    public List<SalesItem> getSalesItemsBySaleId(int salesId) {
        
        

      return returnDao.getSalesItemsBySaleId(salesId);
    }

    
}