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

    public Product(int product_ID, String product_name, String product_description, double product_price, int category_ID, String product_SKU, int quantity_in_stock, String product_image_path) {
        this.product_ID = product_ID;
        this.product_name = product_name;
        this.product_description = product_description;
        this.product_price = product_price;
        this.category_ID = category_ID;
        this.product_SKU = product_SKU;
        this.quantity_in_stock = quantity_in_stock;
        this.product_image_path = product_image_path;
    }

    public Product() {
    }
    
    
}
