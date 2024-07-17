package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAO.EmployeeDAO;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.Models.Email;
import ateam.Models.Employee;
import ateam.Models.Store;
import ateam.Service.EmailService;
import ateam.Service.EmployeeService;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = "/verifyOTP")
public class VerifyOTPServlet extends HttpServlet {
    
    private final EmployeeService employeeService;

    private final StoreService storeService;

    private final EmailService emailService;
    

    public VerifyOTPServlet() {
        
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
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("otp");
        Employee newEmployee = (Employee) request.getSession().getAttribute("newEmployee");
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");
        boolean success = employeeService.addEmployee(newEmployee);

        if (inputOtp.equals(generatedOtp)) {
            //EmployeeService employeeService = new EmployeeServiceImpl(new EmployeeDAOIMPL());
            
            if (success) {
                Employee employee = employeeService.findByEmail(newEmployee.getEmail());
                Store stores = storeService.getStoreById(manager.getStore_ID());

                String msg = "Dear " + newEmployee.getFirstName() + " " + newEmployee.getLastName() + ",\nWelcome aboard! Your employee ID is " + employee.getEmployees_id()
                        + ". We're excited to have you on our team.\n\n\n\nBest regards,\n"
                        + manager.getFirstName() + " " + manager.getLastName() + "\n"
                        + manager.getRole() + "(" + stores.getStore_name() + ")\n"
                        + "Carols Boutique\n"
                        + "011 028 0987";

                Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
                emailDetails.setReceiver(newEmployee.getEmail());
                emailDetails.setSubject("Welcome to Carols Boutique! Your Registration is Complete");
                emailDetails.setMessage(msg);

                emailService.sendMail(emailDetails);
                request.setAttribute("addEmployeeMessage", "Employee added successfully");
            }
            response.sendRedirect(request.getContextPath() + "/employees?submit=getAddEmployee");
        } else {
            request.setAttribute("otpMessage", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
    }
}
