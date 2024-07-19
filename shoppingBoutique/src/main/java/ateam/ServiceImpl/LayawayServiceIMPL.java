/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.ServiceImpl;

import ateam.DAO.LayawayDAO;
import ateam.Models.Layaway;
import ateam.Service.LayawayService;
import java.util.List;

/**
 *
 * @author carme
 */
public class LayawayServiceIMPL implements LayawayService {
    
    private final LayawayDAO layawayDAO;
    
    public LayawayServiceIMPL(LayawayDAO layawayDAO) {
        this.layawayDAO = layawayDAO;
    }

    @Override
    public boolean addLayaway(Layaway layaway) {
        return layawayDAO.addLayaway(layaway);
    }

    @Override
    public Layaway getLayawayById(int layaway_ID) {
        return layawayDAO.getLayawayById(layaway_ID);
    }

    @Override
    public List<Layaway> getAllLayaways() {
        return layawayDAO.getAllLayaways();
    }

    @Override
    public boolean updateLayaway(Layaway layaway) {
        return layawayDAO.updateLayaway(layaway);
    }

    @Override
    public boolean deleteLayaway(int layaway_ID) {
        return layawayDAO.deleteLayaway(layaway_ID);
    }   
}
