package ateam.DAO;


import ateam.Models.Store;
import java.util.List;

public interface StoreDAO {

    boolean addStore(Store store);

    Store getStoreById(int store_ID);

    List<Store> getAllStores();

    boolean updateStore(Store store);

    boolean deleteStore(int store_ID);  
}
