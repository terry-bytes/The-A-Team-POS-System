/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.DTO.StorePerfomanceInSales;
import ateam.DTO.TopProductDTO;
import ateam.DTO.TopSellingEmployee;
import ateam.DTO.TopSellingEmployeeDTO;
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
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");
        
        Map<String, StorePerfomanceInSales> getTopAchievingStores;
        getTopAchievingStores = reports.getTopAchievingStores();
        Map<String, BigDecimal> generateMonthReportForStore = reports.getMonthSalesReport(manager.getStore_ID(), LocalDate.now());
        List<Store> stores = storeService.getAllStores();
        Map<String, Integer> topSellingEmployees = reports.generateTopSellingEmployees();
        Map<String, BigDecimal> leastPerformingStore = reports.leastPerformingStores(3, 40.0);
        Map<String, BigDecimal> todaysSales = reports.getTodaysReportForAllStores();
        List<TopProductDTO> topProduct = reports.top40SellingProducts();
        List<Product> products = productService.getAllItems();
        
        request.getSession(false).setAttribute("Products", products);
        request.getSession(false).setAttribute("top40SellingProducts", topProduct);
        request.getSession(false).setAttribute("Today'sReport", todaysSales);
        request.getSession(false).setAttribute("leastPerformingStores", leastPerformingStore);
        request.getSession(false).setAttribute("topSellingEmployee", topSellingEmployees);
        request.getSession(false).setAttribute("reportForThisMonth", generateMonthReportForStore);
        request.getSession(false).setAttribute("topAchievingStores", getTopAchievingStores);
        request.getSession(false).setAttribute("stores", stores);
        request.getRequestDispatcher("managerDashboard.jsp").forward(request, response);
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        switch(request.getParameter("submit")){
            case "getMonthReport":
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
        String dateStr = request.getParameter("date");
        System.out.println(dateStr);
        Map<String, BigDecimal> report = reports.getMonthSalesReport(storeId, LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM")));
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        // Convert report to JSON format
        out.print(new Gson().toJson(report));
        out.flush();
    }
    
    private LocalDate dateFormatter(String date) throws ParseException{
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
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
        LocalDate startDate = dateFormatter(request.getParameter("date"));
        LocalDate endDate = startDate.withDayOfMonth(startDate.getDayOfMonth());
        System.out.println("Input date:"+ startDate);
        
        Map<String, StorePerfomanceInSales> storeAchievedTarget = reports.StoreAchievedTarget(startDate, endDate);
        
        List<String> labels = storeAchievedTarget.keySet().stream().collect(Collectors.toList());
        List<StorePerfomanceInSales> data = storeAchievedTarget.values().stream().collect(Collectors.toList());
        
        Map<String, Object> jsonResponse = new TreeMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }
    
    private void handleTopSellingEmployeeBasedOnProduct(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int productId = Integer.parseInt(request.getParameter("productId"));
        TopSellingEmployeeDTO topEmp = reports.getTopSellingEmployeeForProduct(productId);

        if (topEmp != null) {
            // Assuming you have methods to get product name and teller name
            String productName = productService.getProductById(topEmp.getProductId()).getProduct_name();
            String tellerName = employeeService.getEmployeeById(topEmp.getEmployeeId()).getFirstName();

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("productName", productName);
            responseData.put("tellerName", tellerName);
            responseData.put("amountSold", topEmp.getTotalAmount());

            String json = new Gson().toJson(responseData);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
       
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
        
        Map<String, BigDecimal> leastPerformingStores = reports.getLeastsPerformingStores(endDate);
        
        List<String> labels = leastPerformingStores.keySet().stream().collect(Collectors.toList());
        List<BigDecimal> data = leastPerformingStores.values().stream().collect(Collectors.toList());
        
        Map<String, Object> jsonResponse = new TreeMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }
}
