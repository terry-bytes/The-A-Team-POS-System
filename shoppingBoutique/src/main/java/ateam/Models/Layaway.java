package ateam.Models;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Layaway implements Serializable {
 private static final long serialVersionUID = 1L;
    private int layaway_ID; 
    private int customer_ID; 
    private int employee_ID; 
    private Timestamp start_date; 
    private Timestamp expiry_date; 
    private String layaway_status; 
    private String customerEmail;
    private String productID;
    private int productQuantity;
    private int customerNumber;
    private String customerName;
    private String productName;
    private String productSKU;
    private double productPrice;
}
