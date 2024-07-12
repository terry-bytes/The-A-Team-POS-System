package ateam.ServiceImpl;

import ateam.DAO.EmployeeDAO;
import ateam.Models.Employee;
import ateam.Service.EmployeeService;


import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;


    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
        
    }

    @Override
    public boolean addEmployee(Employee employee) {
        try {
            //employee.setEmployeePassword(PasswordUtil.encryptPassword(employee.getEmployeePassword().trim()));
            
            return employeeDAO.addEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employee getEmployeeById(int employee_ID) {
        try {
            return employeeDAO.getEmployeeById(employee_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            return employeeDAO.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        try {
            return employeeDAO.updateEmployee(employee);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int employee_ID) {
        try {
            return employeeDAO.deleteEmployee(employee_ID);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Employee login(String employee_id, String password) {
        return employeeDAO.getEmployee(employee_id, password);
    }
        @Override
    public Employee findByEmail(String email) {
        return employeeDAO.findByEmail(email);
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newPassword) {
        return employeeDAO.updatePasswordByEmail(email, newPassword);
    }
}
