package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Store {

    private int store_ID; 
    private String store_name; 
    private String store_address; 
    private String store_city; 
    private String store_province; 
    private int store_zipcode; 
    private String store_phone; 
    private String store_email; 
    
    
}
