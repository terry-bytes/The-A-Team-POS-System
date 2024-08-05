import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InventoryUpdater {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/carolsboutique?useSSL=false";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "Zahlo@5538";

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        Runnable task = InventoryUpdater::updateInventorySummary;
        scheduler.scheduleAtFixedRate(task, 0, 1, TimeUnit.MINUTES);
    }

    private static void updateInventorySummary() {
        String disableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 0;";
        String truncateTable = "TRUNCATE TABLE store_inventory_summary;";
        String enableForeignKeyChecks = "SET FOREIGN_KEY_CHECKS = 1;";
        String insertData = "INSERT INTO store_inventory_summary (store_ID, product_ID, product_name, product_SKU, color, size, total_quantity, productImagePath) " +
                            "SELECT pv.store_ID, p.product_ID, p.product_name, pv.product_SKU, pv.color, pv.size, SUM(i.inventory_quantity) AS total_quantity, " +
                            "pi.image_path AS productImagePath " +
                            "FROM productvariants pv " +
                            "JOIN products p ON pv.product_SKU = p.product_SKU " +
                            "JOIN inventory i ON p.product_ID = i.product_ID AND pv.store_ID = i.store_ID " +
                            "JOIN product_images pi ON p.product_ID = pi.product_ID AND pv.color = pi.color " +
                            "GROUP BY pv.store_ID, p.product_ID, pv.color, pv.size;";

        try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
             Statement stmt = conn.createStatement()) {

            // Disable foreign key checks
            System.out.println("Disabling foreign key checks...");
            stmt.execute(disableForeignKeyChecks);

            // Truncate the table
            System.out.println("Truncating table...");
            stmt.execute(truncateTable);

            // Enable foreign key checks
            System.out.println("Enabling foreign key checks...");
            stmt.execute(enableForeignKeyChecks);

            // Insert data
            System.out.println("Inserting data...");
            int rowsAffected = stmt.executeUpdate(insertData);
            System.out.println("Inventory summary updated. Rows affected: " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
