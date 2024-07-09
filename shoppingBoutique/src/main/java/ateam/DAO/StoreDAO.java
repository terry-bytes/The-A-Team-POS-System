package ateam.DAO;


import ateam.Models.Store;
import java.util.List;

public interface StoreDAO {

    void addStore(Store store);

    Store getStoreById(int store_ID);

    List<Store> getAllStores();

    void updateStore(Store store);

    void deleteStore(int store_ID);  
}
