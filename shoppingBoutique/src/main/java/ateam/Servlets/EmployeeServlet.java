package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;

import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.Exception.EmployeeNotFoundException;
import ateam.Exception.InvalidPasswordException;
import ateam.Models.Store;
import ateam.Service.StoreService;

import ateam.Models.Email;
import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.Service.EmailService;
import ateam.Service.EmployeeService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

@WebServlet(name = "EmployeeServlet", urlPatterns = "/employees")
public class EmployeeServlet extends HttpServlet {

    private final EmployeeService employeeService;

    private final StoreService storeService;

    private final EmailService emailService;
    
    


    public EmployeeServlet() {
        try {
            EmployeeDAO employeeDAO = new EmployeeDAOIMPL();
            this.employeeService = new EmployeeServiceImpl(employeeDAO);

            this.storeService = new StoreServiceImpl(new StoreDAOIMPL(new Connect().connectToDB()));

            this.emailService = new EmailServiceImpl();

        } catch (Exception e) {
            Logger.getLogger(EmployeeServlet.class.getName()).log(Level.SEVERE, "Error initializing EmployeeService", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("submit");

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
            case "login":
                handleLogin(request, response);
                break;
            case "verify":
                verifyOTP(request, response);
                break;
            case "forgotPassword":
                handleForgotPassword(request, response);
                break;
            case "changePassword":
                changePassword(request, response);
                break;
            case "verifyResetOTP":
                verifyResetOTP(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/employees");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("submit");

        HttpSession session = request.getSession(false);

        switch(action){
            case "getAddEmployee":
                List<Store> stores = storeService.getAllStores();
                session.setAttribute("stores", stores);
                request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
                break;

            case "edit":
                showEditForm(request, response);
                break;
            case "addForm":
                showAddForm(request, response);
                break;
            case "deleteConfirm":
                showDeleteConfirm(request, response);
                break;
            default:
                listEmployees(request, response);
                break;

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
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));
        
       String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
      


        int storeId;
        if(role == Role.Manager){
            storeId = Integer.parseInt(request.getParameter("managerStoreId"));
        }else{
            storeId = Integer.parseInt(request.getParameter("tellerStoreId"));
        }
        
        Employee newEmployee = new Employee();

        newEmployee.setFirstName(firstName);
        newEmployee.setLastName(lastName);
        newEmployee.setEmail(email);
        newEmployee.setStore_ID(storeId);
        newEmployee.setEmployeePassword(hashedPassword);
        newEmployee = employeeService.getEmployeeById(storeId);
        newEmployee.setRole(role);
        String msg = "Dear " +newEmployee.getFirstName()+" "+newEmployee.getLastName()+",\nWelcome aboard! Your employee ID is "+newEmployee.getEmployees_id()+
                    ". We're excited to have you on our team.\n";
        String otp = generateOTP();
        Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
        emailDetails.setReceiver(email);
        emailDetails.setSubject("Email Verification OTP");
        emailDetails.setMessage(msg+"\nYour OTP for email verification is: " + otp);

        emailService.sendMail(emailDetails);
        request.getSession().setAttribute("otp", otp);
        request.getSession().setAttribute("newEmployee", newEmployee);

        response.sendRedirect(request.getContextPath() + "/verifyOTP.jsp");
    }

    private String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000;
        return String.valueOf(otp);
    }

    private void updateEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        //String email = request.getParameter("email");                                               // Please add email
        String employeesId = request.getParameter("employeesId");

        String email = request.getParameter("email");
        Integer storeId = Integer.parseInt(request.getParameter("storeId"));

        String password = request.getParameter("password");
        Role role = Role.valueOf(request.getParameter("role"));
        
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("Stored hashed password: " + hashedPassword);
        
      
        Employee employeeToUpdate = new Employee();
        employeeToUpdate.setEmployee_ID(employeeId);
        employeeToUpdate.setFirstName(firstName);
        employeeToUpdate.setLastName(lastName);
        employeeToUpdate.setEmail(email);
        employeeToUpdate.setStore_ID(storeId);
        employeeToUpdate.setEmployeePassword(hashedPassword);
        employeeToUpdate.setRole(role);

        boolean success = employeeService.updateEmployee(employeeToUpdate);
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    private void deleteEmployee(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int employeeId = Integer.parseInt(request.getParameter("employeeId"));
        boolean success = employeeService.deleteEmployee(employeeId);
        response.sendRedirect(request.getContextPath() + "/employees");
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Employee employee = null;
        try {
            employee = employeeService.login(
                    request.getParameter("employeeId"),
                    request.getParameter("password"));
        } catch (EmployeeNotFoundException |InvalidPasswordException ex) {
            
            request.setAttribute("message", "You have entered wrong password or employeeID");
                       request.getRequestDispatcher("login.jsp").forward(request, response);
        } 
        if (employee != null) {
            HttpSession session = request.getSession(true);
            session.setAttribute("Employee", employee);
            Store store = storeService.getStoreById(employee.getStore_ID());
            session.setAttribute("store", store);

            switch (employee.getRole()) {
                case Admin:
                    request.getRequestDispatcher("admin.jsp").forward(request, response);
                    break;
                case Manager:
                    response.sendRedirect("SalesDemo");
                    break;
                default:
                    request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
                    break;
            }

        } else {
            request.setAttribute("message", "failed to login");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    private void handleForgotPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String otp = generateOTP();

        Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
        emailDetails.setReceiver(email);
        emailDetails.setSubject("Password Reset OTP");
        emailDetails.setMessage("Your OTP for password reset is: " + otp);

        emailService.sendMail(emailDetails);
        request.getSession().setAttribute("resetOtp", otp);
        request.getSession().setAttribute("resetEmail", email);

        response.sendRedirect(request.getContextPath() + "/verifyResetOTP.jsp");
    }

    private void verifyOTP(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("otp");
        Employee newEmployee = (Employee) request.getSession().getAttribute("newEmployee");

        if (inputOtp.equals(generatedOtp)) {
            boolean success = employeeService.addEmployee(newEmployee);
            if (success) {
                request.setAttribute("addEmployeeMessage", "Employee added successfully");
            }
            response.sendRedirect(request.getContextPath() + "/employees?submit=getAddEmployee");
        } else {
            request.setAttribute("otpMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
        }
    }

    private void verifyResetOTP(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("resetOtp");
        String email = (String) request.getSession().getAttribute("resetEmail");

        if (inputOtp.equals(generatedOtp)) {
            response.sendRedirect(request.getContextPath() + "/changePassword.jsp?email=" + email);
        } else {
            request.setAttribute("otpMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/verifyResetOTP.jsp").forward(request, response);
        }
    }

    private void changePassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String newPassword = request.getParameter("newPassword");
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
      

        boolean success = employeeService.updatePasswordByEmail(email, hashedPassword);

        if (success) {
            request.setAttribute("message", "Password changed successfully.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            request.setAttribute("message", "Error changing password.");
            request.getRequestDispatcher("/changePassword.jsp").forward(request, response);
        }
    }
}
