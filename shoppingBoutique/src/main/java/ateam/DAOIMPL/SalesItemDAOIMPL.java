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
    
    
    @Override
    public boolean decreaseItem(int quantity,int salesItemId){
        
        boolean success = false;
        String sql = "Update sales_items set quantity = quantity -? where sales_item_ID=?";
        
        try(
                Connection conn = new Connect().connectToDB();
                PreparedStatement prs = conn.prepareStatement(sql);
                
                ){
            
            prs.setInt(1, quantity);
            prs.setInt(2,salesItemId);
            
            if(prs.executeUpdate()>0){
                success = true;
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }
}