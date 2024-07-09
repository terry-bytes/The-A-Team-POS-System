package ateam.DAO;


import ateam.Models.Layaway;
import java.util.List;

public interface LayawayDAO {

    void addLayaway(Layaway layaway);

    Layaway getLayawayById(int layaway_ID);

    List<Layaway> getAllLayaways();

    void updateLayaway(Layaway layaway);

    void deleteLayaway(int layaway_ID); 
}
