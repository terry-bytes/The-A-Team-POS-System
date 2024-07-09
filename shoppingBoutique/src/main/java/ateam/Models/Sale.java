package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {

    private int sales_ID; 
    private Timestamp sales_date; 
    private double total_amount; 
    private String payment_method; 
    private int customer_ID; 
    private int employee_ID;
    private int store_ID; 
    private int layaway_ID; 
}
