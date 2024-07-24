/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import ateam.Service.EmployeeService;
import ateam.Service.ProductService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
     * 
     * 
     * 
     * @param productId
     * @return TopEmployeeforProduct
     */
    
    public Map<String, Integer> topSellingEmployeeForProduct(int productId){
        List<SalesItem> saleItems = new SalesItemServiceImpl()
    }
}
