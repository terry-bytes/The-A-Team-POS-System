/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.ReportDaoImpl;
import ateam.DTO.InventoryData;
import ateam.DTO.ProductSalesData;
import ateam.DTO.SalesData;
import ateam.Models.Employee;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.EmployeeService;
import ateam.Service.SaleItemsService;
import ateam.Service.SaleService2;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.SaleItemServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class ReportTest {
    String url = "jdbc:mysql://localhost:3306/carolsboutique?useSSL=false";
        String user = "root";
        String password = "@Mysql23";
        
        
        public static void generateReport(Connection conn) throws SQLException {
        List<InventoryData> inventoryList = getTotalInventory(conn);
        List<SalesData> salesList = getTotalSold(conn);

        // Combine inventory and sales data using Streams
        Map<Integer, ReportData> reportDataMap = new HashMap<>();

        inventoryList.forEach(inventory -> {
            reportDataMap
                .computeIfAbsent(inventory.getStoreId(), ReportData::new)
                .addTotalInventory(inventory.getProductId(), inventory.getTotalInventory());
        });

        salesList.forEach(sales -> {
            reportDataMap
                .computeIfAbsent(sales.getStoreId(), ReportData::new)
                .addTotalSold(sales.getProductId(), sales.getTotalSold());
        });

        // Print or process report data
        reportDataMap.values().forEach(reportData -> {
            System.out.println("Store ID: " + reportData.getStoreId());
            System.out.println("Total Inventory: " + reportData.getTotalInventory());
            System.out.println("Total Sold: " + reportData.getTotalSold());
            System.out.println("Percentage Sold: " + reportData.getPercentageSold() + "%");
            System.out.println(); // Optional: add separator between stores
        });
    }

//        public Map<Integer, List<ProductSalesData>> getTopSellingProductsByStore() {
//        List<ProductSalesData> salesDataList = new ReportDaoImpl().getTopSellingProduct();
//
//        // Grouping by productId and then by storeId
//        Map<Integer, List<ProductSalesData>> topSellingProductsByStore = salesDataList.stream()
//                .collect(Collectors.groupingBy(ProductSalesData::getProductId, 
//                        Collectors.toList()));
//
//        // Sorting each group by totalQuantitySold to find the store that sold the most of each product
//        topSellingProductsByStore.values().forEach(list -> 
//                list.sort((a, b) -> Integer.compare(b.getTotalQuantitySold(), a.getTotalQuantitySold())));
//
//        return topSellingProductsByStore;
//    }
        
        public Map<Integer, ProductSalesData> getTopSellingProductsByStore() {
            List<ProductSalesData> salesDataList = new ReportDaoImpl().getTopSellingProduct();

            // Grouping by productId and then finding the store with the highest totalQuantitySold
            return salesDataList.stream()
                    .collect(Collectors.groupingBy(ProductSalesData::getProductId, 
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparingInt(ProductSalesData::getTotalQuantitySold)),
                                    Optional::get
                            )
                    ));
        }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        public static List<InventoryData> getTotalInventory(Connection conn) throws SQLException {
        String sql = "SELECT store_ID, product_ID, SUM(previous_quantity + inventory_quantity) AS total_inventory "
                   + "FROM inventory GROUP BY store_ID, product_ID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<InventoryData> inventoryList = new ArrayList<>();
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalInventory = rs.getInt("total_inventory");
                inventoryList.add(new InventoryData(storeId, productId, totalInventory));
            }
            return inventoryList;
        }
    }

    public static List<SalesData> getTotalSold(Connection conn) throws SQLException {
        String sql = "SELECT s.store_ID, si.product_ID, SUM(si.quantity) AS total_sold "
                   + "FROM sales s JOIN sales_items si ON s.sales_ID = si.sales_ID "
                   + "GROUP BY s.store_ID, si.product_ID";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<SalesData> salesList = new ArrayList<>();
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalSold = rs.getInt("total_sold");
                salesList.add(new SalesData(storeId, productId, totalSold));
            }
            return salesList;
        }
    }

    public static void generateReports(Connection conn) throws SQLException {
        List<InventoryData> inventoryList = getTotalInventory(conn);
        List<SalesData> salesList = getTotalSold(conn);

        // Combine inventory and sales data using Java Streams
        Map<Integer, Map<Integer, InventoryData>> reportData = inventoryList.stream()
            .collect(Collectors.groupingBy(
                InventoryData::getStoreId,
                Collectors.toMap(
                    InventoryData::getProductId,
            Function.identity()
                )
            ));

        salesList.forEach(sales -> reportData
            .getOrDefault(sales.getStoreId(), new HashMap<>())
            .computeIfPresent(sales.getProductId(), (k, v) -> {
                v.setTotalSold(v.getTotalSold() + sales.getTotalSold());
                return v;
            })
        );

        reportData.forEach((storeId, products) -> {
            int totalInventory = products.values().stream().mapToInt(InventoryData::getTotalInventory).sum();
            int totalSold = products.values().stream().mapToInt(InventoryData::getTotalSold).sum();

            double percentageSold = totalInventory > 0 ? ((double) totalSold / totalInventory) * 100 : 0;

            System.out.println("Store ID: " + storeId);
            System.out.println("Total Inventory: " + totalInventory);
            System.out.println("Total Sold: " + totalSold);
            System.out.println("Percentage Sold: " + percentageSold + "%");
            System.out.println("-----------------------");
        });
    }

    public static void main(String[] args) {
       // Get top selling products by store
        Map<Integer, ProductSalesData> topSellingProductsByStore = new ReportTest().getTopSellingProductsByStore();
        System.out.println("Top Selling Products By Store:");
        topSellingProductsByStore.forEach((productId, salesList) -> {
            System.out.println("Product ID: " + productId);
            
                System.out.println("  Store ID: " + salesList.getStoreId() + 
                                   ", Total Quantity Sold: " + salesList.getTotalQuantitySold());
            
        });
    }
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
//     public static Map<Integer, BigDecimal> getTotalStockValueByStore() {
//        Map<Integer, BigDecimal> storeStockValues = new HashMap<>();
//        
//
//        String query = "SELECT "
//                + "inventory.store_ID, "
//                + "SUM(inventory.inventory_quantity * p.product_price) AS total_stock_value "
//                + "FROM "
//                + "(SELECT "
//                + "i.store_ID, "
//                + "i.product_ID, "
//                + "SUM(i.inventory_quantity) AS total_inventory_added "
//                + "FROM inventory i "
//                + "GROUP BY i.store_ID, i.product_ID) AS inventory "
//                + "LEFT JOIN "
//                + "(SELECT "
//                + "s.store_ID, "
//                + "si.product_ID, "
//                + "SUM(si.quantity) AS total_quantity_sold "
//                + "FROM sales_items si "
//                + "JOIN sales s ON si.sales_ID = s.sales_ID "
//                + "GROUP BY s.store_ID, si.product_ID) AS sales "
//                + "ON inventory.store_ID = sales.store_ID AND inventory.product_ID = sales.product_ID "
//                + "JOIN products p ON inventory.product_ID = p.product_ID "
//                + "GROUP BY inventory.store_ID";
//
//        try (Connection conn = DriverManager.getConnection(url, user, password);
//             PreparedStatement ps = conn.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                int storeId = rs.getInt("store_ID");
//                BigDecimal totalStockValue = rs.getBigDecimal("total_stock_value");
//                storeStockValues.put(storeId, totalStockValue);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return storeStockValues;
//    }

    public static void mainr(String[] args) {
    ReportTest storeStockValue = new ReportTest();
    Map<Integer, Map<Integer, Integer>> inventoryData = storeStockValue.getTotalInventoryAdded();
    Map<Integer, Map<Integer, Integer>> salesData = storeStockValue.getTotalQuantitySold();
    Map<Integer, Map<Integer, Integer>> stockLevels = storeStockValue.calculateCurrentStockLevels(inventoryData, salesData);
    Map<Integer, BigDecimal> totalStockValues = storeStockValue.calculateTotalStockValue(stockLevels);
    
    totalStockValues.forEach((storeId, totalStockValue) -> 
        System.out.println("Store ID: " + storeId + ", Total Stock Value: " + totalStockValue));
}

    private void chosenProduct(){
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
    
    public Map<Integer, Map<Integer, Integer>> getTotalInventoryAdded() {
        Map<Integer, Map<Integer, Integer>> inventoryData = new HashMap<>();
        String query = "SELECT store_ID, product_ID, SUM(inventory_quantity) AS total_inventory_added " +
                       "FROM inventory " +
                       "GROUP BY store_ID, product_ID";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalInventoryAdded = rs.getInt("total_inventory_added");

                inventoryData
                    .computeIfAbsent(storeId, k -> new HashMap<>())
                    .put(productId, totalInventoryAdded);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return inventoryData;
    }
    
    public Map<Integer, Map<Integer, Integer>> getTotalQuantitySold() {
    Map<Integer, Map<Integer, Integer>> salesData = new HashMap<>();
    String query = "SELECT s.store_ID, si.product_ID, SUM(si.quantity) AS total_quantity_sold " +
                   "FROM sales_items si " +
                   "JOIN sales s ON si.sales_ID = s.sales_ID " +
                   "GROUP BY s.store_ID, si.product_ID";
    
    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            int storeId = rs.getInt("store_ID");
            int productId = rs.getInt("product_ID");
            int totalQuantitySold = rs.getInt("total_quantity_sold");
            
            salesData
                .computeIfAbsent(storeId, k -> new HashMap<>())
                .put(productId, totalQuantitySold);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return salesData;
}

public Map<Integer, Map<Integer, Integer>> calculateCurrentStockLevels(
        Map<Integer, Map<Integer, Integer>> inventoryData,
        Map<Integer, Map<Integer, Integer>> salesData) {
    
    Map<Integer, Map<Integer, Integer>> stockLevels = new HashMap<>();
    
    for (Map.Entry<Integer, Map<Integer, Integer>> storeEntry : inventoryData.entrySet()) {
        int storeId = storeEntry.getKey();
        Map<Integer, Integer> productInventory = storeEntry.getValue();
        
        for (Map.Entry<Integer, Integer> productEntry : productInventory.entrySet()) {
            int productId = productEntry.getKey();
            int totalInventoryAdded = productEntry.getValue();
            int totalQuantitySold = salesData.getOrDefault(storeId, new HashMap<>()).getOrDefault(productId, 0);
            int currentStock = totalInventoryAdded - totalQuantitySold;
            
            stockLevels
                .computeIfAbsent(storeId, k -> new HashMap<>())
                .put(productId, currentStock);
        }
    }
    
    return stockLevels;
}

public Map<Integer, BigDecimal> calculateTotalStockValue(
        Map<Integer, Map<Integer, Integer>> stockLevels) {
    
    Map<Integer, BigDecimal> totalStockValues = new HashMap<>();
    String query = "SELECT product_ID, product_price FROM products";
    Map<Integer, BigDecimal> productPrices = new HashMap<>();
    
    try (Connection conn = DriverManager.getConnection(url, user, password);
         PreparedStatement ps = conn.prepareStatement(query);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            int productId = rs.getInt("product_ID");
            BigDecimal productPrice = rs.getBigDecimal("product_price");
            productPrices.put(productId, productPrice);
        }
        
        for (Map.Entry<Integer, Map<Integer, Integer>> storeEntry : stockLevels.entrySet()) {
            int storeId = storeEntry.getKey();
            Map<Integer, Integer> productStocks = storeEntry.getValue();
            BigDecimal totalStockValue = BigDecimal.ZERO;
            
            for (Map.Entry<Integer, Integer> productEntry : productStocks.entrySet()) {
                int productId = productEntry.getKey();
                int currentStock = productEntry.getValue();
                BigDecimal productPrice = productPrices.get(productId);
                BigDecimal stockValue = productPrice.multiply(BigDecimal.valueOf(currentStock));
                totalStockValue = totalStockValue.add(stockValue);
            }
            
            totalStockValues.put(storeId, totalStockValue);
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    
    return totalStockValues;
}

}

class ReportData {
    private int storeId;
    private Map<Integer, Integer> totalInventoryMap;
    private Map<Integer, Integer> totalSoldMap;

    public ReportData(int storeId) {
        this.storeId = storeId;
        this.totalInventoryMap = new HashMap<>();
        this.totalSoldMap = new HashMap<>();
    }

    public int getStoreId() {
        return storeId;
    }

    public void addTotalInventory(int productId, int totalInventory) {
        totalInventoryMap.put(productId, totalInventory);
    }

    public void addTotalSold(int productId, int totalSold) {
        totalSoldMap.put(productId, totalSold);
    }

    public int getTotalInventory() {
        return totalInventoryMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public int getTotalSold() {
        return totalSoldMap.values().stream().mapToInt(Integer::intValue).sum();
    }

    public double getPercentageSold() {
        int totalInventory = getTotalInventory();
        if (totalInventory == 0) {
            return 0.0; // Handle division by zero gracefully
        }
        int totalSold = getTotalSold();
        return (double) totalSold / totalInventory * 100.0;
    }
}
