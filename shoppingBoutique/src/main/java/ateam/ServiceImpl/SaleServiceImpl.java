/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.SaleDAO;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.Models.Sale;
import ateam.Models.Store;
import ateam.Service.SaleService2;
import ateam.reports.SaleInfo;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



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
    
    /*
    I want to build a report based on the sales 
    I have List<Store>, List<Sale>.
    List<Store> in Store, I have instance variable called Store_name and other instance variable
    List<Sale> in Sale, i have instance variable store_ID, sales_date and other instance variable
    these data come from my database which i have implemented
    i want to send data into jsp with cleaned data so I can display a bargraph.
    this method should return Map collection 
    could you please help generate the monthSale perstore. 
    so in this method the Integer should be the total number of sales per store which I have to calculate it here
    the String is the name of the store and timestamp is the date which the sale was made on
    */
    public Map<String, Map<Integer, Integer>> generateMonthSaleReport(List<Store> stores){
        List<Sale> sales = saleDao.getAllSales();
        Map<Integer, String> storeIdToName = new HashMap<>();
        for (Store store : stores) {
            storeIdToName.put(store.getStore_ID(), store.getStore_name());
        }

        Map<String, Map<Integer, Integer>> storeSalesInfoMap = new HashMap<>();

        for (Sale sale : sales) {
            String storeName = storeIdToName.get(sale.getStore_ID());
            if (storeName != null) {
                // Extract the month and year from the sales date
                Timestamp salesDate = sale.getSales_date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(salesDate.getTime());
                int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based in Calendar, so add 1
                int year = calendar.get(Calendar.YEAR);

                // Create a key for the month and year (e.g., 202401 for January 2024)
                int monthYear = year * 100 + month;

                // Initialize the map for the store if not already present
                storeSalesInfoMap.putIfAbsent(storeName, new HashMap<>());

                // Get the map for the store
                Map<Integer, Integer> monthSalesMap = storeSalesInfoMap.get(storeName);

                // Update the sales count for the monthYear
                monthSalesMap.put(monthYear, monthSalesMap.getOrDefault(monthYear, 0) + 1);
            }
        }

        return storeSalesInfoMap;
    }
    
    public Map<String, Integer> generateStoreMonthReport(int storeId, int month, int year){
        List<Sale> sales = saleDao.getSalesbyStoreId(storeId);
        
        
        Map<String, Integer> salesReport = new HashMap<>();
        
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
    
}
