package ateam.DAO;

import ateam.Models.SalesItem;
import java.util.List;

public interface SalesItemDAO {

    boolean addItemToSale(int salesId, SalesItem item);

    boolean removeSaleItem(int salesItemId);
}
