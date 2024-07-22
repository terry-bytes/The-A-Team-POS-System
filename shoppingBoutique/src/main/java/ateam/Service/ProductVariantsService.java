package ateam.Service;

import ateam.Models.ProductVariants;
import java.util.List;

public interface ProductVariantsService {
    
    void addProductVariant(ProductVariants variant);
    
    void updateProductVariant(ProductVariants variant);
    
    void deleteProductVariant(int variant_ID);
    
    ProductVariants getProductVariantById(int variant_ID);
    
    List<ProductVariants> searchProductVariants(String product_SKU, String size, String color, int store_ID);
    
    List<ProductVariants> searchProductVariants(String product_SKU, String size, int store_ID);
}
