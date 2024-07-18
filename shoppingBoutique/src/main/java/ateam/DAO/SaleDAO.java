package ateam.DAO;

import ateam.Models.Sale;
import java.math.BigDecimal;
import java.util.List;

public interface SaleDAO {

    int startNewSale(int employeeId, int storeId);

    BigDecimal calculateTotalAmount(int salesId);

    boolean updateTotalAmount(int salesId, BigDecimal totalAmount);

    boolean finalizeSale(int salesId, String paymentMethod);
    
    List<Sale> getAllSales();

int saveSale(Sale sale);

}
