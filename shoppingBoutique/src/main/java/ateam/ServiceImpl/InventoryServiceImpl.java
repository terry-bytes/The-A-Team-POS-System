package ateam.Services.impl;

import ateam.DAO.InventoryDAO;
import ateam.DAO.TransactionDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.DAOIMPL.TransactionDAOIMPL;
import ateam.Models.Inventory;
import ateam.Models.Transaction;
import ateam.Service.InventoryService;


import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

public class InventoryServiceImpl implements InventoryService {
    private final InventoryDAO inventoryDAO;
    private final TransactionDAO transactionDAO;

    public InventoryServiceImpl(InventoryDAO inventoryDAO, TransactionDAO transactionDAO) {
        this.inventoryDAO = inventoryDAO;
        inventoryDAO = new InventoryDAOIMPL();
        this.transactionDAO = transactionDAO;
        transactionDAO = new TransactionDAOIMPL();
        
    }

    @Override
    public void replenishStock(int product_ID, int store_ID, int quantity, int employee_ID) throws SQLException {
        // Fetch current inventory
        Inventory currentInventory = inventoryDAO.getInventoryByProductAndStore(product_ID, store_ID);

        int previousQuantity = currentInventory.getInventory_quantity();
        int newQuantity = previousQuantity + quantity;

        // Update inventory
        currentInventory.setInventory_quantity(newQuantity);
        currentInventory.setLast_updated(new Timestamp(System.currentTimeMillis()));
        currentInventory.setAdded_by_employee_ID(employee_ID);
        inventoryDAO.updateInventory(currentInventory);

        // Log transaction
        Transaction transaction = new Transaction();
        transaction.setProduct_ID(product_ID);
        transaction.setStore_ID(store_ID);
        transaction.setQuantity(quantity);
        transaction.setTransaction_date(new Timestamp(System.currentTimeMillis()));
        transaction.setEmployee_ID(employee_ID);
        transaction.setPrevious_quantity(previousQuantity);
        transactionDAO.insertTransaction(transaction);
    }
    
    @Override
    public Inventory getInventoryByProductAndStore(int product_ID, int store_ID) throws SQLException {
        return inventoryDAO.getInventoryByProductAndStore(product_ID, store_ID);
    }

    @Override
    public List<Inventory> getInventoryByStore(int store_ID) throws SQLException {
        return inventoryDAO.getInventoryByStore(store_ID);
    }
}
