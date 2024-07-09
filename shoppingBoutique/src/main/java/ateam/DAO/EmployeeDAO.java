package ateam.DAO;


import ateam.Models.Employee;
import java.util.List;

public interface EmployeeDAO {

    void addEmployee(Employee employee);

    Employee getEmployeeById(int employee_ID);

    List<Employee> getAllEmployees();

    void updateEmployee(Employee employee);

    void deleteEmployee(int employee_ID); 
}
