package ateam.Models;


import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SalesItem {

    private int Sales_item_ID; 
    private int Sales_ID; 
    private int Product_ID; 
    private int Quantity; 
    private BigDecimal Unit_price; 
}
