/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.ServiceImpl;

import ateam.DAO.StoreDAO;
import ateam.Models.Store;
import ateam.Service.StoreService;
import java.util.List;

/**
 *
 * @author Train 01
 */
public class StoreServiceImpl implements StoreService{
    private final StoreDAO storeDao;

    public StoreServiceImpl(StoreDAO storeDao) {
        this.storeDao = storeDao;
    }
    

    @Override
    public boolean addStore(Store store) {
        if(getAllStores().stream()
                .filter(s -> s.getStore_name().equalsIgnoreCase(store.getStore_name()))
                .findFirst()
                .orElse(null)
                != null)
            return false;
        return storeDao.addStore(store);
    }

    @Override
    public Store getStoreById(int store_ID) {
        return storeDao.getStoreById(store_ID);
    }

    @Override
    public List<Store> getAllStores() {
        return storeDao.getAllStores();
    }

    @Override
    public boolean updateStore(Store store) {
        return storeDao.updateStore(store);
    }

    @Override
    public boolean deleteStore(int store_ID) {
        return storeDao.deleteStore(store_ID);
    }
    
}
