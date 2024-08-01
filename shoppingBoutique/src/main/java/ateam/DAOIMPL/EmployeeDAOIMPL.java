package ateam.DAOIMPL;

import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.DAO.EmployeeDAO;
import ateam.BDconnection.Connect;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeDAOIMPL implements EmployeeDAO {

    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO employees (first_name, last_name, store_ID, employee_password, role, email) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE employee_ID = ?";
    private static final String SQL_SELECT_ALL_EMPLOYEES = "SELECT * FROM employees";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE employees SET first_name = ?, last_name = ?, store_ID = ?, employee_password = ?, role = ?, email = ? WHERE employee_ID = ?";
    private static final String SQL_DELETE_EMPLOYEE = "DELETE FROM employees WHERE employee_ID = ?";
    private static final String SQL_SELECT_EMPLOYEE_BY_EMAIL = "SELECT * FROM employees WHERE email = ?";
    private static final String SQL_UPDATE_EMPLOYEE_PASSWORD = "UPDATE employees SET employee_password = ? WHERE email = ?";
    private static final String SQL_GENERATE_EMPLOYEE_ID = "SELECT CONCAT(LEFT(first_name, 1), LEFT(last_name, 1), LPAD(FLOOR(RAND() * 10000), 4, '0')) AS employees_id FROM dual";

    private Connection connection;

    public EmployeeDAOIMPL() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public boolean addEmployee(Employee employee) {
        
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_INSERT_EMPLOYEE);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            if (employee.getStore_ID() != null) {
                preparedStatement.setInt(3, employee.getStore_ID());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            preparedStatement.setString(4, employee.getEmployeePassword());
            preparedStatement.setString(5, employee.getRole().name());
            preparedStatement.setString(6, employee.getEmail());

           return preparedStatement.executeUpdate()>0;
        } catch (SQLException e) {
        } finally {
            close(preparedStatement);
        }

        return false;
    }

    @Override
    public Employee getEmployeeById(int employee_ID) {
        Employee employee = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_EMPLOYEE_BY_ID);
            preparedStatement.setInt(1, employee_ID);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setEmployee_ID(resultSet.getInt("employee_ID"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                Integer storeID = resultSet.getObject("store_ID", Integer.class);
                employee.setStore_ID(storeID);
                employee.setEmployees_id(resultSet.getString("employees_id"));
                employee.setEmployeePassword(resultSet.getString("employee_password"));
                employee.setRole(Role.valueOf(resultSet.getString("role")));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
        } finally {
            close(resultSet, preparedStatement);
        }

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList();
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(SQL_SELECT_ALL_EMPLOYEES);

            while (resultSet.next()) {
                Employee employee = new Employee();
                employee.setEmployee_ID(resultSet.getInt("employee_ID"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                Integer storeID = resultSet.getObject("store_ID", Integer.class);
                employee.setStore_ID(storeID);
                employee.setEmployees_id(resultSet.getString("employees_id"));
                employee.setEmployeePassword(resultSet.getString("employee_password"));
                employee.setRole(Role.valueOf(resultSet.getString("role")));
                employee.setEmail(resultSet.getString("email"));

                employees.add(employee);
            }
        } catch (SQLException e) {
        } finally {
            close(resultSet, statement);
        }

        return employees;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_EMPLOYEE);
            preparedStatement.setString(1, employee.getFirstName());
            preparedStatement.setString(2, employee.getLastName());
            if (employee.getStore_ID() != null) {
                preparedStatement.setInt(3, employee.getStore_ID());
            } else {
                preparedStatement.setNull(3, Types.INTEGER);
            }
            preparedStatement.setString(4, employee.getEmployeePassword());
            preparedStatement.setString(5, employee.getRole().name());
            preparedStatement.setString(6, employee.getEmail());
            preparedStatement.setInt(7, employee.getEmployee_ID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            }
        } catch (SQLException e) {
        } finally {
            close(preparedStatement);
        }

        return success;
    }

    @Override
    public boolean deleteEmployee(int employee_ID) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_DELETE_EMPLOYEE);
            preparedStatement.setInt(1, employee_ID);

            int rowsDeleted = preparedStatement.executeUpdate();
            if (rowsDeleted > 0) {
                success = true;
            }
        } catch (SQLException e) {
        } finally {
            close(preparedStatement);
        }

        return success;
    }

    @Override
    public Employee getEmployee(String employee_id) {
        if (connection == null) {
            return null;
        }
        Employee employee = new Employee();
        String sql = "SELECT employee_ID, first_name, last_name, store_ID, employee_password, role, email"
                + " FROM employees"
                + " WHERE employees_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, employee_id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                   
                        employee.setEmployee_ID(resultSet.getInt("employee_ID"));
                        employee.setFirstName(resultSet.getString("first_name"));
                        employee.setLastName(resultSet.getString("last_name"));
                        employee.setStore_ID(resultSet.getInt("store_ID"));
                        employee.setEmployees_id(employee_id);
                        employee.setEmployeePassword(resultSet.getString("employee_password"));
                        employee.setRole(Enum.valueOf(Role.class, resultSet.getString("role")));
                        employee.setEmail(resultSet.getString("email"));
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return employee;
    }

    @Override
    public Employee findByEmail(String email) {
        Employee employee = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_SELECT_EMPLOYEE_BY_EMAIL);
            preparedStatement.setString(1, email);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setEmployee_ID(resultSet.getInt("employee_ID"));
                employee.setFirstName(resultSet.getString("first_name"));
                employee.setLastName(resultSet.getString("last_name"));
                Integer storeID = resultSet.getObject("store_ID", Integer.class);
                employee.setStore_ID(storeID);
                employee.setEmployees_id(resultSet.getString("employees_id"));
                employee.setEmployeePassword(resultSet.getString("employee_password"));
                employee.setRole(Role.valueOf(resultSet.getString("role")));
                employee.setEmail(resultSet.getString("email"));
            }
        } catch (SQLException e) {
        } finally {
            close(resultSet, preparedStatement);
        }

        return employee;
    }

    @Override
    public boolean updatePasswordByEmail(String email, String newPassword) {
        boolean success = false;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SQL_UPDATE_EMPLOYEE_PASSWORD);
            preparedStatement.setString(1, newPassword);
            preparedStatement.setString(2, email);

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            }
        } catch (SQLException e) {
        } finally {
            close(preparedStatement);
        }

        return success;
    }

    private void close(AutoCloseable... closeables) {
        if (closeables != null) {
            for (AutoCloseable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public List<Employee> getEmployeeByStore(int storeId) {
         if (connection == null) {
            return null;
        }
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT employee_ID, first_name, last_name, employees_id, employee_password, role, email"
                + " FROM employees"
                + " WHERE store_ID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, storeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                        Employee employee = new Employee();
                        employee.setEmployee_ID(resultSet.getInt("employee_ID"));
                        employee.setFirstName(resultSet.getString("first_name"));
                        employee.setLastName(resultSet.getString("last_name"));
                        employee.setStore_ID(storeId);
                        employee.setEmployees_id(resultSet.getString("employees_id"));
                        employee.setEmployeePassword(resultSet.getString("employee_password"));
                        employee.setRole(Enum.valueOf(Role.class, resultSet.getString("role")));
                        employee.setEmail(resultSet.getString("email"));
                    
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(employees.size());
        return employees;
    }

}
