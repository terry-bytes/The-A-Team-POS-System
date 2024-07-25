/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import ateam.DTO.TopProductSellEmployee;
import ateam.DTO.TopSellingEmployee;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class Reports {
    private final EmployeeService employeeService;
    private final SaleService2 saleService;
    private final int targetSalesPerEmployee;
    private final StoreService storeService;
    private final ProductService productService;
    

    public Reports(EmployeeService employeeService, SaleService2 saleService, int target, StoreService storeService, ProductService productService) {
        this.employeeService = employeeService;
        this.saleService = saleService;
        this.targetSalesPerEmployee = target;
        this.storeService = storeService;
        this.productService = productService;
    }

    public Reports() {
        targetSalesPerEmployee = 10;
        this.employeeService = new EmployeeServiceImpl();
        this.saleService = new SaleServiceImpl();
        this.storeService = new StoreServiceImpl();
        this.productService = new ProductServiceImpl();
    }
    
    public Map<String, Integer> generateTopSellingEmployees(){
       return topSellingEmployees(saleService.getAllSales());
    }
    
    public Map<String, Integer> generateTopSellingEmployees(int storeId){
        return topSellingEmployees(saleService.getAllSalesByStoreId(storeId));
    }
    
    public Map<String, Integer> generateMonthReportForStore(int storeId, LocalDate date){
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        // Fetch the sales data
        List<Sale> sales = saleService.getAllSalesByStoreId(storeId); 
 
        Map<String, Integer> salesForMonth = sales.stream()
                .filter(s -> {
                    LocalDate saleDate = s.getSales_date().toLocalDateTime().toLocalDate();
                    return saleDate.isAfter(startOfMonth)&& saleDate.isBefore(endOfMonth);
                })
                .collect(Collectors.groupingBy((Sale sale) -> sale.getSales_date().toLocalDateTime().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        TreeMap::new,
                Collectors.summingInt(s -> 1)
            ));
        
        return salesForMonth;
    }
    
    private Map<String, Integer> topSellingEmployees(List<Sale> sales){
        Map<String, Integer> topSellingEmployees = new TreeMap<>();
        Map<Integer, Integer> employeeSales = new HashMap<>();
        for(Sale sale : sales){
           int employee_Id = sale.getEmployee_ID();
           employeeSales.put(employee_Id, employeeSales.getOrDefault(employee_Id, 0)+ 1);
        }
        
        for(Employee employee : employeeService.getAllEmployees()){
            if(employee.getRole() == Role.Teller){
                int employee_Id = employee.getEmployee_ID();
                String employeeName = employee.getFirstName();
                int totalSales = employeeSales.getOrDefault(employee_Id, 0);
                if(totalSales >= targetSalesPerEmployee)
                    topSellingEmployees.put(employeeName, totalSales);
            }
        }
        return topSellingEmployees;
    }
    
    /**
     * Report of all the stores that have achieved the target for a particular month
     * what is the target of the month?
     * is it same for all months?
     * I need to calculate the total sales for each store for the selected month
     * 
     * @params sales
     * Group store by name @param store id and store name
     * 
     * Build a report for 
     * @param month
     * @param target
     * @return Map<String, Integer>
     * String Map represent the name of the store Integer represent the total number of sales
    */
    
    public Map<String, Integer> StoreAchievedTarget(LocalDate month, int target){
        List<Store> stores = storeService.getAllStores();
        List<Sale> sales = saleService.getAllSales();
        LocalDate firstDay = month.withDayOfMonth(1);
        LocalDate lastDay = month.withDayOfMonth(month.lengthOfMonth());
        
        Map<Integer, Long> salesByStore = sales.stream()
                .filter(sale -> {
                    LocalDate saleDate = sale.getSales_date().toLocalDateTime().toLocalDate();
                    return saleDate.isAfter(firstDay) && saleDate.isBefore(lastDay);
                })
                .collect(Collectors.groupingBy(
                        Sale::getStore_ID,
                        TreeMap::new,
                        Collectors.counting()));
        
        Map<Integer, String> storeNames = stores.stream()
                .collect(Collectors.toMap(
                        Store::getStore_ID,
                        Store::getStore_name));
        
        return salesByStore.entrySet().stream()
                .filter(sale -> sale.getValue() >= target)
                .collect(Collectors.toMap(
                        entry -> storeNames.get(entry.getKey()), 
                        entry -> entry.getValue().intValue()));
    }
    
    /**
     * Generate the on the top 40 highest selling products and which store sold the most
     * first, calculate the total sales per
     *
     */
    
    
    /**
     * Generate a report for chosen product, how much was sold and the top sells person for that product across the stores nationwide
     * from these list get List<Sale> and List<SalesItem> 
     * calculate the total sales for that product interms of items e.g total is 210; 
     * 
     * 
     * @param productId
     * @return TopEmployeeforProduct
     */
    
    public TopSellingEmployee topSellingEmployeeForProduct(int productId){
        SaleItemsService saleItems = new SaleItemServiceImpl();
        TopSellingEmployee topSellingEmployee = new TopSellingEmployee();
        List<SalesItem> salesItems = saleItems.getSalesItemsByProductId(2);
        List<Sale> sales = saleService.getAllSales();
        List<TopProductSellEmployee> topProductSellingemployees = saleItems.getTotalSalesPerProduct().stream()
                .filter(s -> s.getProductId() == productId)
                .collect(Collectors.toList());
        
        
        for(TopProductSellEmployee tp : topProductSellingemployees){
            int tpSales = tp.getTotalSales();
            if(tpSales > topSellingEmployee.getTotalSales()){
                topSellingEmployee.setTotalSales(tpSales);
                topSellingEmployee.setEmployeeName(tp.getEmployeeName());
            }
        }
        
//        List<Sale> salesWithProductId = sales.stream()
//                .filter(sale -> salesItems.stream()
//                        .anyMatch(item -> item.getSales_ID() == sale.getSales_ID()))
//                .collect(Collectors.toList());
//        
//        //grouping by sales id e.g key=2, value will be the list of items sold on that sales
//        Map<Integer, List<SalesItem>> salesItemsBySalesId = salesItems.stream()
//                .collect(Collectors.groupingBy(SalesItem::getSales_ID));
//        
        BigDecimal totalQuantitySold = salesItems.stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        topSellingEmployee.setTotalSalesForProduct(totalQuantitySold);
    return topSellingEmployee;
    }
    
    public String generateDailySaleReport(int storeId) {
        // Assuming saleService is injected or instantiated elsewhere
        List<Sale> currentSales = saleService.getDailyStoreByStoreId(storeId);
        int target =10000;
        // Calculate total sales amount
        BigDecimal totalSalesAmount = calculateTotalSalesAmount(currentSales);
        
        // Calculate percentage of daily sales achieved
        BigDecimal percentageAchieved = calculatePercentage(totalSalesAmount, target);
        
        // Determine how many percentages away from reaching target
        BigDecimal percentageToTarget = BigDecimal.valueOf(100).subtract(percentageAchieved);
        
        // Format and return the report
        String report = String.format("Daily Sales Report for Store %d:\n", storeId);
        report += String.format("Total Sales Amount: R%.2f\n", totalSalesAmount);
        report += String.format("Percentage of Daily Target Achieved: %.2f%%\n", percentageAchieved);
        report += String.format("Percentage Away from Daily Target: %.2f%%\n", percentageToTarget);
        
        return report;
    }
    
    private BigDecimal calculatePercentage(BigDecimal totalSales, int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("Target must be greater than zero");
        }
        BigDecimal percentage = totalSales.divide(BigDecimal.valueOf(target), 4, BigDecimal.ROUND_HALF_UP)
                                         .multiply(BigDecimal.valueOf(100));
        return percentage;
    }
    
    private BigDecimal calculateTotalSalesAmount(List<Sale> sales) {
        BigDecimal total = BigDecimal.ZERO;
        for (Sale sale : sales) {
            total = total.add(sale.getTotal_amount());
        }
        return total;
    }
    
}
