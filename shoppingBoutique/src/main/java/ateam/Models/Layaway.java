package ateam.Models;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Layaway {

    private int layaway_ID; 
    private int customer_ID; 
    private int employee_ID; 
    private Timestamp start_date; 
    private Timestamp expiry_date; 
    private String layaway_status; 
}
