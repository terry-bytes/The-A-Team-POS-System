package ateam.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private int product_ID;
    private String product_name;
    private String product_description;
    private double product_price;
    private int category_ID;
    private String product_SKU;
    private int quantity_in_stock;
    private String product_image_path;
    private int scanCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        return product_SKU != null ? product_SKU.equals(product.product_SKU) : product.product_SKU == null;
    }

    @Override
    public int hashCode() {
        return product_SKU != null ? product_SKU.hashCode() : 0;
    }
}
