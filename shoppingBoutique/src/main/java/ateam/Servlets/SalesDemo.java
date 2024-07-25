/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.DTO.TopSellingEmployee;
import ateam.Models.Employee;
import ateam.Models.Product;
import ateam.Models.Reports;
import ateam.Models.Sale;
import ateam.Models.Store;
import ateam.Service.EmployeeService;
import ateam.Service.ProductService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final Reports reports = new Reports();
    private final ProductService productService = new ProductServiceImpl();
    
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
        Map<String, Integer> topSellingEmployee = reports.generateTopSellingEmployees();
        List<Product> products = productService.getAllItems();
        
        HttpSession session = request.getSession(false);
        
        session.setAttribute("Products", products);
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
                handleRequestTopEmployeeByStore(request, response);
                break;
            case "storeAchievedTarget":
                try {
                    handleStoreAchieveTarget(request, response);
                } catch (ParseException ex) {
                    Logger.getLogger(SalesDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "getTopSellingEmployeeBasedOnProduct":
                handleTopSellingEmployeeBasedOnProduct(request, response);
                break;
            case "getCurrentSaleBasedOnStore":
                handleCurrentSalesBasedOnStore(request, response);
                break;
            case "getLeastPerformingStore":
                handleGetLeastPerformingStore(request, response);
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
        
        Map<String, Integer> report = reports.generateMonthReportForStore(storeId, dateFormatter(request.getParameter("date")));
        
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
    
    private LocalDate dateFormatter(String date) throws ParseException{
        return LocalDate.parse(date + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    private void handleRequestTopEmployeeByStore(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        int storeId = Integer.parseInt(request.getParameter("storeId"));
        Map<String, Integer> topSellingEmpByStore = reports.generateTopSellingEmployees(storeId);

        List<String> labels = topSellingEmpByStore.keySet().stream().collect(Collectors.toList());
        List<Integer> data = topSellingEmpByStore.values().stream().collect(Collectors.toList());

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }
    
    private void handleStoreAchieveTarget(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException{
        LocalDate date = dateFormatter(request.getParameter("date"));
        int target = 10;
        
        Map<String, Integer> storeAchievedTarget = reports.StoreAchievedTarget(date, target);
        
        List<String> labels = storeAchievedTarget.keySet().stream().collect(Collectors.toList());
        List<Integer> data = storeAchievedTarget.values().stream().collect(Collectors.toList());
        
        Map<String, Object> jsonResponse = new TreeMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }
    
    private void handleTopSellingEmployeeBasedOnProduct(HttpServletRequest request, HttpServletResponse response) throws IOException{
        TopSellingEmployee topEmp = reports.topSellingEmployeeForProduct(Integer.parseInt(request.getParameter("productId")));
        
        String result = "<p>Top Selling employee is " + topEmp.getEmployeeName() + "</p>"
                      + "<p>Total sales: " + topEmp.getTotalSales() + "</p>"
                      + "<p>Total sales for this product: " + topEmp.getTotalSalesForProduct() + "</p>";
        
        response.setContentType("text/html");
        response.getWriter().write(result);
    }
    
    private void handleCurrentSalesBasedOnStore(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String rs = reports.generateDailySaleReport(Integer.parseInt(request.getParameter("storeId")));
        
        String result =rs;
        
        response.setContentType("text/html");
        response.getWriter().write(result);
    }
    private void handleGetLeastPerformingStore(HttpServletRequest request, HttpServletResponse response) throws IOException{
        LocalDate today = LocalDate.now();
        int interval = Integer.parseInt(request.getParameter("interval"));
        
        LocalDate endDate = today.minusMonths(interval);
        
        Map<String, BigDecimal> leastPerformingStores = reports.getLeastPerformingStores(endDate);
        
        List<String> labels = leastPerformingStores.keySet().stream().collect(Collectors.toList());
        List<BigDecimal> data = leastPerformingStores.values().stream().collect(Collectors.toList());
        
        Map<String, Object> jsonResponse = new TreeMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }
}
