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

    public ProductServiceImpl(ProductDAO productDAO) {

        this.productDAO = productDAO;
        
 
    }

    public ProductServiceImpl() {
        this.productDAO =new ProductDAOIMPL();
        
    }
    
    

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
