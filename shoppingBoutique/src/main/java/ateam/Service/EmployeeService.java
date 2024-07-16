/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Service;

import ateam.Exception.EmployeeNotFoundException;
import ateam.Exception.InvalidPasswordException;
import ateam.Models.Employee;
import java.util.List;

/**
 *
 * @author Train
 */
public interface EmployeeService {

    boolean addEmployee(Employee employee);

    Employee getEmployeeById(int employee_ID);

    List<Employee> getAllEmployees();

    boolean updateEmployee(Employee employee);

    boolean deleteEmployee(int employee_ID);

    /**
     *
     * @param employee_id
     * @param password
     * @return
     * @throws EmployeeNotFoundException
     * @throws InvalidPasswordException
     */
    Employee login(String employee_id, String password)throws EmployeeNotFoundException,InvalidPasswordException;

    Employee findByEmail(String email);

    boolean updatePasswordByEmail(String email, String newPassword);
}
