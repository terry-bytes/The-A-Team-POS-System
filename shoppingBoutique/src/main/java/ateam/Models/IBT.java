/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Models;

/**
 *
 * @author carme
 */
public class IBT {
    private int productID;
    private int storeID;
    private int quantity;
    private String requestedtore;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getRequestedtore() {
        return requestedtore;
    }

    public void setRequestedtore(String requestedtore) {
        this.requestedtore = requestedtore;
    }
}
