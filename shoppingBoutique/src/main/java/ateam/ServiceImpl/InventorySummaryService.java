/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author user
 */
import ateam.Models.InventorySummary;
import java.util.List;

public class InventorySummaryService {
    private  InventorySummaryDAO inventorySummaryDAO;

    public InventorySummaryService() {
        this.inventorySummaryDAO = new InventorySummaryDAO();
    }

    public List<InventorySummary> getInventorySummaries(String color, String size, String store, String sku) {
        return inventorySummaryDAO.selectInventorySummaries(color, size, store, sku);
    }
}
