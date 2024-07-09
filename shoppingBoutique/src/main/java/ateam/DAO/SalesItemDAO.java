package ateam.DAO;


import ateam.Models.SalesItem;
import java.util.List;

public interface SalesItemDAO {

    boolean createSalesItem(SalesItem salesItem);

    SalesItem getSalesItemById(int sales_item_ID); 

    List<SalesItem> getSalesItemsBySaleId(int sales_ID); 

    boolean updateSalesItem(SalesItem salesItem);

    boolean deleteSalesItem(int sales_item_ID); 
}
