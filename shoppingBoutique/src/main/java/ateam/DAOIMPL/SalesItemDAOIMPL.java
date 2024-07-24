package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SalesItemDAO;
import ateam.Models.SalesItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesItemDAOIMPL implements SalesItemDAO {

    
    String dao ="SELECT\n" +
"    ->     p.product_ID,\n" +
"    ->     p.product_name,count(p.product_ID),\n" +
"    ->     SUM(si.quantity * si.unit_price) AS total_sales_amount,\n" +
"    ->     e.first_name,\n" +
"    ->     e.last_name,\n" +
"    ->     e.role\n" +
"    -> FROM\n" +
"    ->     sales_items si\n" +
"    -> JOIN\n" +
"    ->     sales s ON si.sales_ID = s.sales_ID\n" +
"    -> JOIN\n" +
"    ->     employees e ON s.employee_ID = e.employee_ID\n" +
"    -> JOIN\n" +
"    ->     products p ON si.product_ID = p.product_ID\n" +
"    -> GROUP BY\n" +
"    ->     p.product_ID, p.product_name, e.first_name, e.last_name, e.role\n" +
"    -> ORDER BY\n" +
"    ->     total_sales_amount DESC";
    @Override
    public void saveSalesItem(SalesItem salesItem) {
        
        
        String sql = "INSERT INTO sales_items (sales_ID, product_ID, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection conn = new Connect().connectToDB(); 
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
            // Use correct getter names from the SalesItem model
            pstmt.setInt(1, salesItem.getSales_ID());
            pstmt.setInt(2, salesItem.getProduct_ID());
            pstmt.setInt(3, salesItem.getQuantity());
            pstmt.setBigDecimal(4, salesItem.getUnit_price());
            
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}