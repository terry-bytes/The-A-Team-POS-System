package ateam.Service;

import ateam.Models.Inventory;
import java.sql.SQLException;
import java.util.List;

public interface InventoryService {

    void replenishStock(String productSKU, int product_ID, int store_ID, int quantity, int employee_ID) throws SQLException;

    List<Inventory> getAll() throws Exception;

    void processSale(int salesId) throws SQLException;
}
