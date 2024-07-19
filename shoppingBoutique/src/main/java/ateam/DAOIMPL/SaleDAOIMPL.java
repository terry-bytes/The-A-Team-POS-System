package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SaleDAOIMPL implements SaleDAO {

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

    /**
     *
     * @param salesItems
     */
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
}
