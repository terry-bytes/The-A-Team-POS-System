
package ateam.DAO;

import ateam.Models.Product;
import java.util.List;

public interface ProductDAO {

    Product getProductBySKU(String productSKU);
    boolean addProduct(Product product);
    List<Product> allProduct ();
            
            
}

