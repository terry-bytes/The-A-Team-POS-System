package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ProductVariantsDAO;
import ateam.Models.ProductVariants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductVariantsDAOImpl implements ProductVariantsDAO {

    private Connect dbConnect = new Connect();

    @Override
    public List<ProductVariants> getVariantsByProductSKU(String productSKU) {
        List<ProductVariants> variants = new ArrayList<>();
        String sql = "SELECT * FROM productvariants WHERE product_SKU = ?";

        try (Connection conn = dbConnect.connectToDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, productSKU);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ProductVariants variant = new ProductVariants();
                variant.setVariant_ID(rs.getInt("variant_ID"));
                variant.setProduct_SKU(rs.getString("product_SKU"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStore_ID(rs.getInt("store_ID"));
                variants.add(variant);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variants;
    }

    @Override
    public ProductVariants getVariantByBarcode(String productSKU) {
        // Decode the barcode
        String[] parts = productSKU.split("-");
        if (parts.length != 3) {
            // Invalid barcode format
            throw new IllegalArgumentException("Invalid barcode format");
        }

        String sku = parts[0];
        String size = parts[1];
        String color = parts[2];

        ProductVariants variant = null;
        String sql = "SELECT * FROM productvariants WHERE product_SKU = ? AND size = ? AND color = ?";

        try (Connection conn = dbConnect.connectToDB(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sku);
            stmt.setString(2, size);
            stmt.setString(3, color);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                variant = new ProductVariants();
                variant.setVariant_ID(rs.getInt("variant_ID"));
                variant.setProduct_SKU(rs.getString("product_SKU"));
                variant.setSize(rs.getString("size"));
                variant.setColor(rs.getString("color"));
                variant.setStore_ID(rs.getInt("store_ID"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return variant;
    }
}
