/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Sale;
import ateam.Models.Store;
import ateam.Service.EmployeeService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import java.io.IOException;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



/**
 *
 * @author T440
 */
@WebServlet(name = "SalesDemo", urlPatterns = {"/SalesDemo"})
public class SalesDemo extends HttpServlet {
    private final SaleService2 saleService = new SaleServiceImpl();
    private final StoreService storeService = new StoreServiceImpl(new StoreDAOIMPL(new Connect().connectToDB()));
    private final EmployeeService employeeService = new EmployeeServiceImpl(new EmployeeDAOIMPL());
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        // Generate fake data, while waiting for database.
        Map<String, Integer> salesData = new HashMap<>();
        salesData.put("Midrand Branch", 189);
        salesData.put("Sandton Branch", 201);
        salesData.put("Gomora Branch", 160);
        salesData.put("Fourways Branch", 313);
        
        List<Sale> sales = saleService.getAllSales();
        List<Store> stores = storeService.getAllStores();
        List<Employee> employees = employeeService.getAllEmployees();
        
        HttpSession session = request.getSession(false);
        
        session.setAttribute("Employees", employees);
        session.setAttribute("Stores", stores);
        session.setAttribute("Sales", sales);
        
        request.setAttribute("salesData", salesData);
        request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
