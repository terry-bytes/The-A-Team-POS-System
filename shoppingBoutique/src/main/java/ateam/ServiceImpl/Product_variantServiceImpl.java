/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.ProductVariantsDAO;
import ateam.DAOIMPL.ProductVariantsDAOImpl;
import ateam.Models.ProductVariants;
import ateam.Service.ProductVariantsService;
import java.util.List;

/**
 *
 * @author user
 */
public class Product_variantServiceImpl implements ProductVariantsService{

    private ProductVariantsDAO productVariantsDao;

    public Product_variantServiceImpl() {
        this.productVariantsDao = new ProductVariantsDAOImpl();
    }
    
    
    
    @Override
   public List<ProductVariants> getAllVariants(){
       
      return productVariantsDao.getAllVariants();
   }    

    @Override
    public void addProductVariant(ProductVariants variant) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void updateProductVariant(ProductVariants variant) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteProductVariant(int variant_ID) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ProductVariants getProductVariantById(int variant_ID) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, String color, int store_ID) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, int store_ID) {
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
