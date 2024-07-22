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
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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
        
        
        Map<String, Integer> salesData = new HashMap<>();
        salesData.put("Midrand Branch", 189);
        salesData.put("Sandton Branch", 201);
        salesData.put("Gomora Branch", 160);
        salesData.put("Fourways Branch", 313);
        
        List<Sale> sales = saleService.getAllSales();
        List<Store> stores = storeService.getAllStores();
        List<Employee> employees = employeeService.getAllEmployees();
        Map<String, Integer> topSellingEmployee = saleService.generateTopSellingEmployee(employees);
        
        HttpSession session = request.getSession(false);
        
        session.setAttribute("Employees", employees);
        session.setAttribute("Stores", stores);
        session.setAttribute("Sales", sales);
        session.setAttribute("topSellingEmp", topSellingEmployee);
        
        request.setAttribute("salesData", salesData);
        request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        switch(request.getParameter("submit")){
            case "filter":
            
                try {
                    handleMonthReport(request, response);
                } catch (ParseException ex) {
                    Logger.getLogger(SalesDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "topEmpByStore":
                int storeId = Integer.parseInt(request.getParameter("storeId"));
                List<Employee> employees = employeeService.getAllEmployees();
                Map<String, Integer> topSellingEmpByStore = saleService.generateTopSellingEmployee(storeId, employees);
                
                List<String> labels = topSellingEmpByStore.keySet().stream().collect(Collectors.toList());
                List<Integer> data = topSellingEmpByStore.values().stream().collect(Collectors.toList());
                
                Map<String, Object> jsonResponse = new HashMap<>();
                jsonResponse.put("labels", labels);
                jsonResponse.put("data", data);
                
                response.setContentType("application/json");
                response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
                break;
        }
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

    private void handleMonthReport(HttpServletRequest request, HttpServletResponse response) throws ParseException, ServletException, IOException{
        int storeId = Integer.parseInt(request.getParameter("storeId"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormatter(request.getParameter("date")));
        
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        
        Map<String, Integer> report = saleService.generateStoreMonthReport(storeId, month, year);
        
        Gson gson = new Gson();
        String json = gson.toJson(report);
        
         if (json == null || json.isEmpty()) {
            json = "{}"; // Set to empty JSON object if there's no data
        }
        
        HttpSession session = request.getSession(false);
        session.setAttribute("report", report);
        request.setAttribute("monthlReport", json);
        response.sendRedirect("SalesDemo");
    }
    
    private Date dateFormatter(String date) throws ParseException{
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM");
        return inputFormat.parse(date);
    }
}
