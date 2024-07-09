package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private int employee_ID;  
    private String firstName;
    private String lastName;
    private String store_ID;  
    private String employeeIdVar;  
    private String employeePassword;
    private boolean isManager;
}
