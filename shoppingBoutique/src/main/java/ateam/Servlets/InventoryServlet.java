package ateam.Servlets;

import ateam.Models.Inventory;
import ateam.Models.Employee;
import ateam.Service.InventoryService;
import ateam.Services.impl.InventoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {

    
    private final InventoryService inventoryService = new InventoryServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    

        try {
            
            
            
            int productId = Integer.parseInt(request.getParameter("productId"));
            int additionalStock = Integer.parseInt(request.getParameter("additionalStock"));
            int storeId = ((Employee) request.getSession(false).getAttribute("Employee")).getStore_ID();
            int employeeId = ((Employee) request.getSession(false).getAttribute("Employee")).getEmployee_ID();
            
            try {
                inventoryService.replenishStock(productId, additionalStock, storeId, employeeId);
                response.getWriter().write("Stock replenished successfully.");
            } catch (IOException | SQLException e) {
                response.getWriter().write("Error replenishing stock: " + e.getMessage());
            }
            // Retrieve updated inventory data and forward to JSP
            List<Inventory> inventoryList = inventoryService.getAll();
            request.setAttribute("inventoryList", inventoryList);
            request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
        } catch (Exception ex) {
            Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Retrieve updated inventory data and forward to JSP
            List<Inventory> inventoryList = inventoryService.getAll();
            request.setAttribute("inventoryList", inventoryList);
            request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("message","couldnt load the jsp");
            Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
