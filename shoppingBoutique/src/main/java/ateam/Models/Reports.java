/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import ateam.Service.EmployeeService;
import ateam.Service.SaleService2;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Admin
 */
public class Reports {
    private EmployeeService employeeService;
    private SaleService2 saleService;
    private int targetSalesPerEmployee;

    public Reports(EmployeeService employeeService, SaleService2 saleService) {
        this.employeeService = employeeService;
        this.saleService = saleService;
    }


    public Reports() {
        targetSalesPerEmployee = 10;
    }
    
    public Map<String, Integer> generateTopSellingEmployees(){
        Map<String, Integer> topSellingEmployees = new TreeMap<>();
        Map<Integer, Integer> employeeSales = new HashMap<>();
        for(Sale sale : saleService.getAllSales()){
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
    
    public Map<String, Integer> generateMonthReportForStore(int storeId, LocalDate date){
        
        // Define the start and end of the month
        LocalDate startOfMonth = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        // Fetch the sales data
        List<Sale> sales = saleService. // Assuming fetchSalesData() retrieves the sales data
        
        // Filter the sales based on the storeId and the date range
        Map<String, Integer> salesForMonth = sales.stream()
            .filter(sale -> sale.getStoreId() == storeId)
            .filter(sale -> !sale.getDate().isBefore(startOfMonth) && !sale.getDate().isAfter(endOfMonth))
            .collect(Collectors.groupingBy(
                sale -> sale.getDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                Collectors.summingInt(Sale::getAmount)
            ));
        
        return salesForMonth;
  
    }
}
