package ateam.DAOIMPL;

import ateam.BDconnection.Connect;
import ateam.DAO.ProductVariantsDAO;
import ateam.Models.ProductVariants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductVariantsDAOImpl implements ProductVariantsDAO {

    private final Connection connection;

    public ProductVariantsDAOImpl() {
        this.connection = new Connect().connectToDB();
    }

    @Override
    public void addProductVariant(ProductVariants variant) {
        String query = "INSERT INTO ProductVariants (product_SKU, size, color, store_ID) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, variant.getProduct_SKU());
            ps.setString(2, variant.getSize());
            ps.setString(3, variant.getColor());
            ps.setInt(4, variant.getStore_ID());
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void updateProductVariant(ProductVariants variant) {
        String query = "UPDATE ProductVariants SET product_SKU = ?, size = ?, color = ?, store_ID = ? WHERE variant_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, variant.getProduct_SKU());
            ps.setString(2, variant.getSize());
            ps.setString(3, variant.getColor());
            ps.setInt(4, variant.getStore_ID());
            ps.setInt(5, variant.getVariant_ID());
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void deleteProductVariant(int variant_ID) {
        String query = "DELETE FROM ProductVariants WHERE variant_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, variant_ID);
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public ProductVariants getProductVariantById(int variant_ID) {
        String query = "SELECT * FROM ProductVariants WHERE variant_ID = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, variant_ID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new ProductVariants(
                        rs.getInt("variant_ID"),
                        rs.getString("product_SKU"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getInt("store_ID")
                );
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, String color, int store_ID) {
        String query = "SELECT * FROM ProductVariants WHERE product_SKU = ? AND size = ? AND color = ? AND store_ID = ?";
        List<ProductVariants> variants = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product_SKU);
            ps.setString(2, size);
            ps.setString(3, color);
            ps.setInt(4, store_ID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                variants.add(new ProductVariants(
                        rs.getInt("variant_ID"),
                        rs.getString("product_SKU"),
                        rs.getString("product_size"),
                        rs.getString("product_colour"),
                        rs.getInt("store_ID")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return variants;
    }

    @Override
    public List<ProductVariants> searchProductVariants(String product_SKU, String size, int store_ID) {
        String query = "SELECT * FROM ProductVariants WHERE product_SKU = ? AND size = ? AND store_ID = ?";
        List<ProductVariants> variants = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, product_SKU);
            ps.setString(2, size);
            ps.setInt(3, store_ID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                variants.add(new ProductVariants(
                        rs.getInt("variant_ID"),
                        rs.getString("product_SKU"),
                        rs.getString("size"),
                        rs.getString("color"),
                        rs.getInt("store_ID")
                ));
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductVariantsDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return variants;
    }
}
