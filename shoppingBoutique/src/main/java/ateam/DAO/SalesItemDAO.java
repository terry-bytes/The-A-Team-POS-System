
package ateam.DAO;

import ateam.DTO.TopProductSellEmployee;
import ateam.Models.SalesItem;
import java.util.List;

public interface SalesItemDAO {


 void saveSalesItem(SalesItem salesItem);
 boolean decreaseItem(int quantity,int salesItemId);

    
    List<SalesItem> getAllSalesItemByProductId(int productId);
    List<TopProductSellEmployee> getTopTotalSalesPerEmployees();
    public List<SalesItem> getAllSalesItems();

}

