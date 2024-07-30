
package ateam.DAO;

import ateam.Models.SalesItem;
import java.util.List;

public interface SalesItemDAO {

 void saveSalesItem(SalesItem salesItem);
 boolean decreaseItem(int quantity,int salesItemId);
}

