/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ateam.ServiceImpl;

import ateam.DAO.SaleDAO;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.Models.Sale;
import ateam.Service.SaleService2;
import java.util.List;



/**
 *
 * @author Admin
 */
public class SaleServiceImpl implements SaleService2{
    private final SaleDAO saleDao;
 
    
    public SaleServiceImpl(){
        this.saleDao = new SaleDAOIMPL();
    }
    @Override
    public List<Sale> getAllSales() {
        return saleDao.getAllSales();
    }
    
    
    
}
