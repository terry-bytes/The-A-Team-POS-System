/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.ProductDAO;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.Models.Product;
import ateam.Service.ProductService;
import java.util.List;

/**
 *
 * @author user
 */
public class ProductServiceImpl implements ProductService {
    private ProductDAO productDAO;

    public ProductServiceImpl(ProductDAO productDao) {
        
        this.productDAO = productDao;
        productDao = new ProductDAOIMPL();
        
    }
    
    
    

    @Override
    public List<Product> getAllItems() {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean InsertItems(Product product) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
