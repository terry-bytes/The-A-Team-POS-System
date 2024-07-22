package ateam.Models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariants {
    
    private int variant_ID;
    private String product_SKU;
    private String size;
    private String color;
    private int store_ID;

}
