package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.SalesItemDAO;
import ateam.Models.SalesItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SalesItemDAOIMPL implements SalesItemDAO {
    private Connection connection;
    public SalesItemDAOIMPL(){
        this(new Connect().connectToDB());
    }
    
    public SalesItemDAOIMPL(Connection connection){
        this.connection = connection;
    }
    
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
    public List<SalesItem> getAllSalesItemByProductId(int productId) {
        if(connection == null) return null;
        
        List<SalesItem> salesItems = new ArrayList<>();
        String sql = "SELECT sales_item_ID, sales_ID, quantity, unit_price "
                + "FROM sales_items "
                + "WHERE product_ID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,productId);
            
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    SalesItem salesItem = new SalesItem();
                    salesItem.setSales_item_ID(resultSet.getInt("sales_item_ID"));
                    salesItem.setSales_ID(resultSet.getInt("sales_ID"));
                    salesItem.setProduct_ID(productId);
                    salesItem.setQuantity(resultSet.getInt("quantity"));
                    salesItem.setUnit_price(resultSet.getBigDecimal("unit_price"));

                    salesItems.add(salesItem);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalesItemDAOIMPL.class.getName()).log(Level.SEVERE, null, ex);
        }
        return salesItems;
    }
}
