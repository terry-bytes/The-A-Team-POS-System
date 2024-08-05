package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Models.Voucher;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleDAOIMPL implements SaleDAO {
    private Connection connection;

    public SaleDAOIMPL() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public int saveSale(Sale sale) {
        String sql = "INSERT INTO sales (sales_date, total_amount, payment_method, employee_ID, store_ID) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setTimestamp(1, sale.getSales_date());
            pstmt.setBigDecimal(2, sale.getTotal_amount());
            pstmt.setString(3, sale.getPayment_method());
            pstmt.setInt(4, sale.getEmployee_ID());
            pstmt.setInt(5, sale.getStore_ID());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public void updateStockQuantities(List<SalesItem> salesItems) {
        String sql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ? WHERE product_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            for (SalesItem item : salesItems) {
                pstmt.setInt(1, item.getQuantity());
                pstmt.setInt(2, item.getProduct_ID());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public BigDecimal calculateTotalAmount(int salesId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        String sql = "SELECT total_amount FROM sales WHERE sales_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, salesId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalAmount = rs.getBigDecimal("total_amount");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return totalAmount;
    }

    @Override
    public boolean updateTotalAmount(int salesId, BigDecimal totalAmount) {
        boolean success = false;
        String sql = "UPDATE sales SET total_amount = ? WHERE sales_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setBigDecimal(1, totalAmount);
            stmt.setInt(2, salesId);
            int rowsUpdated = stmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    @Override
    public boolean finalizeSale(int salesId, String paymentMethod) {
        boolean success = false;
        String sql = "UPDATE sales SET payment_method = ? WHERE sales_ID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paymentMethod);
            stmt.setInt(2, salesId);
            int rowsUpdated = stmt.executeUpdate();
            success = rowsUpdated > 0;
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    @Override
    public List<Sale> getAllSales() {
        List<Sale> sales = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID, store_ID FROM sales";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    Sale sale = new Sale();
                    sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                    sale.setPayment_method(resultSet.getString("payment_method"));
                    sale.setSales_ID(resultSet.getInt("sales_ID"));
                    sale.setSales_date(resultSet.getTimestamp("sales_date"));
                    sale.setStore_ID(resultSet.getInt("store_ID"));
                    sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));

                    sales.add(sale);
                }
            } catch (SQLException ex) {
                Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sales;
    }

    @Override
    public int startNewSale(int employeeId, int storeId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Sale> getSalesbyStoreId(int storeId) {
        List<Sale> sales = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID FROM sales WHERE store_ID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, storeId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
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
        }
        return sales;
    }

    @Override
    public List<Sale> getDailySalesForStore(int storeId) {
       if(connection == null) return null;
       List<Sale> sales = new ArrayList<>();
       String sql = "SELECT sales_ID, total_amount, payment_method, employee_ID, sales_date " +
             "FROM sales " +
             "WHERE store_ID = ? AND DATE(sales_date) = CURDATE()";

       try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
           preparedStatement.setInt(1, storeId);
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
    public List<Sale> getLeastPerformingStores(LocalDate endDate) {
        if(connection == null) return null;
        
        List<Sale> sales = new ArrayList<>();
        String sql = "SELECT sales_ID, total_amount, payment_method, employee_ID, sales_date " +
                     "FROM sales " +
                     "WHERE sales_date BETWEEN ? AND NOW()";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            Timestamp endDateTimestamp = Timestamp.valueOf(endDate.atStartOfDay());

            // Set the endDate parameter
            preparedStatement.setTimestamp(1, endDateTimestamp);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Sale sale = new Sale();
                    sale.setSales_ID(resultSet.getInt("sales_ID"));
                    sale.setTotal_amount(resultSet.getBigDecimal("total_amount"));
                    sale.setPayment_method(resultSet.getString("payment_method"));
                    sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                    sale.setSales_date(resultSet.getTimestamp("sales_date"));

                    sales.add(sale);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sales;
    }

    @Override
    public List<Sale> getSalesForStoreByRange(int storeId, LocalDate startDate, LocalDate endDate) {
         List<Sale> sales = new ArrayList<>();
        if (connection != null) {
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID FROM sales "
                    + "WHERE store_ID = ? AND sales_date BETWEEN ? AND ? ";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                preparedStatement.setInt(1, storeId);
                preparedStatement.setTimestamp(2, Timestamp.valueOf(startDate.atStartOfDay()));
                preparedStatement.setTimestamp(3, Timestamp.valueOf(endDate.atStartOfDay()));
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
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
        }
        return sales;
    }

    @Override
    public void addVoucher(String voucherNumber, BigDecimal amount) {
        Voucher voucher = new Voucher();
        String query = "INSERT INTO vouchers (voucher_code, amount, created_at) VALUES (?, ?, ?)";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement stmt = conn.prepareStatement(query))  {
            stmt.setString(1,voucherNumber );
            stmt.setBigDecimal(2, amount);
            stmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    public BigDecimal validateVoucher(String voucherCode) {
        BigDecimal amount = BigDecimal.ZERO;
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement stmt = conn.prepareStatement("SELECT amount FROM vouchers WHERE voucher_code = ? AND used = 0")) {
            stmt.setString(1, voucherCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                amount = rs.getBigDecimal("amount");
            }
        } catch (SQLException e) {
           
        }
        return amount;
    }
    @Override
    public void markVoucherAsUsed(String voucherCode) {
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement stmt = conn.prepareStatement("UPDATE vouchers SET used = 1 WHERE voucher_code = ?")) {
            stmt.setString(1, voucherCode);
            stmt.executeUpdate();
        } catch (SQLException e) {
            
        }
    }

}
