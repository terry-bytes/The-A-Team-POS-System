package ateam.Servlets;

import ateam.Models.Employee;
import ateam.Models.Inventory;
import ateam.Models.Product;
import ateam.Service.InventoryService;
import ateam.Service.ProductService;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.Services.impl.InventoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@WebServlet("/InventoryServlet")
public class InventoryServlet extends HttpServlet {

    private final ProductService productService = new ProductServiceImpl();
    private final InventoryService inventoryService = new InventoryServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    

        try {
            
            boolean success = false;
            int productId=0;
            String productSku = request.getParameter("barcode");
           
            
            List<Product> getList =productService.getProductBySKU(productSku);
            // Check if the list is not empty and get the first product's ID
            if (!getList.isEmpty()) {
            productId = getList.get(0).getProduct_ID();
}
            int productID = productId;
            int additionalStock = Integer.parseInt(request.getParameter("additionalStock"));
            int storeId = ((Employee) request.getSession(false).getAttribute("Employee")).getStore_ID();
            int employeeId = ((Employee) request.getSession(false).getAttribute("Employee")).getEmployee_ID();
            
            try {
                inventoryService.replenishStock(productSku, productID, additionalStock, employeeId,storeId);
                success = true;
            } catch (SQLException e) {
                response.getWriter().write("Error replenishing stock: " + e.getMessage());
                
            }
            
             request.setAttribute("success", success);
             request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
             
    
            } catch (Exception ex) {
                Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        
     
        
    }
     
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String actions = request.getParameter("submit");
        
        switch(actions){
            
            case "viewAll":
                try {
                    // Retrieve updated inventory data and forward to JSP
                    List<Inventory> inventoryList = inventoryService.getAll();
                    request.setAttribute("inventoryList", inventoryList);
                    request.getRequestDispatcher("allInventories.jsp").forward(request, response);
                } catch (Exception ex) {
                    request.setAttribute("message", "couldnt load the jsp");
                    Logger.getLogger(InventoryServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
        break;
        
        
            case "logout":
                
                request.getSession(false).invalidate();
                request.getRequestDispatcher("login.jsp").forward(request, response);
                
                
                break;
        }
        
        
        
        
        
        
        
    }
    
}
