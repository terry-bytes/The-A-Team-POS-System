package ateam.Servlets;

import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.Service.EmployeeService;
import ateam.ServiceImpl.EmployeeServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/employees")
public class EmployeeServlet extends HttpServlet {

    private final EmployeeService employeeService;

    public EmployeeServlet() {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAOIMPL();
            this.employeeService = new EmployeeServiceImpl(employeeDAO);
        } catch (Exception e) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, "Error initializing EmployeeService", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addEmployee(request, response);
                break;
            case "update":
                updateEmployee(request, response);
                break;
            case "delete":
                deleteEmployee(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("submit");

        if ("edit".equals(action)) {
            showEditForm(request, response);
        } else if ("addForm".equals(action)) {
            showAddForm(request, response);
        } else if ("deleteConfirm".equals(action)) {
            showDeleteConfirm(request, response);
        } else {
            listEmployees(request, response);
        }
    }

    private void listEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employees = employeeService.getAllEmployees();
        request.setAttribute("employees", employees);
        request.getRequestDispatcher("/listEmployees.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/addEmployee.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        Employee employee = employeeService.getEmployeeById(employeeId);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("/editEmployee.jsp").forward(request, response);
    }

    private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        Employee employee = employeeService.getEmployeeById(employeeId);
        request.setAttribute("employee", employee);
        request.getRequestDispatcher("/deleteEmployee.jsp").forward(request, response);
    }

    private void addEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Integer storeId = Integer.parseInt(request.getParameter("storeId"));
        String employeesId = request.getParameter("employeesId");
        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));

        Employee newEmployee = new Employee();
        newEmployee.setFirstName(firstName);
        newEmployee.setLastName(lastName);
        newEmployee.setStore_ID(storeId);
        newEmployee.setEmployees_id(employeesId);
        newEmployee.setEmployeePassword(password);
        newEmployee.setRole(role);

        boolean success = employeeService.addEmployee(newEmployee);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/employees");
        } else {
            response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        Integer storeId = Integer.parseInt(request.getParameter("storeId"));
        String employeesId = request.getParameter("employeesId");
        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));

        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setEmployee_ID(employeeId);
        employeeToUpdate.setFirstName(firstName);
        employeeToUpdate.setLastName(lastName);
        employeeToUpdate.setStore_ID(storeId);
        employeeToUpdate.setEmployees_id(employeesId);
        employeeToUpdate.setEmployeePassword(password);
        employeeToUpdate.setRole(role);

        boolean success = employeeService.updateEmployee(employeeToUpdate);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/employees");
        } else {
            response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));

        boolean success = employeeService.deleteEmployee(employeeId);

        if (success) {
            response.sendRedirect(request.getContextPath() + "/employees");
        } else {
            response.sendRedirect(request.getContextPath() + "/employees");
        }
    }
}
