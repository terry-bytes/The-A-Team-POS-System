package ateam.Servlets;

import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.Models.Employee;
import ateam.Service.EmployeeService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "VerifyOTPServlet", urlPatterns = "/verifyOTP")
public class VerifyOTPServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String inputOtp = request.getParameter("otp");
        String generatedOtp = (String) request.getSession().getAttribute("otp");
        Employee newEmployee = (Employee) request.getSession().getAttribute("newEmployee");

        if (inputOtp.equals(generatedOtp)) {
            EmployeeService employeeService = new EmployeeServiceImpl(new EmployeeDAOIMPL());
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

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/verifyOTP.jsp").forward(request, response);
    }
}
