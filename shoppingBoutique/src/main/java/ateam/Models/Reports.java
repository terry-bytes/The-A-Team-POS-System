/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.Models;

import ateam.DAO.ReportDAO;
import ateam.DAOIMPL.ReportDaoImpl;
import ateam.DTO.InventoryData;
import ateam.DTO.ProductSalesData;
import ateam.DTO.SalesData;
import ateam.DTO.StorePerfomanceInSales;
import ateam.DTO.StorePerformance;
import ateam.DTO.TopProductDTO;
import ateam.DTO.TopProductSellEmployee;
import ateam.DTO.TopSellingEmployee;
import ateam.Service.EmployeeService;
import ateam.Service.ProductService;
import ateam.Service.SaleItemsService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.SaleItemServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class Reports {
    private final EmployeeService employeeService;
    private final SaleService2 saleService;
    private final int targetSalesPerEmployee;
    private final StoreService storeService;
    private final ProductService productService;
    private final ReportDAO reportDao;
    

    public Reports(EmployeeService employeeService, SaleService2 saleService, int target, StoreService storeService, ProductService productService, ReportDAO reportDao) {
        this.employeeService = employeeService;
        this.saleService = saleService;
        this.targetSalesPerEmployee = target;
        this.storeService = storeService;
        this.productService = productService;
        this.reportDao = reportDao;
    }

    public Reports() {
        targetSalesPerEmployee = 10;
        this.employeeService = new EmployeeServiceImpl();
        this.saleService = new SaleServiceImpl();
        this.storeService = new StoreServiceImpl();
        this.productService = new ProductServiceImpl();
        this.reportDao = new ReportDaoImpl();
    }
    
    public Map<String, Integer> generateTopSellingEmployees(){
       return topSellingEmployees(saleService.getAllSales());
    }
    
    public Map<String, Integer> generateTopSellingEmployees(int storeId){
        return topSellingEmployees(saleService.getAllSalesByStoreId(storeId));
    }
    
    private Map<String, Integer> generateMonthReportForStore(int storeId, LocalDate date){
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
    
    public Map<String, BigDecimal> getMonthSalesReport(int storeId, LocalDate date){
        LocalDate startDate = date.withDayOfMonth(1);
        LocalDate endOfMonth = date.withDayOfMonth(date.lengthOfMonth());
        
        Map<LocalDate, BigDecimal> sales = reportDao.getSalesByStoreAndDateRange(storeId, startDate, endOfMonth);
        
        return sales.entrySet().stream()
                .collect(Collectors.toMap(
                    entry -> entry.getKey().format(DateTimeFormatter.ISO_LOCAL_DATE),
                    Map.Entry::getValue,
                    (e1, e2) -> e1, 
                    TreeMap::new    
                ));
    }
    
    /**
     * A report on top Selling Employees
     * This report get a top selling employees in by first i dentifying the sales for that company 
     * 
     */
    
   private Map<String, Integer> topSellingEmployees(List<Sale> sales){
        Map<String, Integer> topSellingEmployees = new TreeMap<>();
        Map<Integer, Integer> employeeSales = new HashMap<>();
        
        sales.stream()
                .map(sale -> sale.getEmployee_ID())
                .forEachOrdered(employee_Id -> {
            employeeSales.put(employee_Id, employeeSales.getOrDefault(employee_Id, 0)+ 1);
        });
        System.out.println(employeeSales.size());
        
        employeeService.getAllEmployees().stream()
                .filter(employee -> (employee.getRole() == Role.Teller))
                .forEachOrdered(employee -> {
                    int employee_Id = employee.getEmployee_ID();
                    String employeeName = employee.getFirstName();
                    int totalSales = employeeSales.getOrDefault(employee_Id, 0);
                    if (totalSales >= targetSalesPerEmployee) {
                        topSellingEmployees.put(employeeName, totalSales);
                }
        });
        System.out.println("Top selling employees: "+ topSellingEmployees.size());
       return topSellingEmployees;
   }
    
    /**
     * Report of all the stores that have achieved the target for a particular month
     * what is the target of the month?
     * Target is set to be 80% of stock they received in that particular month
     * I need to calculate the total sales for each store for the selected month
     * 
     * @params sales
     * Group store by name @param store id and store name
     * 
     * Build a report for 
     * @param month
     * 
     * @return Map<String, Integer>
     * String Map represent the name of the store Integer represent the total number of sales
    */
    
    public Map<String, StorePerfomanceInSales> StoreAchievedTarget(LocalDate startDate, LocalDate endDate){
        return storesPerformance(startDate, endDate).values().stream()
                .filter(s -> s.getPercentageSold() < 80.0)
                .collect(Collectors.toMap(
                        s -> storeService.getStoreById(s.getStoreId()).getStore_name(),
                        s -> s
                ));
        
        
    }
    
    /**
     * Generate the on the top 40 highest selling products and which store sold the most
     * first, calculate the total sales per
     *
     */
    
    
    /**
     * Generate a report for chosen product, how much was sold and the top sells person for that product across the stores nationwide
     * from these list get List<Sale> and List<SalesItem> 
     * calculate the total sales for that product interms of items e.g total is 210; 
     * 
     * 
     * @param productId
     * @return TopEmployeeforProduct
     */
    
    public TopSellingEmployee topSellingEmployeeForProduct(int productId){
        SaleItemsService saleItems = new SaleItemServiceImpl();
        TopSellingEmployee topSellingEmployee = new TopSellingEmployee();
        List<SalesItem> salesItems = saleItems.getSalesItemsByProductId(productId);
        List<Sale> sales = saleService.getAllSales();
        List<TopProductSellEmployee> topProductSellingemployees = saleItems.getTotalSalesPerProduct().stream()
                .filter(s -> s.getProductId() == productId)
                .collect(Collectors.toList());
        
        
        for(TopProductSellEmployee tp : topProductSellingemployees){
            int tpSales = tp.getTotalSales();
            if(tpSales > topSellingEmployee.getTotalSales()){
                topSellingEmployee.setTotalSales(tpSales);
                topSellingEmployee.setEmployeeName(tp.getEmployeeName());
            }
        }
        
//        List<Sale> salesWithProductId = sales.stream()
//                .filter(sale -> salesItems.stream()
//                        .anyMatch(item -> item.getSales_ID() == sale.getSales_ID()))
//                .collect(Collectors.toList());
//        
//        //grouping by sales id e.g key=2, value will be the list of items sold on that sales
//        Map<Integer, List<SalesItem>> salesItemsBySalesId = salesItems.stream()
//                .collect(Collectors.groupingBy(SalesItem::getSales_ID));
//        
        BigDecimal totalQuantitySold = salesItems.stream()
                .map(item -> BigDecimal.valueOf(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        topSellingEmployee.setTotalSalesForProduct(totalQuantitySold);
    return topSellingEmployee;
    }
    
    public String generateDailySaleReport(int storeId) {
        // Assuming saleService is injected or instantiated elsewhere
        List<Sale> currentSales = saleService.getDailyStoreByStoreId(storeId);
        System.out.println(currentSales.size());
        int target =10000;
        // Calculate total sales amount
        BigDecimal totalSalesAmount = calculateTotalSalesAmount(currentSales);
        
        // Calculate percentage of daily sales achieved
        BigDecimal percentageAchieved = calculatePercentage(totalSalesAmount, target);
        
        // Determine how many percentages away from reaching target
        BigDecimal percentageToTarget = BigDecimal.valueOf(100).subtract(percentageAchieved);
        
        // Format and return the report
        String report = String.format("Daily Sales Report for Store %d:\n", storeId);
        report += String.format("Total Sales Amount: R%.2f\n", totalSalesAmount);
        report += String.format("Percentage of Daily Target Achieved: %.2f%%\n", percentageAchieved);
        report += String.format("Percentage Away from Daily Target: %.2f%%\n", percentageToTarget);
        
        return report;
    }
    
    private BigDecimal calculatePercentage(BigDecimal totalSales, int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("Target must be greater than zero");
        }
        BigDecimal percentage = totalSales.divide(BigDecimal.valueOf(target), 4, BigDecimal.ROUND_HALF_UP)
                                         .multiply(BigDecimal.valueOf(100));
        return percentage;
    }
    
    private BigDecimal calculateTotalSalesAmount(List<Sale> sales) {
        BigDecimal total = BigDecimal.ZERO;
        for (Sale sale : sales) {
            total = total.add(sale.getTotal_amount());
        }
        return total;
    }
    
    public Map<String, BigDecimal> getLeastsPerformingStores(LocalDate endDate){
        List<Sale> sales = saleService.getLeastPerformingStore(endDate);
        BigDecimal avarageTotal = new BigDecimal("1000");
        Map<Integer, BigDecimal> totalSalesPerStore = new HashMap<>(); // I want to hold the storeid and the total sales in price in that store
        
        
        for(Sale sale : sales){
           //totalSalesPerStore.compute(sale.getStore_ID(), (k,v) -> v == null ? sale.getTotal_amount() : v.add(sale.getTotal_amount()));
           if(totalSalesPerStore.containsKey(sale.getStore_ID())){
               totalSalesPerStore.put(sale.getStore_ID(), totalSalesPerStore.get(sale.getStore_ID()).add(sale.getTotal_amount()));
           }else{
               totalSalesPerStore.put(sale.getStore_ID(), sale.getTotal_amount());
           }
        }
        totalSalesPerStore.forEach((k, v) -> System.out.println(k +"  :  "+v));
        
        
        Map<String, BigDecimal> totalSales = new HashMap<>(); //I want to convert storeid into store name
        for(int storeId : totalSalesPerStore.keySet()){
           if(totalSalesPerStore.get(storeId).compareTo(avarageTotal) == -1){
               totalSales.put(storeService.getStoreById(storeId).getStore_name(), totalSalesPerStore.get(storeId));
           } 
        }
        
        System.out.println("Filtered sales");
        totalSales.forEach((k, v) -> System.out.println(k +"  :  "+v));
        return totalSales;
    }
    
    public List<StorePerformance> evaluateTopAchievers(LocalDate startDate, LocalDate endDate, LocalDate prevStartDate, LocalDate prevEndDate) {
        // Step 1: Fetch Data
        List<StorePerformance> performanceList = reportDao.fetchStorePerformanceData(startDate, endDate);

        // Step 2: Calculate Additional Metrics
        performanceList = calculateAdditionalMetrics(performanceList);

        // Step 3: Normalize Metrics
        performanceList = normalizeMetrics(performanceList);

        // Step 4: Calculate Composite Scores
        performanceList = calculateCompositeScores(performanceList);

        // Step 5: Rank Stores
        return rankStores(performanceList);
    }

    public List<StorePerformance> rankStores(List<StorePerformance> performanceList) {
        return performanceList.stream()
            .sorted(Comparator.comparing(StorePerformance::getCompositeScore).reversed())
            .collect(Collectors.toList());
    }

    public List<StorePerformance> calculateCompositeScores(List<StorePerformance> performanceList) {
        BigDecimal weightTotalSales = new BigDecimal("0.40");
        BigDecimal weightSalesEfficiency = new BigDecimal("0.20");
        BigDecimal weightStockTurnover = new BigDecimal("0.20");
        BigDecimal weightSalesGrowth = new BigDecimal("0.20");

        return performanceList.stream().map(sp -> {
            BigDecimal compositeScore = sp.getNormalizedTotalSales().multiply(weightTotalSales)
                .add(sp.getNormalizedSalesEfficiency().multiply(weightSalesEfficiency))
                .add(sp.getNormalizedStockTurnover().multiply(weightStockTurnover))
                .add(sp.getNormalizedSalesGrowth().multiply(weightSalesGrowth));

            sp.setCompositeScore(compositeScore);
            return sp;
        }).collect(Collectors.toList());
    }

    public List<StorePerformance> normalizeMetrics(List<StorePerformance> performanceList) {
        BigDecimal maxTotalSales = performanceList.stream()
            .map(StorePerformance::getTotalSales)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ONE);

        BigDecimal maxSalesEfficiency = performanceList.stream()
            .map(StorePerformance::getSalesEfficiency)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ONE);

        BigDecimal maxStockTurnover = performanceList.stream()
            .map(StorePerformance::getStockTurnover)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ONE);

        BigDecimal maxSalesGrowth = performanceList.stream()
            .map(StorePerformance::getSalesGrowth)
            .max(BigDecimal::compareTo)
            .orElse(BigDecimal.ONE);

        return performanceList.stream().map(sp -> {
            sp.setNormalizedTotalSales(sp.getTotalSales().divide(maxTotalSales, RoundingMode.HALF_UP));
            sp.setNormalizedSalesEfficiency(sp.getSalesEfficiency().divide(maxSalesEfficiency, RoundingMode.HALF_UP));
            sp.setNormalizedStockTurnover(sp.getStockTurnover().divide(maxStockTurnover, RoundingMode.HALF_UP));
            sp.setNormalizedSalesGrowth(sp.getSalesGrowth().divide(maxSalesGrowth, RoundingMode.HALF_UP));
            return sp;
        }).collect(Collectors.toList());
    }

    public List<StorePerformance> calculateAdditionalMetrics(List<StorePerformance> performanceList) {
        return performanceList.stream().map(sp -> {
            // Calculate sales efficiency
            BigDecimal salesEfficiency = sp.getTotalSales().divide(sp.getTotalStockValue(), RoundingMode.HALF_UP);

            // Calculate stock turnover
            BigDecimal stockTurnover = new BigDecimal(sp.getTotalQuantitySold()).divide(sp.getTotalStockValue(), RoundingMode.HALF_UP);

            // Calculate sales growth
            BigDecimal salesGrowth = sp.getCurrentPeriodSales().subtract(sp.getPreviousPeriodSales())
                                        .divide(sp.getPreviousPeriodSales(), RoundingMode.HALF_UP)
                                        .multiply(new BigDecimal(100));

            sp.setSalesEfficiency(salesEfficiency);
            sp.setStockTurnover(stockTurnover);
            sp.setSalesGrowth(salesGrowth);

            return sp;
        }).collect(Collectors.toList());
    }

    
    
    
    public Map<String, StorePerfomanceInSales> getTopAchievingStores(){
        
    List<InventoryData> inventoryList = reportDao.getTotalInventory();
    List<SalesData> salesList = reportDao.getTotalSoldItems();

    Map<Integer, StorePerfomanceInSales> reportDataMap = new HashMap<>();

    inventoryList.forEach(inventory -> {
        reportDataMap
            .computeIfAbsent(inventory.getStoreId(), StorePerfomanceInSales::new)
            .addTotalInventory(inventory.getProductId(), inventory.getTotalInventory());
    });

    salesList.forEach(sales -> {
        reportDataMap
            .computeIfAbsent(sales.getStoreId(), StorePerfomanceInSales::new)
            .addTotalSold(sales.getProductId(), sales.getTotalSold());
    });

     return reportDataMap.values().stream()
        .filter(reportData -> reportData.getPercentageSold() < 50.0)
        .collect(Collectors.toMap(
            reportData -> storeService.getStoreById(reportData.getStoreId()).getStore_name(),
            reportData -> reportData
        ));
    }
    
    public Map<String, StorePerfomanceInSales> getLeastPerformingStoree(LocalDate today, LocalDate endDate, double target){

        return storesPerformance(today, endDate).values().stream()
                .filter(s -> s.getPercentageSold() < target)
                .collect(Collectors.toMap(
                        s -> storeService.getStoreById(s.getStoreId()).getStore_name(),
                        storeSalesPercentage -> storeSalesPercentage
                ));
    }
    
    private Map<Integer, StorePerfomanceInSales> storesPerformance(LocalDate startDate, LocalDate endDate){
        List<InventoryData> inventoryList = reportDao.getTotalInventory(startDate, endDate);
        List<SalesData> salesList = reportDao.getTotalSoldItems(startDate, endDate);
        
        Map<Integer, StorePerfomanceInSales> totalSalesPerStore = new HashMap<>();
        
        inventoryList.forEach(inventory ->{
            totalSalesPerStore.computeIfAbsent(inventory.getStoreId(), StorePerfomanceInSales::new)
                    .addTotalInventory(inventory.getProductId(), inventory.getTotalInventory());
        });
        
        salesList.forEach(sale -> {
            totalSalesPerStore.computeIfAbsent(sale.getStoreId(), StorePerfomanceInSales::new)
                    .addTotalSold(sale.getProductId(), sale.getTotalSold());
        });
         return totalSalesPerStore;
    }
    
    public List<TopProductDTO> top40SellingProducts() {
        List<ProductSalesData> salesDataList = reportDao.getTopSellingProduct();


            // Grouping by productId and then finding the store with the highest totalQuantitySold
            Map<Integer, ProductSalesData> topSellingProductsByStore = salesDataList.stream()
                    .collect(Collectors.groupingBy(ProductSalesData::getProductId, 
                            Collectors.collectingAndThen(
                                    Collectors.maxBy(Comparator.comparingInt(ProductSalesData::getTotalQuantitySold)),
                                    Optional::get
                            )
                    ));
        List<TopProductDTO> topProducts = topSellingProductsByStore.entrySet().stream()
                .map(entry -> {
                    ProductSalesData salesData = entry.getValue();
                    String productName = productService.getProductById(salesData.getProductId()).getProduct_name();
                    String storeName = storeService.getStoreById(salesData.getStoreId()).getStore_name();
                    return new TopProductDTO(productName, storeName, salesData.getTotalQuantitySold());
                })
                .sorted(Comparator.comparingInt(TopProductDTO::getTotalQuantitySold).reversed())
                .limit(40) // Limit to top 40 products
                .collect(Collectors.toList());

        return topProducts;

        
    }

    public Map<String, BigDecimal> leastPerformingStores(int months, double target){
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(months);
        System.out.println("Start date for least performing store"+startDate);
        System.out.println(endDate);
        
        Map<Integer, BigDecimal> totalInventoryMap = reportDao.getTotalInventoryValuePerStore(startDate, endDate);
        Map<Integer, BigDecimal> totalAmountSalesMap = reportDao.getTotalSalesAmountPerStore(startDate, endDate);
        System.out.println("totalInve: "+ totalInventoryMap.size());
        System.out.println("totalSale: "+ totalAmountSalesMap.size());
        return totalAmountSalesMap.entrySet().stream()
            .filter(entry -> totalInventoryMap.containsKey(entry.getKey()) && totalInventoryMap.get(entry.getKey()).compareTo(BigDecimal.ZERO) > 0)
            .filter(entry -> {
                BigDecimal totalSales = entry.getValue();
                BigDecimal totalInventory = totalInventoryMap.get(entry.getKey());
                double salesPercentage = totalSales.divide(totalInventory, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).doubleValue();
                return salesPercentage < target;
            })
            .collect(Collectors.toMap(
                    entry -> storeService.getStoreById(entry.getKey()).getStore_name(),
                    Map.Entry::getValue
            ));
    }
    
    public Map<String, BigDecimal> getTodaysReportForAllStores(){
        List<Sale> sales = reportDao.getTodaysSales();
        
        if(sales == null && sales.isEmpty()) return null;
        
        return reportDao.getTodaysSales().stream()
                .collect(Collectors.groupingBy(
                    sale -> storeService.getStoreById(sale.getStore_ID()).getStore_name(),
                        Collectors.mapping(
                                Sale::getTotal_amount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                ));
    }
}
