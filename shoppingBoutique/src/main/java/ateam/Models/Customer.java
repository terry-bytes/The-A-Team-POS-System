package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private int customer_ID; 
    private String customer_name; 
    private String customer_surname;
    private String customer_email; 
    private String customer_password; 
}
