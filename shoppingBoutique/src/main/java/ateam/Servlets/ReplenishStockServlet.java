package ateam.Servlets;

import ateam.DAO.InventoryDAO;
import ateam.DAO.TransactionDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.DAOIMPL.TransactionDAOIMPL;
import ateam.Services.impl.InventoryServiceImpl;
import ateam.Models.Inventory;
import ateam.Service.InventoryService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ReplenishStockServlet")
public class ReplenishStockServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int product_ID = Integer.parseInt(request.getParameter("product_ID"));
        int store_ID = Integer.parseInt(request.getParameter("store_ID"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int employee_ID = Integer.parseInt(request.getParameter("employee_ID"));

        Connection connection = null;
        String message;

        try {
            // Establish the database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/carolsboutique?useSSL=false", "root", "Zahlo@5538");

            // Initialize the service
            InventoryDAO inventoryDAO = new InventoryDAOIMPL();
            TransactionDAO transactionDAO = new TransactionDAOIMPL();
            InventoryService inventoryService = new InventoryServiceImpl(inventoryDAO, transactionDAO);
            request.getSession(false).getAttribute("Employee");
            // Replenish stock
            inventoryService.replenishStock(product_ID, store_ID, quantity, employee_ID);
            message = "Stock replenished successfully.";

            // Get updated inventory list
            List<Inventory> inventoryList = inventoryService.getInventoryByStore(store_ID);
            request.setAttribute("inventoryList", inventoryList);

        } catch (ClassNotFoundException | SQLException e) {
           
            message = "Error replenishing stock: " + e.getMessage();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    
                }
            }
        }

        // Set the message attribute and forward to the JSP
        request.setAttribute("message", message);
        request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
    }
}
