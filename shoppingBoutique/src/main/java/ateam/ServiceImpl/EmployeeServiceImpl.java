package ateam.ServiceImpl;

import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Exception.EmployeeNotFoundException;
import ateam.Exception.InvalidPasswordException;
import ateam.Models.Employee;
import ateam.Service.EmployeeService;
import java.util.Comparator;


import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mindrot.jbcrypt.BCrypt;

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeDAO employeeDAO;


    public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
        
    }
    
    public EmployeeServiceImpl(){
        this.employeeDAO = new EmployeeDAOIMPL();
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
    public Employee login(String employee_id, String password) throws EmployeeNotFoundException,InvalidPasswordException {
        
        
        Employee employee = employeeDAO.getEmployee(employee_id);
        
        
//         
        if (employee != null) {
            if (BCrypt.checkpw(password, employee.getEmployeePassword())) {
                return employee;
            } else {
                throw new InvalidPasswordException("Incorrect password");
            }
        }
        throw new EmployeeNotFoundException("Employee not found");
        
        
    }
        @Override
    public Employee findByEmail(String email) {
        return employeeDAO.findByEmail(email);
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newPassword) {
        return employeeDAO.updatePasswordByEmail(email, newPassword);
    }

    @Override
    public List<Employee> managersEmployee(int storeId) {
        return employeeDAO.getAllEmployees().stream()
                .filter(employee -> employee.getStore_ID() == storeId)
                .sorted(Comparator.comparing(Employee::getFirstName))
                .collect(Collectors.toList());
    }
}
