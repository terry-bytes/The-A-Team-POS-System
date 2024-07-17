package ateam.Servlets;

import ateam.DAO.ProductDAO;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.ProductService;
import ateam.ServiceImpl.ProductServiceImpl;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAOIMPL();
    private final ProductService productService = new ProductServiceImpl(productDAO);
     private SaleDAO saleDAO = new SaleDAOIMPL();
    private SalesItemDAO salesItemDAO = new SalesItemDAOIMPL();
    

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee manager = (Employee) request.getSession(false).getAttribute("Employee");
        List<Product> scannedItems = (List<Product>) session.getAttribute("scannedItems");
        if (scannedItems == null) {
            scannedItems = new ArrayList<>();
            session.setAttribute("scannedItems", scannedItems);
        }

        String submit = request.getParameter("submit");
        String sku = request.getParameter("input-field");

        switch (submit) {
            case "Add-Item":
            case "auto-submit":
                List<Product> foundProducts = productService.getProductBySKU(sku);
                if (!foundProducts.isEmpty()) {
                    Product productToAdd = foundProducts.get(0);

                    // Check if the product is already in the list
                    boolean foundInScannedItems = false;
                    for (Product scannedItem : scannedItems) {
                        if (scannedItem.getProduct_SKU().equals(sku)) {
                            scannedItem.setScanCount(scannedItem.getScanCount() + 1);
                            foundInScannedItems = true;
                            break; // Exit the loop once found
                        }
                    }

                    // If not found, add it to the list with count 1
                    if (!foundInScannedItems) {
                        productToAdd.setScanCount(1);
                        scannedItems.add(productToAdd);
                    }
                }
                break;
                 case "Complete-Sale":
                Sale newSale = new Sale();
                newSale.setSales_date(new Timestamp(System.currentTimeMillis()));
                newSale.setTotal_amount(BigDecimal.valueOf(calculateTotalPrice(scannedItems)));
                newSale.setPayment_method(request.getParameter("payment_method")); 

                Employee loggedInUser = (Employee) session.getAttribute("Employee");
                if (loggedInUser != null) {
                    newSale.setEmployee_ID(loggedInUser.getEmployee_ID());
                } else {
                    // Handle no logged-in employee (error message, etc.)
                    Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Employee not logged in");
                }
                newSale.setStore_ID(1); // Replace with actual store ID if available

                int newSalesID = saleDAO.saveSale(newSale);
                if (newSalesID != -1) { // Check if sale was saved successfully
                    for (Product item : scannedItems) {
                        SalesItem salesItem = new SalesItem();
                        salesItem.setSales_ID(newSalesID);
                        salesItem.setProduct_ID(item.getProduct_ID());
                        salesItem.setQuantity(item.getScanCount());
                        salesItem.setUnit_price(BigDecimal.valueOf(item.getProduct_price()));

                        salesItemDAO.saveSalesItem(salesItem);
                    }

                    // Clear cart and reset total after successful sale
                    scannedItems.clear();
            double totalPrice = 0.0;
                } else {
                    // Handle sale saving failure (error message, etc.)
                    Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Failed to save sale");
                }
                break;

            // ... (other cases: Complete-Sale, Remove-Item, etc.)
        }

        // Calculate total price (assuming you have a calculateTotalPrice method)
        double totalPrice = calculateTotalPrice(scannedItems);

        request.setAttribute("scannedItems", scannedItems);
        request.setAttribute("totalPrice", totalPrice);
        request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
    }

    // ... (calculateTotalPrice and other existing methods)
    private double calculateTotalPrice(List<Product> scannedItems) {
        double totalPrice = 0.0;
        for (Product item : scannedItems) {
            double itemPrice = item.getProduct_price();
            int quantity = item.getScanCount();
            totalPrice += itemPrice * quantity;
        }
        return totalPrice;
    }
}
