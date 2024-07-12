package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private int product_ID; 
    private String product_name; 
    private String product_description; 
    private double product_price; 
    private int category_ID; 
    private String product_SKU; 
    private int quantity_in_stock; 
    private String product_image_path;

    
   
    
}
