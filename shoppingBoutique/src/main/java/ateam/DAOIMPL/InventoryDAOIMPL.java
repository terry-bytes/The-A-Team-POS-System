package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.InventoryDAO;
import ateam.Models.Inventory;
import ateam.Models.Product;
import ateam.Models.SalesItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryDAOIMPL implements InventoryDAO {
    
    
 @Override
   public void addProductAndInventory(String barcode, int storeID, int quantity,int employeeID) throws SQLException {
    String[] barcodeParts = barcode.split("-");
    String productSKU = barcodeParts[0];
    String size = barcodeParts[1];
    String color = barcodeParts[2];
    Product product = new Product();
    Inventory inventory = new Inventory();
    Connection conn = null;
    PreparedStatement psProduct = null;
    PreparedStatement psVariant = null;
    PreparedStatement psInventory = null;

    try {
        conn = new Connect().connectToDB();
        conn.setAutoCommit(false); // Start transaction

        // Check if product exists
        String checkProductSQL = "SELECT product_ID FROM products WHERE product_SKU = ?";
        psProduct = conn.prepareStatement(checkProductSQL);
        psProduct.setString(1, productSKU);
        ResultSet rsProduct = psProduct.executeQuery();

        int productID;
        if (rsProduct.next()) {
            productID = rsProduct.getInt("product_ID");
        } else {
            // Insert new product if not exists
            String insertProductSQL = "INSERT INTO products (product_name,product_description,product_price,category_ID,product_SKU,quantity_in_stock,productImagePath,size,color) VALUES (?,?,?,?,?,?,?,?,?)";
            psProduct = conn.prepareStatement(insertProductSQL, Statement.RETURN_GENERATED_KEYS);
            psProduct.setString(1,product.getProduct_name() );
            psProduct.setString (2,product.getProduct_description());
            psProduct.setDouble(3,product.getProduct_price());
            psProduct.setInt(4,product.getCategory_ID());
            psProduct.setString(5, productSKU);
            psProduct.setInt(6,product.getQuantity_in_stock());
            psProduct.setString(7,product.getProduct_image_path());
            psProduct.setString(8,size);
            psProduct.setString(9, color);

            // Set other parameters...
            psProduct.executeUpdate();

            ResultSet generatedKeys = psProduct.getGeneratedKeys();
            if (generatedKeys.next()) {
                productID = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating product failed, no ID obtained.");
            }
        }

        // Check if variant exists
        String checkVariantSQL = "SELECT variant_ID FROM productvariants WHERE product_SKU = ? AND size = ? AND color = ? AND store_ID = ?";
        psVariant = conn.prepareStatement(checkVariantSQL);
        psVariant.setString(1, productSKU);
        psVariant.setString(2, size);
        psVariant.setString(3, color);
        psVariant.setInt(4, storeID);
        ResultSet rsVariant = psVariant.executeQuery();

        if (!rsVariant.next()) {
            // Insert new variant if not exists
            String insertVariantSQL = "INSERT INTO productvariants (product_SKU, size, color, store_ID) VALUES (?, ?, ?, ?)";
            psVariant = conn.prepareStatement(insertVariantSQL);
            psVariant.setString(1, productSKU);
            psVariant.setString(2, size);
            psVariant.setString(3, color);
            psVariant.setInt(4, storeID);
            psVariant.executeUpdate();
        }

        // Set other properties before we add on inventory
        inventory.setReorder_point(5); 
        inventory.setLast_updated(new Timestamp(System.currentTimeMillis()));
        inventory.setUpdated_by_employee_ID(employeeID);

        // Insert into inventory
        String insertInventorySQL = "INSERT INTO inventory (product_ID, store_ID, inventory_quantity, previous_quantity, reorder_point, last_updated, updated_by_employee_ID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        psInventory = conn.prepareStatement(insertInventorySQL);
        psInventory.setInt(1, productID);
        psInventory.setInt(2, storeID);
        psInventory.setInt(3, quantity);
        psInventory.setInt(4,inventory.getPrevious_quantity());
        psInventory.setInt(5,inventory.getReorder_point());
        psInventory.setTimestamp(6,inventory.getLast_updated());
        psInventory.setInt(7,employeeID);
        // Set other parameters...
        psInventory.executeUpdate();

        conn.commit(); // Commit transaction
    } catch (SQLException e) {
        if (conn != null) {
            conn.rollback(); // Rollback transaction on error
        }
        throw e;
    } finally {
        if (psProduct != null) psProduct.close();
        if (psVariant != null) psVariant.close();
        if (psInventory != null) psInventory.close();
        if (conn != null) conn.close();
    }
}

private int getInventoryId(int productID, int storeID) throws SQLException {
    String query = "SELECT inventory_ID FROM inventory WHERE product_ID = ? AND store_ID = ?";
    try (Connection conn = new Connect().connectToDB();
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, productID);
        ps.setInt(2, storeID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("inventory_ID");
        } else {
            throw new SQLException("No inventory record found for product ID " + productID + " and store ID " + storeID);
        }
    }
}

private int getPreviousQuantity(int productID, int storeID) throws SQLException {
    String query = "SELECT inventory_quantity FROM inventory WHERE product_ID = ? AND store_ID = ?";
    try (Connection conn = new Connect().connectToDB();
         PreparedStatement ps = conn.prepareStatement(query)) {
        ps.setInt(1, productID);
        ps.setInt(2, storeID);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt("inventory_quantity");
        } else {
            return 0; // Assuming the previous quantity is 0 if no record is found
        }
    }
}


    @Override
    public int getPreviousQuantity(int productId) throws Exception {
        String sql = "SELECT quantity_in_stock FROM products WHERE product_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("quantity_in_stock");
                } else {
                    
                    
                    throw new Exception("Product not found.");
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Error fetching previous quantity: " + ex.getMessage(), ex);
        }
    }
        

    @Override
    public void updateProductQuantity(int productId, int newQuantity) throws Exception {
        String sql = "UPDATE products SET quantity_in_stock = ? WHERE product_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error updating product quantity: " + ex.getMessage(), ex);
        }
    }
    
    
    @Override
     public List<Inventory> getAllInventories() throws Exception {
        String sql = "SELECT * FROM inventory";
        List<Inventory> inventoryList = new ArrayList<>();
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Inventory inventory = new Inventory();
                inventory.setInventory_ID(rs.getInt("inventory_ID"));
                inventory.setProduct_ID(rs.getInt("product_ID"));
                inventory.setStore_ID(rs.getInt("store_ID"));
                inventory.setInventory_quantity(rs.getInt("inventory_quantity"));
                inventory.setPrevious_quantity(rs.getInt("previous_quantity"));
                inventory.setReorder_point(rs.getInt("reorder_point"));
                inventory.setLast_updated(rs.getTimestamp("last_updated"));
                inventory.setUpdated_by_employee_ID(rs.getInt("updated_by_employee_ID"));
                inventoryList.add(inventory);
            }
        }
        return inventoryList;
    }
     
 @Override
    public void decreaseInventoryQuantity(int productId, int storeId, int quantity) throws Exception {
        String sql = "UPDATE inventory SET inventory_quantity = inventory_quantity - ? WHERE product_ID = ? AND store_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.setInt(3, storeId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error decreasing inventory quantity: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void decreaseProductQuantity(int productId, int quantity) throws Exception {
        String sql = "UPDATE products SET quantity_in_stock = quantity_in_stock - ? WHERE product_ID = ?";
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            throw new Exception("Error decreasing product quantity: " + ex.getMessage(), ex);
        }
    }


// Helper method to get store ID from sales table
    @Override 
    public int getStoreIdFromSales(int salesId) throws Exception {
    String sql = "SELECT store_ID FROM sales WHERE sales_ID = ?";
    try (Connection conn = new Connect().connectToDB();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, salesId);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("store_ID");
            } else {
                throw new Exception("Sale not found.");
            }
        }
    } catch (SQLException ex) {
        throw new Exception("Error fetching store ID from sales: " + ex.getMessage(), ex);
    }
}
    
    
    @Override
    public List<SalesItem> getSalesItems(int salesId) throws Exception {
        String sql = "SELECT product_ID, quantity FROM sales_items WHERE sales_ID = ?";
        List<SalesItem> salesItemList = new ArrayList<>();
        try (Connection conn = new Connect().connectToDB();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, salesId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    SalesItem salesItem = new SalesItem();
                    salesItem.setProduct_ID(rs.getInt("product_ID"));
                    salesItem.setQuantity(rs.getInt("quantity"));
                    salesItemList.add(salesItem);
                }
            }
        } catch (SQLException ex) {
            throw new Exception("Error fetching sales items: " + ex.getMessage(), ex);
        }
        return salesItemList;
    }

    }

    
