package ateam.Servlets;

import ateam.BDconnection.Connect;
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
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAOIMPL();
    private final ProductService productService = new ProductServiceImpl(productDAO);
    private SaleDAO saleDAO = new SaleDAOIMPL();
    private SalesItemDAO salesItemDAO = new SalesItemDAOIMPL();
    private Connect dbConnect = new Connect();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee loggedInUser = (Employee) session.getAttribute("Employee");
        List<Product> scannedItems = (List<Product>) session.getAttribute("scannedItems");

        if (scannedItems == null) {
            scannedItems = new ArrayList<>();
            session.setAttribute("scannedItems", scannedItems);
        }

        String submit = request.getParameter("submit");
        String sku = request.getParameter("input-field");
        String sku2 = request.getParameter("sku");
        String managerPassword = request.getParameter("manager_password");

        try {
            switch (submit) {
                case "Add-Item":
                case "auto-submit":
                    List<Product> foundProducts = productService.getProductBySKU(sku);
                    session.setAttribute("foundProducts", foundProducts);
                    if (!foundProducts.isEmpty()) {
                        Product productToAdd = foundProducts.get(0);

                        boolean foundInScannedItems = false;
                        for (Product scannedItem : scannedItems) {
                            if (scannedItem.getProduct_SKU().equals(sku)) {
                                scannedItem.setScanCount(scannedItem.getScanCount() + 1);
                                foundInScannedItems = true;
                                break;
                            }
                        }

                        if (!foundInScannedItems) {
                            productToAdd.setScanCount(1);
                            scannedItems.add(productToAdd);
                        }
                    }
                    break;

                case "Remove-Item":
                    // Set the SKU in the session for the removal confirmation page
                    session.setAttribute("itemToRemoveSKU", sku2);
                    // Forward to confirmation page
                    if (!response.isCommitted()) {
                        request.getRequestDispatcher("confirmRemove.jsp").forward(request, response);
                    }
                    return; // Return to avoid further processing

                case "Confirm-Remove":
                    // Get the SKU to remove from the session
                    String skuToRemove = (String) session.getAttribute("itemToRemoveSKU");
                    if (skuToRemove != null && verifyManagerPassword(loggedInUser.getStore_ID(), managerPassword)) {
                        // Find the item with the specified SKU and update its scan count or remove it
                        for (Product item : scannedItems) {
                            if (item.getProduct_SKU().equals(skuToRemove)) {
                                if (item.getScanCount() > 1) {
                                    item.setScanCount(item.getScanCount() - 1);
                                } else {
                                    scannedItems.remove(item);
                                }
                                break;
                            }
                        }
                        session.removeAttribute("itemToRemoveSKU"); // Clean up session attribute
                    } else {
                        // Handle password verification failure or missing SKU
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Invalid manager password or unauthorized access");
                    }
                    break;

                case "Complete-Sale":
                    BigDecimal totalAmount = BigDecimal.valueOf(calculateTotalPrice(scannedItems));

                    if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) { // Check if total is zero or negative
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.WARNING, "Invalid total amount: " + totalAmount);
                        request.setAttribute("errorMessage", "Total amount cannot be zero or negative.");
                        break; // Don't proceed with saving the sale
                    }
                    Sale newSale = new Sale();
                    newSale.setSales_date(new Timestamp(System.currentTimeMillis()));
                    newSale.setTotal_amount(BigDecimal.valueOf(calculateTotalPrice(scannedItems)));
                    newSale.setPayment_method(request.getParameter("payment_method"));
                    if (loggedInUser != null) {
                        newSale.setEmployee_ID(loggedInUser.getEmployee_ID());
                        newSale.setStore_ID(loggedInUser.getStore_ID());
                    } else {
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Employee not logged in");
                    }

                    int newSalesID = saleDAO.saveSale(newSale);
                    if (newSalesID != -1) {
                        for (Product item : scannedItems) {
                            SalesItem salesItem = new SalesItem();
                            salesItem.setSales_ID(newSalesID);
                            salesItem.setProduct_ID(item.getProduct_ID());
                            salesItem.setQuantity(item.getScanCount());
                            salesItem.setUnit_price(BigDecimal.valueOf(item.getProduct_price()));

                            salesItemDAO.saveSalesItem(salesItem);
                        }

                        scannedItems.clear();
                    } else {
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Failed to save sale");
                    }
                    break;

                case "Inventory":
                    request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
                    break;

                // ... (other cases)
            }

           List<BigDecimal> totalPricePerItem = scannedItems.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct_price()).multiply(BigDecimal.valueOf(item.getScanCount())))
                .collect(Collectors.toList());

            double totalPrice = calculateTotalPrice(scannedItems); // For display purposes (can be BigDecimal)
            request.setAttribute("scannedItems", scannedItems);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("totalPricePerItem", totalPricePerItem); 

            request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Error in ProductServlet", e);
        }
    }

    private boolean verifyManagerPassword(int storeID, String password) {
        Logger.getLogger(ProductServlet.class.getName()).log(Level.INFO, "Verifying manager password for store ID: " + storeID);
        String hashedPassword = getManagerHashedPassword(storeID);
        Logger.getLogger(ProductServlet.class.getName()).log(Level.INFO, "Manager hashed password retrieved: " + hashedPassword);

        boolean result = hashedPassword != null && BCrypt.checkpw(password, hashedPassword);
        Logger.getLogger(ProductServlet.class.getName()).log(Level.INFO, "Password verification result: " + result);
        return result;
    }

    private String getManagerHashedPassword(int storeID) {
        String hashedPassword = null;
        try (Connection conn = dbConnect.connectToDB();
                PreparedStatement stmt = conn.prepareStatement("SELECT employee_password FROM employees WHERE store_ID = ? AND role = 'Manager'")) {
            stmt.setInt(1, storeID);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                hashedPassword = rs.getString("employee_password");
            }
            Logger.getLogger(ProductServlet.class.getName()).log(Level.INFO, "Fetched hashed password from DB: " + hashedPassword);
        } catch (SQLException e) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Database error while retrieving manager password", e);
        }
        return hashedPassword;
    }

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