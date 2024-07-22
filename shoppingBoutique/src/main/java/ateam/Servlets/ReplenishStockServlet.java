package ateam.Servlets;

import ateam.DAO.InventoryDAO;
import ateam.Services.impl.InventoryServiceImpl;
import ateam.Service.InventoryService;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReplenishStockServlet")
public class ReplenishStockServlet extends HttpServlet {
    
    
        private InventoryService inventoryService = new InventoryServiceImpl();
    
            
        
    
  

            @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Parse the request parameters
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int additionalStock = Integer.parseInt(request.getParameter("additionalStock"));
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            int storeId = Integer.parseInt(request.getParameter("storeId"));

            // Replenish stock
            
            inventoryService.replenishStock(productId, additionalStock, employeeId, storeId);
            response.getWriter().write("Stock replenished successfully.");
            
        } catch (SQLException e) {
          
            response.getWriter().write("Error replenishing stock: " + e.getMessage());
        }
    }

}
