package ateam.DAO;


import ateam.Models.Layaway;
import java.util.List;

public interface LayawayDAO {

    boolean addLayaway(Layaway layaway);

    Layaway getLayawayById(int layaway_ID);

    List<Layaway> getAllLayaways();

    boolean updateLayaway(Layaway layaway);

    boolean deleteLayaway(int layaway_ID); 
}
