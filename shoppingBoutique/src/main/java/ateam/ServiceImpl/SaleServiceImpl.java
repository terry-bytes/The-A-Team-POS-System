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
import ateam.DTO.SalesDTO;
import ateam.Models.Store;
import ateam.Service.EmployeeService;
import ateam.Service.SaleService2;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 *
 * @author Admin
 */
public class SaleServiceImpl implements SaleService2{
    private final SaleDAO saleDao;
    private final EmployeeService employeeService;
    
    public SaleServiceImpl(){
        this.saleDao = new SaleDAOIMPL();
        this.employeeService = new EmployeeServiceImpl();
    }
    @Override
    public List<Sale> getAllSales() {
        return saleDao.getAllSales();
    }
    
    public Map<String, Integer> generateStoreMonthReport(int storeId, int month, int year){
        List<Sale> sales = saleDao.getSalesbyStoreId(storeId);
        
        // LocalDate/Time, Duration
        
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
    
    /**
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
    * 
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

    @Override
    public List<Sale> getAllSalesByStoreId(int storeId) {
        return saleDao.getSalesbyStoreId(storeId);
    }

    @Override
    public List<Sale> getDailyStoreByStoreId(int storeId) {
        return saleDao.getDailySalesForStore(storeId);
    }

    @Override
    public List<Sale> getLeastPerformingStore(LocalDate endDate) {
        return saleDao.getLeastPerformingStores(endDate);
    }

    @Override

    public void markVoucherAsUsed(String voucherCode) {
      saleDao.markVoucherAsUsed( voucherCode);   
    }

    @Override
    public BigDecimal validateVoucher(String voucherCode) {
        
     return saleDao.validateVoucher(voucherCode);
    }

    public List<Sale> getSaleForStoreByRange(int storeId, LocalDate startDate, LocalDate endDate) {
        return saleDao.getSalesForStoreByRange(storeId, startDate, endDate);
    }

    @Override
    public List<SalesDTO> getStoreSales(int storeId) {
        List<Sale> sales = saleDao.getSalesbyStoreId(storeId);
        if (sales == null) {
            throw new IllegalStateException("Sales list is null for storeId: " + storeId);
        }

        List<SalesDTO> mySales = new ArrayList<>();
        sales.forEach(sale -> {
            SalesDTO saleDto = new SalesDTO();
            saleDto.setSaleId(sale.getSales_ID());

            Employee employee = employeeService.getEmployeeById(sale.getEmployee_ID());
            if (employee != null) {
                saleDto.setTeller(employee.getFirstName());
            } else if(sale.getEmployee_ID() == 0){
                saleDto.setTeller("Unknown");
            }else {
                saleDto.setTeller(Integer.toString(sale.getEmployee_ID()));
            }

            saleDto.setSalesDate(sale.getSales_date());
            saleDto.setPaymentMethod(sale.getPayment_method());
            saleDto.setTotalAmount(sale.getTotal_amount());
            mySales.add(saleDto);
        });

        return mySales;
    }

    
    
}
