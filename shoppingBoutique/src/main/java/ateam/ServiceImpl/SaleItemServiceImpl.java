/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.DTO.TopProductSellEmployee;
import ateam.Models.SalesItem;
import ateam.Service.SaleItemsService;
import java.util.List;

/**
 *
 * @author Admin
 */
public class SaleItemServiceImpl implements SaleItemsService{
    private final SalesItemDAO salesItemDao;
    
    public SaleItemServiceImpl(){
        this(new SalesItemDAOIMPL());
    }
    
    public SaleItemServiceImpl(SalesItemDAO salesItemDao){
        this.salesItemDao = salesItemDao;
    }

    @Override
    public List<SalesItem> getSalesItemsByProductId(int productId) {
        return salesItemDao.getAllSalesItemByProductId(productId);
    }

    @Override
    public List<TopProductSellEmployee> getTotalSalesPerProduct() {
        return salesItemDao.getTopTotalSalesPerEmployees();
    }
}
