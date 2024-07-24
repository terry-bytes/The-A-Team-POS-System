/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam;

import ateam.Models.Employee;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.EmployeeService;
import ateam.Service.SaleItemsService;
import ateam.Service.SaleService2;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.SaleItemServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class ReportTest {
    public static void main(String[] args) {
        SaleItemsService saleItems = new SaleItemServiceImpl();
        SaleService2 saleService = new SaleServiceImpl();
        EmployeeService employeeService = new EmployeeServiceImpl();
        
        List<SalesItem> salesItems = saleItems.getSalesItemsByProductId(2);
        List<Sale> sales = saleService.getAllSales();
        List<Employee> employees = employeeService.getAllEmployees();
        Map<Integer, Integer> salesQuantity = new HashMap<>();
        
        
        for(SalesItem slItm : salesItems){
            System.out.println(slItm.getSales_item_ID()+"   "
                    +slItm.getSales_ID()+" "
                    +slItm.getQuantity()+"     "+
                    slItm.getProduct_ID());
            salesQuantity.put(slItm.getSales_ID(), slItm.getQuantity());
        }
        
        String name;
        Map<String, Integer> employeeSales = new HashMap<>();
        for(Sale sale : sales){
            if(salesQuantity.containsKey(sale.getSales_ID())){
                System.out.println("The name of the person who made this sale is "
                        +sale.getEmployee_ID()+" in sale: "+sale.getSales_ID());
            }
        }
    }
}
