package ateam.DAO;


import ateam.Models.Sale;
import java.util.List;

public interface SaleDAO {

    boolean addSale(Sale sale);

    Sale getSaleById(int sales_ID); 

    List<Sale> getAllSales();

    boolean updateSale(Sale sale);

    boolean deleteSale(int sales_ID);
}
