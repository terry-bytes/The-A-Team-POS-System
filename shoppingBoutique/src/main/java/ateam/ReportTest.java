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
import java.util.TreeMap;
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
        
        List<SalesItem> salesItems = saleItems.getSalesItemsByProductId(1);
        List<Sale> sales = saleService.getAllSales();
        List<Employee> employees = employeeService.getAllEmployees(); // get employee by id
        Map<Integer, Integer> salesQuantity = new HashMap<>();
        
        
        for(SalesItem slItm : salesItems){
//            System.out.println(slItm.getSales_item_ID()+"   "
//                    +slItm.getSales_ID()+" "
//                    +slItm.getQuantity()+"     "+
//                    slItm.getProduct_ID());
            salesQuantity.put(slItm.getSales_ID(), slItm.getQuantity());
        }
        
        
        String name;
        Map<Integer, Integer> employeeSalesByID = new HashMap<>();
        
        for(int saleId:salesQuantity.keySet()){
           Sale sale=sales.stream().filter(s->s.getSales_ID()==saleId).findFirst().get();  // sales by sales id
           
           
           // pick yours
          if(employeeSalesByID.containsKey(sale.getEmployee_ID())){
              employeeSalesByID.put(sale.getEmployee_ID(), employeeSalesByID.get(sale.getEmployee_ID())+salesQuantity.get(saleId) );
          }else{
              employeeSalesByID.put(sale.getEmployee_ID(), salesQuantity.get(saleId));
          }
          employeeSalesByID.compute(sale.getEmployee_ID(), (k,v)->v==null?salesQuantity.get(saleId):v+salesQuantity.get(saleId));
          
        }
        employeeSalesByID.forEach((k,v)->System.out.println(k+" : "+v));
    }
//        for(Sale sale : sales){
//            if(salesQuantity.containsKey(sale.getSales_ID())){
//                employeeSalesByID.put(sale.getEmployee_ID(), salesQuantity.get(sale.getSales_ID()));
//            }
//        }
//        
//        Map<String, Integer> employeeSales = new TreeMap<>();
//        for(Employee employee : employees){
//            int employeeId = employee.getEmployee_ID();
//            if(employeeSalesByID.containsKey(employeeId)){
//                employeeSales.put(employee.getFirstName(), employeeSalesByID.get(employeeId));
//            }
//        }
//        
//        System.out.println("Employee name and quantity");
//        for(Map.Entry<String, Integer> entry : employeeSales.entrySet()){
//            System.out.println(entry.getKey()+"     "+entry.getValue());
//        }
//    }
}
