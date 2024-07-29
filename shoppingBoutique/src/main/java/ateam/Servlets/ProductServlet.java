package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAO.ProductDAO;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Layaway;
import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Models.SmsSender;

import ateam.Service.InventoryService;

import ateam.Service.EmailService;

import ateam.Service.ProductService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.Services.impl.InventoryServiceImpl;
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
    private final ProductService productService = new ProductServiceImpl();
    private SaleDAO saleDAO = new SaleDAOIMPL();
    private SalesItemDAO salesItemDAO = new SalesItemDAOIMPL();
    private EmailService emailService = new EmailServiceImpl();
    private Connect dbConnect = new Connect();
    private InventoryService inventoryService = new InventoryServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        Employee loggedInUser = (Employee) session.getAttribute("Employee");
        List<Product> scannedItems = (List<Product>) session.getAttribute("scannedItems");
        List<Layaway> scannedItemsList = new ArrayList<>();

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

                    session.setAttribute("scannedItemsList", foundProducts);

                    if (foundProducts.isEmpty()) {
                        // Handle the case where no products are found
                        request.setAttribute("errorMessage", "Product with SKU '" + sku + "' not found.");
                        request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
                        return; // Return to avoid further processing
                    }

                    session.setAttribute("foundProducts", foundProducts);

                    // Assuming SKU format includes size and color
                    String[] skuParts = sku.split("-");
                    String productSKU = skuParts[0];
                    String size = skuParts.length > 1 ? skuParts[1] : "";
                    String color = skuParts.length > 2 ? skuParts[2] : "";

                    boolean foundInScannedItems = false;

                    for (Product scannedItem : scannedItems) {
                        // Check if the product and variant (size/color) match
                        if (scannedItem.getProduct_SKU().equals(productSKU)
                                && scannedItem.getSize().equals(size)
                                && scannedItem.getColor().equals(color)) {
                            // Increase the quantity if the item is already scanned
                            scannedItem.setScanCount(scannedItem.getScanCount() + 1);
                            foundInScannedItems = true;
                            break;
                        }
                    }

                    // If not found, add it to the list with count set to 1
                    if (!foundInScannedItems) {
                        Product productToAdd = foundProducts.get(0);
                        productToAdd.setSize(size);
                        productToAdd.setColor(color);
                        productToAdd.setScanCount(1);
                        scannedItems.add(productToAdd);
                        request.setAttribute("ScannedItemsList", scannedItems);
                    }
                    for (Product product : scannedItems) {
                        String productSKUU = product.getProduct_SKU();
                        double productPrice = product.getProduct_price();
                        String productName = product.getProduct_name();
                        int productID = product.getProduct_ID();
                        System.out.println("Product SKU: " + productSKU);
                        Layaway layaway = new Layaway();
                        layaway.setProductSKU(productSKU);
                        layaway.setProductPrice(productPrice);
                        layaway.setProductName(productName);
                        layaway.setProductID(String.valueOf(productID));
                        scannedItemsList.add(layaway);

                    }

                    session.setAttribute("scannedItemsList", scannedItemsList);
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
                        boolean itemRemoved = false;
                        for (Product item : scannedItems) {
                            if (item.getProduct_SKU().equals(skuToRemove)) {
                                if (item.getScanCount() > 1) {
                                    // Decrease the quantity if more than one
                                    item.setScanCount(item.getScanCount() - 1);
                                } else {
                                    // Remove item if scan count is 1
                                    scannedItems.remove(item);
                                }
                                itemRemoved = true;
                                break;
                            }
                        }
                        if (!itemRemoved) {
                            request.setAttribute("errorMessage", "Item with SKU '" + skuToRemove + "' not found in scanned items.");
                        }
                        session.removeAttribute("itemToRemoveSKU"); // Clean up session attribute
                    } else {
                        // Handle password verification failure or missing SKU
                        request.setAttribute("errorMessage", "Invalid manager password or unauthorized access.");
                    }
                    break;

                case "Complete-Sale":
                    BigDecimal totalAmount = BigDecimal.valueOf(calculateTotalPrice(scannedItems));

                    if (totalAmount.compareTo(BigDecimal.ZERO) <= 0) {
                        request.setAttribute("errorMessage", "Total amount cannot be zero or negative.");
                        break;
                    }

                    Sale newSale = new Sale();
                    newSale.setSales_date(new Timestamp(System.currentTimeMillis()));
                    newSale.setTotal_amount(totalAmount);
                    newSale.setPayment_method(request.getParameter("payment_method"));
                    if (loggedInUser != null) {
                        newSale.setEmployee_ID(loggedInUser.getEmployee_ID());
                        newSale.setStore_ID(loggedInUser.getStore_ID());
                    } else {
                        request.setAttribute("errorMessage", "Employee not logged in.");
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

                         // Call processSale method to update inventory and product quantities
                            inventoryService.processSale(newSalesID);
                        
                        String salespersonName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
                        String saleTime = newSale.getSales_date().toString();
                        String customerEmail = request.getParameter("customer_email");

                        emailService.sendSaleReceipt(customerEmail, salespersonName, saleTime, scannedItems, totalAmount, newSale.getPayment_method());
                        SmsSender.sendSms("+27631821265", saleTime);

                        scannedItems.clear();
                    } else {
                        request.setAttribute("errorMessage", "Failed to save sale.");
                    }
                    break;

                case "Inventory":
                    request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
                    break;

            }
            List<BigDecimal> totalPricePerItem = scannedItems.stream()
                    .map(item -> BigDecimal.valueOf(item.getProduct_price()).multiply(BigDecimal.valueOf(item.getScanCount())))
                    .collect(Collectors.toList());

            double totalPrice = calculateTotalPrice(scannedItems); // For display purposes (can be BigDecimal)
            request.setAttribute("scannedItems", scannedItems);
            request.setAttribute("totalPrice", totalPrice);
            request.setAttribute("totalPricePerItem", totalPricePerItem);

            // Forward to the same page to display updated information and any error messages
            request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
        } catch (Exception e) {
            Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Error in ProductServlet", e);
            request.setAttribute("errorMessage", "An unexpected error occurred.");
            request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
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
        try (Connection conn = dbConnect.connectToDB(); PreparedStatement stmt = conn.prepareStatement("SELECT employee_password FROM employees WHERE store_ID = ? AND role = 'Manager'")) {
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
