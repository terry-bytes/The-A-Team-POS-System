package ateam.DAO;


import ateam.Models.Product;
import java.util.List;

public interface ProductDAO {

    boolean addProduct(Product product);

    Product getProductById(int product_ID);

    List<Product> getAllProducts();

    boolean updateProduct(Product product);

    boolean deleteProduct(int product_ID);
}
