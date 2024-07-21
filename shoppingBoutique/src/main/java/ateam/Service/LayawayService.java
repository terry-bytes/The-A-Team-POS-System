/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Service;

import ateam.Models.Layaway;
import java.util.List;

/**
 *
 * @author carme
 */
public interface LayawayService {
    
    boolean addLayaway(Layaway layaway);

    Layaway getLayawayById(int layaway_ID);

    List<Layaway> getAllLayaways();

    boolean updateLayaway(Layaway layaway);

    boolean deleteLayaway(int layaway_ID); 
    
    Layaway emailData(String customerEmail);
}
