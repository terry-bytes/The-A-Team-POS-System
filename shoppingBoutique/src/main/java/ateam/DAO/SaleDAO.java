package ateam.DAO;


import ateam.Models.Sale;
import java.util.List;

public interface SaleDAO {

    void addSale(Sale sale);

    Sale getSaleById(int sales_ID); 

    List<Sale> getAllSales();

    void updateSale(Sale sale);

    void deleteSale(int sales_ID);
}
