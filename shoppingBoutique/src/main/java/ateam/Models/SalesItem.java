package ateam.Models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalesItem {

    private int sales_item_ID; 
    private int sales_ID; 
    private int product_ID; 
    private int quantity; 
    private double unit_price; 
}
