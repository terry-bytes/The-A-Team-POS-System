/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ReportDAO;
import ateam.DTO.InventoryData;
import ateam.DTO.ProductSalesData;
import ateam.DTO.SalesData;
import ateam.DTO.StorePerformance;
import ateam.DTO.TopSellingEmployeeDTO;
import ateam.Models.Sale;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author T440
 */
public class ReportDaoImpl implements ReportDAO{
    private final Connection connection;
    
    public ReportDaoImpl(){
        this(new Connect().connectToDB());
    }
    
    public ReportDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<StorePerformance> fetchStorePerformanceData(LocalDate startDate, LocalDate endDate) {
    List<StorePerformance> performanceList = new ArrayList<>();

    // Fetch total sales and quantities sold
    String salesQuery = "SELECT s.store_ID, SUM(si.quantity * si.unit_price) AS total_sales, SUM(si.quantity) AS total_quantity_sold " +
                        "FROM sales s JOIN sales_items si ON s.sales_ID = si.sales_ID " +
                        "WHERE s.sales_date BETWEEN ? AND ? GROUP BY s.store_ID";
    
    try (PreparedStatement pstmt = connection.prepareStatement(salesQuery)) {
        pstmt.setDate(1, java.sql.Date.valueOf(startDate));
        pstmt.setDate(2, java.sql.Date.valueOf(endDate));
        try(ResultSet rs = pstmt.executeQuery()){

            while (rs.next()) {
                StorePerformance storePerformance = new StorePerformance();
                int storeID = rs.getInt("store_ID");
                BigDecimal totalSales = rs.getBigDecimal("total_sales");
                int totalQuantitySold = rs.getInt("total_quantity_sold");
                performanceList.add(storePerformance);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Fetch total stock values
    String stockQuery = "SELECT store_ID, SUM(product_price * quantity_in_stock) AS total_stock_value FROM products GROUP BY store_ID";
    
    try (PreparedStatement pstmt = connection.prepareStatement(stockQuery)) {
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int storeID = rs.getInt("store_ID");
            BigDecimal totalStockValue = rs.getBigDecimal("total_stock_value");
            for (StorePerformance sp : performanceList) {
                if (sp.getStoreID() == storeID) {
                    sp.setTotalStockValue(totalStockValue);
                    break;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Fetch sales for growth calculation
    String growthQuery = "SELECT s.store_ID, " +
                         "SUM(CASE WHEN s.sales_date BETWEEN ? AND ? THEN si.quantity * si.unit_price ELSE 0 END) AS current_period_sales, " +
                         "SUM(CASE WHEN s.sales_date BETWEEN ? AND ? THEN si.quantity * si.unit_price ELSE 0 END) AS previous_period_sales " +
                         "FROM sales s JOIN sales_items si ON s.sales_ID = si.sales_ID GROUP BY s.store_ID";
    
    try (PreparedStatement pstmt = connection.prepareStatement(growthQuery)) {
        pstmt.setDate(1, java.sql.Date.valueOf(startDate));
        pstmt.setDate(2, java.sql.Date.valueOf(endDate));
        pstmt.setDate(3, java.sql.Date.valueOf(startDate));
        pstmt.setDate(4, java.sql.Date.valueOf(endDate));
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            int storeID = rs.getInt("store_ID");
            BigDecimal currentPeriodSales = rs.getBigDecimal("current_period_sales");
            BigDecimal previousPeriodSales = rs.getBigDecimal("previous_period_sales");
            for (StorePerformance sp : performanceList) {
                if (sp.getStoreID() == storeID) {
                    sp.setCurrentPeriodSales(currentPeriodSales);
                    sp.setPreviousPeriodSales(previousPeriodSales);
                    break;
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return performanceList;
}

    @Override
    public List<InventoryData> getTotalInventory() {
        if(connection == null) return null;
        List<InventoryData> inventoryList = new ArrayList<>();
        String sql = "SELECT store_ID, product_ID, SUM(previous_quantity + inventory_quantity) AS total_inventory "
                   + "FROM inventory GROUP BY store_ID, product_ID";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalInventory = rs.getInt("total_inventory");
                inventoryList.add(new InventoryData(storeId, productId, totalInventory));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return inventoryList;
    }

    @Override
    public List<SalesData> getTotalSoldItems() {
        if(connection == null) return null;
        String sql = "SELECT s.store_ID, si.product_ID, SUM(si.quantity) AS total_sold "
                   + "FROM sales s JOIN sales_items si ON s.sales_ID = si.sales_ID "
                   + "GROUP BY s.store_ID, si.product_ID";
        List<SalesData> salesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalSold = rs.getInt("total_sold");
                salesList.add(new SalesData(storeId, productId, totalSold));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesList;
    }

    @Override
    public List<InventoryData> getTotalInventory(LocalDate startDate, LocalDate endDate) {
        if (connection == null) return null;
        List<InventoryData> inventoryList = new ArrayList<>();

        String sql = "SELECT store_ID, product_ID, SUM(previous_quantity + inventory_quantity) AS total_inventory "
                   + "FROM inventory "
                   + "WHERE last_updated BETWEEN ? AND ? "
                   + "GROUP BY store_ID, product_ID";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int storeId = rs.getInt("store_ID");
                    int productId = rs.getInt("product_ID");
                    int totalInventory = rs.getInt("total_inventory");
                    inventoryList.add(new InventoryData(storeId, productId, totalInventory));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return inventoryList;
    }


    @Override
    public List<SalesData> getTotalSoldItems(LocalDate startDate, LocalDate endDate) {
        if(connection == null) return null;
        String sql = "SELECT s.store_ID, si.product_ID, SUM(si.quantity) AS total_sold "
                   + "FROM sales s JOIN sales_items si ON s.sales_ID = si.sales_ID "
                + "WHERE s.sales_date BETWEEN ? AND ? "
                   + "GROUP BY s.store_ID, si.product_ID";
        List<SalesData> salesList = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                int productId = rs.getInt("product_ID");
                int totalSold = rs.getInt("total_sold");
                salesList.add(new SalesData(storeId, productId, totalSold));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesList;
    }

    @Override
    public List<ProductSalesData> getTopSellingProduct() {
        if (connection == null) return null;
        List<ProductSalesData> salesDataList = new ArrayList<>();

        String sql = "SELECT si.product_ID, s.store_ID, SUM(si.quantity) AS total_quantity_sold " +
                     "FROM sales_items si " +
                     "JOIN sales s ON si.sales_ID = s.sales_ID " +
                     "GROUP BY si.product_ID, s.store_ID " +
                     "ORDER BY total_quantity_sold DESC " +
                     "LIMIT 40";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int productId = rs.getInt("product_ID");
                int storeId = rs.getInt("store_ID");
                int totalQuantitySold = rs.getInt("total_quantity_sold");
                salesDataList.add(new ProductSalesData(productId, storeId, totalQuantitySold));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return salesDataList;
    }

    @Override
    public Map<LocalDate, BigDecimal> getSalesByStoreAndDateRange(int storeId, LocalDate startDate, LocalDate endDate) {
        if(connection == null) return null;
        Map<LocalDate, BigDecimal> results = new HashMap<>();
        String sql = "SELECT DATE(sales_date) AS sales_day, SUM(total_amount) AS total_amount_per_day "
                + "FROM sales "
                + "WHERE store_ID = ? "
                + "AND sales_date BETWEEN ? AND ? "
                + "GROUP BY DATE(sales_date) "
                + "ORDER BY sales_day";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, storeId);
            stmt.setDate(2, Date.valueOf(startDate));
            stmt.setDate(3, Date.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Date salesDay = rs.getDate("sales_day");
                    BigDecimal totalAmountPerDay = rs.getBigDecimal("total_amount_per_day");
                    results.put(salesDay.toLocalDate(), totalAmountPerDay);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return results;
    }
    
    @Override
   public Map<Integer, BigDecimal> getTotalInventoryValuePerStore(LocalDate startDate, LocalDate endDate) {
    if (connection == null) return null;
    String query = "SELECT i.store_ID, SUM(p.product_price * i.inventory_quantity) AS total_inventory_value " +
                   "FROM inventory i " +
                   "JOIN products p ON i.product_ID = p.product_ID " +
                   "WHERE i.last_updated BETWEEN ? AND ? " +
                   "GROUP BY i.store_ID";

    Map<Integer, BigDecimal> inventoryValuePerStore = new HashMap<>();

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        // Convert LocalDate to LocalDateTime at the start of the day
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);

        System.out.println("startDate"+ startDateTime);
        System.out.println("endDate: "+ endDateTime);
        // Convert LocalDateTime to Timestamp
        Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
        Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

        stmt.setTimestamp(1, startTimestamp);
        stmt.setTimestamp(2, endTimestamp);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                BigDecimal totalValue = rs.getBigDecimal("total_inventory_value");
                inventoryValuePerStore.put(storeId, totalValue);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return inventoryValuePerStore;
}

    
    @Override
   public Map<Integer, BigDecimal> getTotalSalesAmountPerStore(LocalDate startDate, LocalDate endDate) {
    String query = "SELECT store_ID, SUM(total_amount) AS total_sales_amount " +
                       "FROM sales " +
                       "WHERE sales_date BETWEEN ? AND ? " +
                       "GROUP BY store_ID";

        Map<Integer, BigDecimal> salesAmountPerStore = new HashMap<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
             LocalDateTime startDateTime = startDate.atStartOfDay();
            LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);

            
            // Convert LocalDateTime to Timestamp
            Timestamp startTimestamp = Timestamp.valueOf(startDateTime);
            Timestamp endTimestamp = Timestamp.valueOf(endDateTime);

            stmt.setTimestamp(1, startTimestamp);
            stmt.setTimestamp(2, endTimestamp);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int storeId = rs.getInt("store_ID");
                BigDecimal totalSalesAmount = rs.getBigDecimal("total_sales_amount");
                salesAmountPerStore.put(storeId, totalSalesAmount);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return salesAmountPerStore;
}

    
    public List<Sale> getDailySalesForStore() {
       if(connection == null) return null;
       List<Sale> sales = new ArrayList<>();
       String sql = "SELECT sales_ID, total_amount, payment_method, employee_ID, sales_date " +
             "FROM sales " +
             "WHERE DATE(sales_date) = CURDATE()";

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

           try(ResultSet resultSet = preparedStatement.executeQuery()){
               while(resultSet.next()){
                    Sale sale = new Sale();
                    sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                    sale.setPayment_method(resultSet.getString("payment_method"));
                    sale.setSales_ID(resultSet.getInt("sales_ID"));
                    sale.setSales_date(resultSet.getTimestamp("sales_date"));
                    sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));

                    sales.add(sale);
               }
           }
       } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
       return sales;
    }

    @Override
    public List<Sale> getTodaysSales() {
         if(connection == null) return null;
       List<Sale> sales = new ArrayList<>();
       String sql = "SELECT sales_ID, total_amount, payment_method, employee_ID, sales_date " +
             "FROM sales " +
             "WHERE DATE(sales_date) = CURDATE()";

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){

           try(ResultSet resultSet = preparedStatement.executeQuery()){
               while(resultSet.next()){
                    Sale sale = new Sale();
                    sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                    sale.setPayment_method(resultSet.getString("payment_method"));
                    sale.setSales_ID(resultSet.getInt("sales_ID"));
                    sale.setSales_date(resultSet.getTimestamp("sales_date"));
                    sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));

                    sales.add(sale);
               }
           }
       } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
       return sales;
    }
    
    public TopSellingEmployeeDTO getTopSellingEmployeeForProduct(int productId) {
    String query = "SELECT sd.product_ID, s.employee_ID, COUNT(sd.product_ID) AS total_sales, " +
                   "SUM(sd.quantity * sd.unit_price) AS total_amount " +
                   "FROM sales s " +
                   "JOIN sales_items sd ON s.sales_ID = sd.sales_ID " +
                   "WHERE sd.product_ID = ? " +
                   "GROUP BY sd.product_ID, s.employee_ID " +
                   "ORDER BY total_sales DESC " +
                   "LIMIT 1";

    try (PreparedStatement stmt = connection.prepareStatement(query)) {
        stmt.setInt(1, productId);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                int employeeId = rs.getInt("employee_ID");
                int totalSales = rs.getInt("total_sales");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");

                return new TopSellingEmployeeDTO(productId, employeeId, totalSales, totalAmount);
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(ReportDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
    }

    return null;
}

    @Override
    public List<Sale> getSalesForStore(LocalDate startDate, LocalDate endDate, int storeId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
