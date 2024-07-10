package ateam.DAOIMPL;



import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.BDconnection.Connect;

import ateam.DAO.EmployeeDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAOIMPL implements EmployeeDAO {

    private static final String SQL_INSERT_EMPLOYEE = "INSERT INTO employees (first_name, last_name, store_ID, employees_id, employee_password, role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_EMPLOYEE_BY_ID = "SELECT * FROM employees WHERE employee_ID = ?";
    private static final String SQL_SELECT_ALL_EMPLOYEES = "SELECT * FROM employees";
    private static final String SQL_UPDATE_EMPLOYEE = "UPDATE employees SET first_name = ?, last_name = ?, store_ID = ?, employees_id = ?, employee_password = ?, role = ? WHERE employee_ID = ?";
    private static final String SQL_DELETE_EMPLOYEE = "DELETE FROM employees WHERE employee_ID = ?";

    private Connection connection;

    public EmployeeDAOIMPL() {
        Connect connect = new Connect();
        this.connection = connect.connectToDB();
    }

    @Override
    public boolean addEmployee(Employee employee) {
        boolean success = false;
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
            preparedStatement.setString(4, employee.getEmployees_id());
            preparedStatement.setString(5, employee.getEmployeePassword());
            preparedStatement.setString(6, employee.getRole().name());

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(preparedStatement);
        }

        return success;
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(resultSet, preparedStatement);
        }

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
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

                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            preparedStatement.setString(4, employee.getEmployees_id());
            preparedStatement.setString(5, employee.getEmployeePassword());
            preparedStatement.setString(6, employee.getRole().name());
            preparedStatement.setInt(7, employee.getEmployee_ID());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
            e.printStackTrace();
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
                    e.printStackTrace();
                }
            }
        }
    }
}
