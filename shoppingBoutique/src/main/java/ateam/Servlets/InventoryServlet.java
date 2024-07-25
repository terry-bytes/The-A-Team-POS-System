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
import java.util.stream.Collectors;


@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {

    
    private final InventoryService inventoryService = new InventoryServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    

        try {
            
            
            int reorder = 5;
            String productSku = request.getParameter("barcode");
            //String [] sku = productSku.split("-");
            int productId = 0;
            int additionalStock = Integer.parseInt(request.getParameter("additionalStock"));
            int storeId = ((Employee) request.getSession(false).getAttribute("Employee")).getStore_ID();
            int employeeId = ((Employee) request.getSession(false).getAttribute("Employee")).getEmployee_ID();
            
            try {
                inventoryService.replenishStock(productSku, productId, additionalStock, employeeId,storeId);
                response.sendRedirect("success.jsp");
            } catch (IOException | SQLException e) {
                response.getWriter().write("Error replenishing stock: " + e.getMessage());
                request.getRequestDispatcher("error.jsp").forward(request, response);
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
