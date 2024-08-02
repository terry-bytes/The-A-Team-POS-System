/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Service;

import ateam.Exception.DuplicateStoreException;
import ateam.Models.Store;
import java.util.List;

/**
 *
 * @author carme
 */
public interface StoreService {
     boolean addStore(Store store) throws DuplicateStoreException;

    Store getStoreById(int store_ID);

    List<Store> getAllStores();

    boolean updateStore(Store store);

    boolean deleteStore(int store_ID);  
}
