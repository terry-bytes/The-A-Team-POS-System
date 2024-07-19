package ateam.Models;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    private int Sales_ID; 
    private Timestamp Sales_date; 
    private BigDecimal Total_amount; 
    private String Payment_method;
    private int Employee_ID;
    private int Store_ID; 

  
}
