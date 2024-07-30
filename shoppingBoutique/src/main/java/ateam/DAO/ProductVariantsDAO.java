package ateam.DAO;

import ateam.Models.ProductVariants;
import java.util.List;

public interface ProductVariantsDAO {

  public ProductVariants getVariantByBarcode(String productSKU);
  public List<ProductVariants> getVariantsByProductSKU(String productSKU);
  List<ProductVariants> getAllVariants();
}
