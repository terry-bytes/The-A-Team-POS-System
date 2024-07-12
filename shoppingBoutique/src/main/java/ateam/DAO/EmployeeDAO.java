package ateam.DAO;

import ateam.Models.Employee;
import java.util.List;

public interface EmployeeDAO {

    boolean addEmployee(Employee employee);

    Employee getEmployeeById(int employee_ID);

    List<Employee> getAllEmployees();

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(int employee_ID);
    Employee getEmployee(String employee_id, String password);
    Employee findByEmail(String email);
    boolean updatePasswordByEmail(String email, String newPassword);
    
}
