package ateam.DAO;


import ateam.Models.Product;
import java.util.List;

public interface ProductDAO {

    void addProduct(Product product);

    Product getProductById(int product_ID);

    List<Product> getAllProducts();

    void updateProduct(Product product);

    void deleteProduct(int product_ID);
    
    Product getProductByBarcode(String product_SKU);
}
