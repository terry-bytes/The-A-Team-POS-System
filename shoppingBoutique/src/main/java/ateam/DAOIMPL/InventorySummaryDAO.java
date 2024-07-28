import ateam.BDconnection.Connect;
import ateam.Models.InventorySummary;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InventorySummaryDAO {
     Connection conn = null;

    private static final String SELECT_INVENTORY_SUMMARIES = 
        "SELECT * FROM store_inventory_summary WHERE 1=1";

    public InventorySummaryDAO() {
         this.conn= new Connect().connectToDB();
    }

   

    public List<InventorySummary> selectInventorySummaries(String color, String size, String store, String sku) {
        List<InventorySummary> inventorySummaries = new ArrayList<>();
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement preparedStatement = createPreparedStatement(conn, color, size, store, sku);
             ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                InventorySummary summary = new InventorySummary();
                summary.setStoreID(rs.getInt("store_ID"));
                summary.setProductID(rs.getInt("product_ID"));
                summary.setProductName(rs.getString("product_name"));
                summary.setProductSKU(rs.getString("product_SKU"));
                summary.setColor(rs.getString("color"));
                summary.setSize(rs.getString("size"));
                summary.setTotalQuantity(rs.getInt("total_quantity"));
                summary.setProductImagePath(rs.getString("productImagePath"));
                inventorySummaries.add(summary);
            }
        } catch (Exception e) {
        }
        return inventorySummaries;
    }

    private PreparedStatement createPreparedStatement(Connection connection, String color, String size, String store, String sku) throws Exception {
        String query = SELECT_INVENTORY_SUMMARIES;

        if (color != null && !color.isEmpty()) {
            query += " AND color = ?";
        }
        if (size != null && !size.isEmpty()) {
            query += " AND size = ?";
        }
        if (store != null && !store.isEmpty()) {
            query += " AND store_ID = ?";
        }
        if (sku != null && !sku.isEmpty()) {
            query += " AND product_SKU = ?";
        }

        PreparedStatement ps = connection.prepareStatement(query);

        int index = 1;
        if (color != null && !color.isEmpty()) {
            ps.setString(index++, color);
        }
        if (size != null && !size.isEmpty()) {
            ps.setString(index++, size);
        }
        if (store != null && !store.isEmpty()) {
            ps.setInt(index++, Integer.parseInt(store));
        }
        if (sku != null && !sku.isEmpty()) {
            ps.setString(index++, sku);
        }

        return ps;
    }
}
