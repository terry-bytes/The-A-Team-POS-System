/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import ateam.Service.EmployeeService;
import ateam.Service.SaleService2;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
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
    private EmployeeService employeeService;
    private SaleService2 saleService;
    private int targetSalesPerEmployee;

    public Reports(EmployeeService employeeService, SaleService2 saleService, int target) {
        this.employeeService = employeeService;
        this.saleService = saleService;
        this.targetSalesPerEmployee = target;
    }


    public Reports() {
        targetSalesPerEmployee = 10;
        this.employeeService = new EmployeeServiceImpl();
        this.saleService = new SaleServiceImpl();
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
}
