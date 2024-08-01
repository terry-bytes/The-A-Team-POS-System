/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.DAO;

import ateam.Models.IBT;
import java.util.List;

/**
 *
 * @author carme
 */
public interface IBTDAO {
    List<IBT> getAllProducts(int product_ID);
    boolean sendIBTRequest(int product_ID, int store_ID,String store_name, int product_quantity, String customerName, String customerNumber, String customerEmail, int storeID);
    List<IBT> receiveIBTRequest(int store_ID);
    boolean deleteRequestedIBT(int store_ID);
    boolean checkForIBTNotification(int store_ID);
    String retrieveCustomerNumber(int layawayID);
    int retrieveStoreID(int IBTID);
}
