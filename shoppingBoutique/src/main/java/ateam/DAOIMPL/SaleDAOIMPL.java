package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.Models.Sale;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleDAOIMPL implements SaleDAO {

    private static final String SQL_INSERT_SALE = "INSERT INTO sales (sales_date, employee_ID, store_ID) VALUES (CURRENT_TIMESTAMP, ?, ?)";
    private static final String SQL_SELECT_TOTAL_AMOUNT = "SELECT SUM(quantity * unit_price) AS total_amount FROM sales_items WHERE sales_ID = ?";
    private static final String SQL_UPDATE_TOTAL_AMOUNT = "UPDATE sales SET total_amount = ? WHERE sales_ID = ?";
    private static final String SQL_UPDATE_PAYMENT_METHOD = "UPDATE sales SET payment_method = ? WHERE sales_ID = ?";

    private Connection connection;

    public SaleDAOIMPL() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public int startNewSale(int employeeId, int storeId) {
        int salesId = -1;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_INSERT_SALE, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, employeeId);
            stmt.setInt(2, storeId);
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        salesId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesId;
    }

    @Override
    public BigDecimal calculateTotalAmount(int salesId)  {
        BigDecimal totalAmount = BigDecimal.ZERO;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_SELECT_TOTAL_AMOUNT)) {
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
    public boolean updateTotalAmount(int salesId, BigDecimal totalAmount){
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_TOTAL_AMOUNT)) {
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
    public boolean finalizeSale(int salesId, String paymentMethod)  {
        boolean success = false;
        try (PreparedStatement stmt = connection.prepareStatement(SQL_UPDATE_PAYMENT_METHOD)) {
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
        if(connection != null){
            String sql = "SELECT sales_ID, sales_date, total_amount, payment_method, employee_ID, store_ID FROM sales";
            try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
                try(ResultSet resultSet = preparedStatement.executeQuery()){
                    while(resultSet.next()){
                        Sale sale = new Sale();
                        sale.setEmployee_ID(resultSet.getInt("employee_ID"));
                        sale.setPayment_method(resultSet.getString("payment_method"));
                        sale.setSales_ID(resultSet.getInt("sales_ID"));
                        sale.setSales_date(resultSet.getTimestamp("sales_date"));
                        sale.setStore_ID(resultSet.getInt("store_Id"));
                        sale.setTotal_amount(resultSet.getDouble("total_amount"));
                        
                        sales.add(sale);
                    }
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(SaleDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sales;
    }
}
