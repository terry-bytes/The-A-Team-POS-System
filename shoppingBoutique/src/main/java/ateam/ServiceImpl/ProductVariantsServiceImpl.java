package ateam.ServiceIMPL;

import ateam.DAO.ProductVariantsDAO;
import ateam.DAOIMPL.ProductVariantsDAOImpl;
import ateam.Models.ProductVariants;
import ateam.Service.ProductVariantsService;

import java.util.List;

public class ProductVariantsServiceImpl implements ProductVariantsService {

    private final ProductVariantsDAO productVariantsDAO;

    public ProductVariantsServiceImpl() {
        this.productVariantsDAO = new ProductVariantsDAOImpl();
    }

    @Override
    public void addProductVariant(ProductVariants variant) {
        productVariantsDAO.addProductVariant(variant);
    }

    @Override
    public void updateProductVariant(ProductVariants variant) {
        productVariantsDAO.updateProductVariant(variant);
    }

    @Override
    public void deleteProductVariant(int variant_ID) {
        productVariantsDAO.deleteProductVariant(variant_ID);
    }

    @Override
    public ProductVariants getProductVariantById(int variant_ID) {
        return productVariantsDAO.getProductVariantById(variant_ID);
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, int store_ID) {
        return productVariantsDAO.searchProductVariants(product_SKU, size, store_ID);
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, String color, int store_ID) {
        return productVariantsDAO.searchProductVariants(product_SKU, size, color, store_ID);
    }
}
