/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.ServiceImpl;

import ateam.DAO.IBTDAO;
import ateam.Models.IBT;
import ateam.Service.IBTService;
import java.util.List;

/**
 *
 * @author carme
 */
public class IBTServiceIMPL implements IBTService{
    
    private final IBTDAO ibtDAO;
    
    public IBTServiceIMPL(IBTDAO ibtDAO) {
        this.ibtDAO = ibtDAO;
    }

    @Override
    public List<IBT> getAllProducts(int product_ID) {
        return ibtDAO.getAllProducts(product_ID);
    }

    @Override
    public boolean sendIBTRequest(int product_ID, int store_ID ,String store_name, int product_quantity) {
        return ibtDAO.sendIBTRequest(product_ID, store_ID, store_name, product_quantity);
    }

    @Override
    public List<IBT> receiveIBTRequest(int store_ID) {
        return ibtDAO.receiveIBTRequest(store_ID);
    }

    @Override
    public boolean deleteRequestedIBT(int store_ID) {
        return ibtDAO.deleteRequestedIBT(store_ID);
    }

    @Override
    public boolean checkForIBTNotification(int store_ID) {
        return ibtDAO.checkForIBTNotification(store_ID);
    }
}
