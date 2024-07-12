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
    private Integer store_ID;
    private String employees_id;
    private String employeePassword;
    private Role role;
    private String email;

}
