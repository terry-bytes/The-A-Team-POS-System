/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.ProductDAO;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.Models.Product;
import ateam.Service.InventoryService;
import ateam.Service.ProductService;
import java.util.List;
import ateam.Services.impl.InventoryServiceImpl;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author user
 */
public class ProductServiceImpl implements ProductService {

    private final InventoryService inventoryService;

    private ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDAO,InventoryService inventoryService) {

        this.productDAO = productDAO;
        this.inventoryService = inventoryService;
        
        
 
    }
   

    public ProductServiceImpl() {
        
        this.inventoryService = new InventoryServiceImpl();
        this.productDAO =new ProductDAOIMPL();
        
        
    }
    
    public void completeSale(int salesId) {
        try {
            inventoryService.processSale(salesId);
        } catch (SQLException ex) {
            Logger.getLogger(ProductServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<Product> getProductBySKU(String sku) {

        return productDAO.getProductBySKU(sku);
    }

    @Override
    public List<Product> getAllItems() {

        return productDAO.allProduct();
    }

    @Override
    public boolean InsertItems(Product product) {

        return productDAO.addProduct(product);
    }

}
