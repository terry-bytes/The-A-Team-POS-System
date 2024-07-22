package ateam.Service;


import ateam.Models.Employee;
import ateam.Models.Sale;
import java.util.List;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */

/**
 *
 * @author Admin
 */
public interface SaleService2 {
    List<Sale> getAllSales();
    Map<String, Integer> generateStoreMonthReport(int storeId, int month, int year);
    Map<String, Integer> generateTopSellingEmployee(List<Employee> employees);
    Map<String, Integer> generateTopSellingEmployee(int storeId, List<Employee> employee);
}
