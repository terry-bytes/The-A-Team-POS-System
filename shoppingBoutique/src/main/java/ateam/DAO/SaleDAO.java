package ateam.DAO;

import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface SaleDAO {



    int startNewSale(int employeeId, int storeId);

    BigDecimal calculateTotalAmount(int salesId);

    boolean updateTotalAmount(int salesId, BigDecimal totalAmount);

    boolean finalizeSale(int salesId, String paymentMethod);
    
    List<Sale> getAllSales();

    int saveSale(Sale sale);

    List<Sale> getSalesbyStoreId(int storeId);
    void updateStockQuantities(List<SalesItem> salesItems);
    List<Sale> getDailySalesForStore(int storeId);
    List<Sale> getLeastPerformingStores(LocalDate endDate);
    

}
