package ateam.DAOIMPL;


import ateam.BDconnection.Connect;
import ateam.DAO.EmployeeDAO;
import ateam.Models.Employee;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOIMPL implements EmployeeDAO {


    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO employees (first_name, last_name, store_ID, employees_id, employee_password, is_manager) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE employee_ID = ?";
    private static final String SQL_SELECT_ALL_EMPLOYEES = "SELECT * FROM employees";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE employees SET first_name = ?, last_name = ?, store_ID = ?, employees_id = ?, employee_password = ?, is_manager = ? WHERE employee_ID = ?";
    private static final String SQL_DELETE_EMPLOYEE = "DELETE FROM employees WHERE employee_ID = ?";

    private final Connect dbConnect;

    public EmployeeDAOIMPL(Connect dbConnect) {
        this.dbConnect = dbConnect;
    }

    @Override
    public void addEmployee(Employee employee) {
        try (Connection conn = dbConnect.connectToDB();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_EMPLOYEE)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getStore_ID());
            stmt.setString(4, employee.getEmployeeIdVar());
            stmt.setString(5, employee.getEmployeePassword());
            stmt.setBoolean(6, employee.isManager());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(); 
        }
    }

    @Override
    public Employee getEmployeeById(int employee_ID) {
        Employee employee = null;
        try (Connection conn = dbConnect.connectToDB();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_EMPLOYEE_BY_ID)) {
            stmt.setInt(1, employee_ID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                employee = new Employee();
                employee.setEmployee_ID(rs.getInt("employee_ID"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setStore_ID(rs.getString("store_ID"));
                employee.setEmployeeIdVar(rs.getString("employees_id"));
                employee.setEmployeePassword(rs.getString("employee_password"));
                employee.setManager(rs.getBoolean("is_manager"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection conn = dbConnect.connectToDB();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_EMPLOYEES)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmployee_ID(rs.getInt("employee_ID"));
                employee.setFirstName(rs.getString("first_name"));
                employee.setLastName(rs.getString("last_name"));
                employee.setStore_ID(rs.getString("store_ID"));
                employee.setEmployeeIdVar(rs.getString("employees_id"));
                employee.setEmployeePassword(rs.getString("employee_password"));
                employee.setManager(rs.getBoolean("is_manager"));
                employees.add(employee);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); 
        }
        return employees;
    }

    @Override
    public void updateEmployee(Employee employee) {
        try (Connection conn = dbConnect.connectToDB();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_EMPLOYEE)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getStore_ID());
            stmt.setString(4, employee.getEmployeeIdVar());
            stmt.setString(5, employee.getEmployeePassword());
            stmt.setBoolean(6, employee.isManager());
            stmt.setInt(7, employee.getEmployee_ID());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(); 
        }
    }

    @Override
    public void deleteEmployee(int employee_ID) {
        try (Connection conn = dbConnect.connectToDB();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_EMPLOYEE)) {
            stmt.setInt(1, employee_ID);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(); 
        }
    }
}