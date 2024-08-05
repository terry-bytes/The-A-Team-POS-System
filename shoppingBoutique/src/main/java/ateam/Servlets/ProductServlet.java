package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAO.InventoryDAO;
import ateam.DAO.ProductDAO;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.InventoryDAOIMPL;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Inventory;
import ateam.Models.Layaway;
import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Models.SmsSender;

import ateam.Service.InventoryService;

import ateam.Service.EmailService;

import ateam.Service.ProductService;
import ateam.Service.ReturnService;
import ateam.Service.SaleService2;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.ReturnServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.InventoryServiceImpl;
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

    private SaleService2 saleService = new SaleServiceImpl();

    private InventoryDAO inventoryDAO = new InventoryDAOIMPL();

    private static final double VAT_RATE = 0.15;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        BigDecimal voucherAmount = BigDecimal.ZERO;
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
        String cashPaidStr = request.getParameter("cash_amount");
        Sale newSale = new Sale();

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
                    BigDecimal vatAmount = totalAmount.multiply(BigDecimal.valueOf(VAT_RATE));
                    BigDecimal totalAmountWithoutVAT = totalAmount;
                    BigDecimal change = BigDecimal.ZERO;
                    BigDecimal cashPaid = BigDecimal.ZERO;
                    BigDecimal cardPaid = BigDecimal.ZERO;

                    try {
                        // Check if there are scanned items
                        if (scannedItems == null || scannedItems.isEmpty()) {
                            request.setAttribute("errorMessage", "No items have been scanned for the sale.");
                            break;
                        }

                        String paymentMethod = request.getParameter("payment_method");

                        // Handle cash payment
                        if ("cash".equals(paymentMethod)) {
                             cashPaidStr = request.getParameter("cash_amount");
                            if (cashPaidStr != null && !cashPaidStr.trim().isEmpty()) {
                                cashPaidStr = cashPaidStr.trim().replace(",", "");
                                cashPaid = new BigDecimal(cashPaidStr);
                                if (cashPaid.compareTo(totalAmountWithoutVAT) < 0) {
                                    request.setAttribute("errorMessage", "Cash paid is less than the total amount.");
                                    break;
                                }
                                change = cashPaid.subtract(totalAmountWithoutVAT);
                            } else {
                                request.setAttribute("errorMessage", "Cash amount is required.");
                                break;
                            }

                            // Handle card payment
                        } else if ("card".equals(paymentMethod)) {
                            cardPaid = totalAmountWithoutVAT; // Assuming the full amount is paid by card

                            // Handle card and cash payment
                        } else if ("cardAndcash".equals(paymentMethod)) {
                            String cashPaidStr2 = request.getParameter("cash_amount2");
                            String cardPaidStr2 = request.getParameter("card_amount2");

                            if (cashPaidStr2 != null && !cashPaidStr2.trim().isEmpty()) {
                                cashPaidStr2 = cashPaidStr2.trim().replace(",", "");
                                cashPaid = new BigDecimal(cashPaidStr2);
                            } else {
                                request.setAttribute("errorMessage", "Cash amount is required.");
                                break;
                            }

                            if (cardPaidStr2 != null && !cardPaidStr2.trim().isEmpty()) {
                                cardPaidStr2 = cardPaidStr2.trim().replace(",", "");
                                cardPaid = new BigDecimal(cardPaidStr2);
                            } else {
                                request.setAttribute("errorMessage", "Card amount is required.");
                                break;
                            }

                            BigDecimal totalPaid = cashPaid.add(cardPaid);
                            if (totalPaid.compareTo(totalAmountWithoutVAT) < 0) {
                                request.setAttribute("errorMessage", "Total amount is not covered by cash and card payments.");
                                break;
                            }

                            change = totalPaid.subtract(totalAmountWithoutVAT);

                            // Handle voucher payment
                        } else if ("voucher".equals(paymentMethod)) {
                            String voucherCode = request.getParameter("voucher_code");
                            request.getSession(false).setAttribute("voucherCode", voucherCode);
                            voucherAmount = saleService.validateVoucher(voucherCode);
                            if (voucherAmount.compareTo(BigDecimal.ZERO) <= 0) {
                                request.setAttribute("errorMessage", "Invalid or expired voucher code.");
                                break;
                            }

                            BigDecimal remainingAmount = totalAmountWithoutVAT.subtract(voucherAmount);
                            if (remainingAmount.compareTo(BigDecimal.ZERO) < 0) {
                                change = voucherAmount.subtract(totalAmountWithoutVAT);
                                voucherAmount = totalAmountWithoutVAT;

                            } else {
                                totalAmountWithoutVAT = remainingAmount;
                            }
                        }

                        // Save sale details
                        newSale.setSales_date(new Timestamp(System.currentTimeMillis()));
                        newSale.setTotal_amount(totalAmountWithoutVAT);
                        newSale.setPayment_method(paymentMethod);

                        if (newSale.getPayment_method().equals("voucher")) {
                            // Mark the voucher as used
                            String voucher = (String) request.getSession(false).getAttribute("voucherCode");
                            saleService.markVoucherAsUsed(voucher);
                        }

                        // Check if employee is logged in
                        if (loggedInUser != null) {
                            newSale.setEmployee_ID(loggedInUser.getEmployee_ID());
                            newSale.setStore_ID(loggedInUser.getStore_ID());
                        } else {
                            request.setAttribute("errorMessage", "Employee not logged in.");
                            break;
                        }

                        int newSalesID = saleDAO.saveSale(newSale);
                        if (newSalesID != -1) {
                            // Save sales items
                            for (Product item : scannedItems) {
                                SalesItem salesItem = new SalesItem();
                                salesItem.setSales_ID(newSalesID);
                                salesItem.setProduct_ID(item.getProduct_ID());
                                salesItem.setQuantity(item.getScanCount());
                                salesItem.setUnit_price(BigDecimal.valueOf(item.getProduct_price()));

                                salesItemDAO.saveSalesItem(salesItem);
                            }

                            // Process inventory and notifications
                            inventoryService.processSale(newSalesID);
                            List<Inventory> reorderList = inventoryDAO.checkAndSendReorderNotification(loggedInUser.getStore_ID());

                            // Send receipt via email and SMS
                            String salespersonName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
                            String saleTime = newSale.getSales_date().toString();
                            String customerEmail = request.getParameter("customer_email");
                            request.setAttribute("saleID", newSalesID);

                            emailService.sendSaleReceipt(customerEmail, salespersonName, saleTime, scannedItems, totalAmountWithoutVAT, vatAmount, change, newSale.getPayment_method(), cashPaid, cardPaid, newSalesID);
                            SmsSender.sendSms("+27631821265", "Thank you for SHOPPING with us! 😊 Please check your email (" + customerEmail + ") for your RECEIPT.");

                            // Clear scanned items and set attributes for receipt
                            scannedItems.clear();
                            request.setAttribute("totalAmount", totalAmountWithoutVAT);
                            request.setAttribute("vatAmount", vatAmount);
                            request.setAttribute("change", change);
                            request.setAttribute("cashPaid", cashPaid);
                            request.setAttribute("cardPaid", cardPaid);
                            request.setAttribute("scannedItems", scannedItems);
                            request.getRequestDispatcher("saleReceipt.jsp").forward(request, response);
                            return;
                        } else {
                            request.setAttribute("errorMessage", "Failed to save sale.");
                        }
                    } catch (NumberFormatException e) {
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Invalid number format for payment amounts", e);
                        request.setAttribute("errorMessage", "Invalid number format for payment amounts.");
                    } catch (Exception e) {
                        Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Error processing sale: ", e);
                        request.setAttribute("errorMessage", "Error processing sale.");
                    }
                    break;

                case "Inventory":
                    request.getRequestDispatcher("replenishStock.jsp").forward(request, response);
                    break;
                case "return":

                    request.getRequestDispatcher("returnSale.jsp").forward(request, response);

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