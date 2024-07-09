package ateam.DAO;


import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import java.util.List;

public interface SalesItemDAO {

    void createSalesItem(SalesItem salesItem);

    SalesItem getSalesItemById(int sales_item_ID); 

    List<SalesItem> getSalesItemsBySaleId(int sales_ID); 

    void updateSalesItem(SalesItem salesItem, Sale sale, Product product);

    void deleteSalesItem(int sales_item_ID); 
}
