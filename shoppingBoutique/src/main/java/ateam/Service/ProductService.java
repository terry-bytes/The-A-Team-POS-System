/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package ateam.Service;

import ateam.Models.Product;
import java.util.List;

/**
 *
 * @author user
 */
public interface ProductService {
    
    
    List<Product>getAllItems ();
    boolean InsertItems(Product product);
    
    
    
}
