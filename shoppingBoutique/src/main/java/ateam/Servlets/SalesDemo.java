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
import ateam.Models.SalesItem;
import ateam.Models.Store;
import ateam.Service.EmployeeService;
import ateam.Service.ProductService;
import ateam.Service.SaleItemsService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.SaleItemServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStream;
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

@WebServlet(name = "SalesDemo", urlPatterns = {"/SalesDemo"})
public class SalesDemo extends HttpServlet {

    private final SaleService2 saleService = new SaleServiceImpl();
    private final StoreService storeService = new StoreServiceImpl(new StoreDAOIMPL(new Connect().connectToDB()));
    private final EmployeeService employeeService = new EmployeeServiceImpl(new EmployeeDAOIMPL());
    private final Reports reports = new Reports();
    private final ProductService productService = new ProductServiceImpl();
    SaleItemsService saleItemsService = new SaleItemServiceImpl();

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
        Map<Integer, Integer> salesInHour = reports.hourlySales(manager.getStore_ID());

        System.out.println("MonthReport for my store: "+ generateMonthReportForStore.size());
        System.out.println("least performing store: "+ leastPerformingStore.size());
        
        request.getSession(false).setAttribute("SalesRate", salesInHour);
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
        switch (request.getParameter("submit")) {
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
            case "downloadReport":
                handleDownloadRequest(request, response);
                break;
            case "salesAnalytics":
                handleSalesAnalytics(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void handleMonthReport(HttpServletRequest request, HttpServletResponse response) throws ParseException, ServletException, IOException {
        int storeId = Integer.parseInt(request.getParameter("storeId"));
        String dateStr = request.getParameter("date");

        Map<String, BigDecimal> report = reports.getMonthSalesReport(storeId, LocalDate.parse(dateStr + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("labels", report.keySet().toArray(new String[0])); // Convert keys to array for labels
        responseData.put("data", report.values().toArray(new BigDecimal[0]));

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(mapper.writeValueAsString(responseData));
    }

    private LocalDate dateFormatter(String date) throws ParseException {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    private void handleRequestTopEmployeeByStore(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

    private void handleStoreAchieveTarget(HttpServletRequest request, HttpServletResponse response) throws ParseException, IOException {
        LocalDate startDate = dateFormatter(request.getParameter("date"));
        LocalDate endDate = startDate.withDayOfMonth(startDate.getDayOfMonth());

        Map<String, StorePerfomanceInSales> storeAchievedTarget = reports.StoreAchievedTarget(startDate, endDate);

        List<String> labels = storeAchievedTarget.keySet().stream().collect(Collectors.toList());
        List<StorePerfomanceInSales> data = storeAchievedTarget.values().stream().collect(Collectors.toList());

        Map<String, Object> jsonResponse = new TreeMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }

    private void handleTopSellingEmployeeBasedOnProduct(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int productId = Integer.parseInt(request.getParameter("productId"));
        TopSellingEmployeeDTO topEmp = reports.getTopSellingEmployeeForProduct(productId);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Convert the list to JSON
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonResponse = mapper.writeValueAsString(topEmp);

        // Send the response back to the client
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }

    private void handleCurrentSalesBasedOnStore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rs = reports.generateDailySaleReport(Integer.parseInt(request.getParameter("storeId")));

        String result = rs;

        response.setContentType("text/html");
        response.getWriter().write(result);
    }

    private void handleGetLeastPerformingStore(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LocalDate today = LocalDate.now();
        Map<String, BigDecimal> leastPerformingStores = reports.leastPerformingStores(3, 40.0);

        List<String> labels = leastPerformingStores.keySet().stream().collect(Collectors.toList());
        List<BigDecimal> data = leastPerformingStores.values().stream().collect(Collectors.toList());

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("labels", labels);
        jsonResponse.put("data", data);

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(jsonResponse));
    }

    private void handleDownloadRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment;filename=sales_items_report.csv");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Sales Item ID,Sales ID,Product ID,Quantity,Unit Price");

            List<SalesItem> salesItems = saleItemsService.getAllSalesItems();

// Print out the sales items
            for (SalesItem salesItem : salesItems) {
                writer.printf("%d,%d,%d,%d,%.2f%n",
                        salesItem.getSales_item_ID(),
                        salesItem.getSales_ID(),
                        salesItem.getProduct_ID(),
                        salesItem.getQuantity(),
                        salesItem.getUnit_price());
            }
        }
    }

    private void handleSalesAnalytics(HttpServletRequest request, HttpServletResponse response) throws IOException{
        int storeId = Integer.parseInt(request.getParameter("storeId"));
        
        Map<Integer, Integer> salesInHour = reports.hourlySales(storeId);
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> responseData = new TreeMap<>();
        responseData.put("labels", salesInHour.keySet().toArray(new Integer[0])); // Convert keys to array for labels
        responseData.put("data", salesInHour.values().toArray(new Integer[0]));
        
         response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        // Write the response data as JSON
        mapper.writeValue(response.getWriter(), responseData);
    }
}
