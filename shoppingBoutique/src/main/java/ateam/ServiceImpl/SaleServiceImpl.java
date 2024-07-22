/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.SaleDAO;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DTO.EmployeeMonthSales;
import ateam.Models.Employee;
import ateam.Models.Role;
import ateam.Models.Sale;
import ateam.Models.Store;
import ateam.Service.SaleService2;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 *
 * @author Admin
 */
public class SaleServiceImpl implements SaleService2{
    private final SaleDAO saleDao;
 
    
    public SaleServiceImpl(){
        this.saleDao = new SaleDAOIMPL();
    }
    @Override
    public List<Sale> getAllSales() {
        return saleDao.getAllSales();
    }
    
    public Map<String, Integer> generateStoreMonthReport(int storeId, int month, int year){
        List<Sale> sales = saleDao.getSalesbyStoreId(storeId);
        
        
        Map<String, Integer> salesReport = new TreeMap<>();
        
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(year, month - 1, 1, 0, 0, 0);
        startCalendar.set(Calendar.MILLISECOND, 0);
        Timestamp startDate = new Timestamp(startCalendar.getTimeInMillis());
        
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.set(year, month, 1, 0, 0, 0);
        endCalendar.set(Calendar.MILLISECOND, 0);
        endCalendar.add(Calendar.DAY_OF_MONTH, -1);
        Timestamp endDate = new Timestamp(endCalendar.getTimeInMillis());
        
        for(Sale sale : sales){
            Timestamp saleDate = sale.getSales_date();
            if(saleDate.after(startDate) && saleDate.before(endDate)){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = dateFormat.format(saleDate);
                
                salesReport.put(dateStr, salesReport.getOrDefault(dateStr, 0) + 1);
            }
        }
        return salesReport;
    }
    
    /*
    Goal: Report for top selling employees across the company or in certain store
    UI: I think i need a form to select a store but when it load it should give employees of the company
        with their total sales
    breakdown: calculate the total sales,
               calculate total sales per each employee
               separates employee according to their store
               then calculate the total sale for that store 
               build a report for that store.
    I think i might need to have to methods to achieve this
    one method is gonna calculate the total sales of the company
    another is for the store to 
    */

    /*
    have all the sales and employees
    add sales into an employee
        how to add sales into employee?
        loop through the sales, check the employee id
        check if the employee does exist in collection 
            if it doesn't exist then 
                add to a collection then add 1
            if it does then add 1
    */
    @Override
    public Map<String, Integer> generateTopSellingEmployee(List<Employee> employees) {
        List<Sale> sales = saleDao.getAllSales();
        
        return topEmp(sales, employees);
    }

    @Override
    public Map<String, Integer> generateTopSellingEmployee(int storeId, List<Employee> employees) {
        List<Sale> sales = saleDao.getSalesbyStoreId(storeId);
  
        return topEmp(sales, employees);
    }

    /*
    Goal: I want to develop a report for top selling employee accross the company
    i have the list of sales and employee
    i have created a DTO called EmployeeMonthSales which will hold the total number of sales and storeId
    so what i want to achieve with this i want to return a treeMap that hold a string name of the employee
    and employeemonthSales which is total sales for that employee and store Id
    the store id will help when filtering in client side
    problem I have is how am I gonna achieve this and how am I gonna fill the employeeMonthSale DTO for each employee
    */
    @Override
    public Map<String, EmployeeMonthSales> generateTopEmployee(List<Employee> employees) {
       List<Sale> sales = saleDao.getAllSales();
       
       Map<String, EmployeeMonthSales> employeeSalesMap = new TreeMap<>();
       for(Sale sale : sales){
           int employee_Id = sale.getEmployee_ID();
           int storeId = sale.getStore_ID();
           
           Employee employee = employees.stream()
                   .filter(emp -> emp.getEmployee_ID() == employee_Id)
                   .findFirst()
                   .orElse(null);
           if(employee == null)
               continue;
           
           String employeeName = employee.getFirstName();
           EmployeeMonthSales employeeMonthSales = employeeSalesMap.get(employeeName);
           if(employeeMonthSales == null){
               employeeMonthSales = new EmployeeMonthSales(storeId, 1);
               employeeSalesMap.put(employeeName, employeeMonthSales);
           }else{
               int total = employeeSalesMap.get(employeeName).getTotalSales();
               employeeMonthSales.setTotalSales(total+1);
               
           }
       }
       return employeeSalesMap;
    }
    
    private Map<String, Integer> topEmp(List<Sale> sales, List<Employee> employees){
        Map<String, Integer> topSellingEmployee = new TreeMap<>();
        Map<Integer, Integer> employeeSales = new HashMap<>();
        for(Sale sale : sales){
           int employee_Id = sale.getEmployee_ID();
           employeeSales.put(employee_Id, employeeSales.getOrDefault(employee_Id, 0)+ 1);
        }
        
        for(Employee employee : employees){
            if(employee.getRole() == Role.Teller){
                int employee_Id = employee.getEmployee_ID();
                String employeeName = employee.getFirstName();
                int totalSales = employeeSales.getOrDefault(employee_Id, 0);
                topSellingEmployee.put(employeeName, totalSales);
            }
        }
        return topSellingEmployee;
    }
    
}
