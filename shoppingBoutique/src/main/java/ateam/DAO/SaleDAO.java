package ateam.DAO;

import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.util.List;

public interface SaleDAO {

int saveSale(Sale sale);
void updateStockQuantities(List<SalesItem> salesItems);
}
