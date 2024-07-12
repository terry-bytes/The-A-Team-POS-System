package ateam.Service;

import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.SaleDAOIMPL;

import ateam.DAOIMPL.SalesItemDAOIMPL;

import ateam.Models.SalesItem;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class SaleService {

    private SaleDAO saleDAO = new SaleDAOIMPL();
    private SalesItemDAO salesItemDAO = new SalesItemDAOIMPL();

    public boolean processSale(List<SalesItem> items, int employeeId, int storeId, String paymentMethod) throws SQLException {

        int salesId = saleDAO.startNewSale(employeeId, storeId);
        if (salesId == -1) {
            return false;
        }

      
        for (SalesItem item : items) {
            if (!salesItemDAO.addItemToSale(salesId, item)) {
                return false;
            }
        }

        BigDecimal totalAmount = saleDAO.calculateTotalAmount(salesId);
        if (!saleDAO.updateTotalAmount(salesId, totalAmount)) {
            return false;
        }

        return saleDAO.finalizeSale(salesId, paymentMethod);
    }

    public boolean removeSaleItem(int salesItemId) throws SQLException {
        return salesItemDAO.removeSaleItem(salesItemId);
    }
}
