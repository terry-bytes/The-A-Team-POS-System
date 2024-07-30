import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Employee;
import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.EmailService;
import ateam.Service.InventoryService;
import ateam.Service.ProductService;
import ateam.Service.ReturnService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.ReturnServiceImpl;
import ateam.ServiceImpl.TheReturn;
import ateam.Services.impl.InventoryServiceImpl;
import ateam.Servlets.ProductServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/ReturnedServlet")
public class ReturnedServlet extends HttpServlet {
    private final ReturnService returnService = new ReturnServiceImpl();
    private ProductService productService = new ProductServiceImpl();
    private SaleDAO saleDAO = new SaleDAOIMPL();
    private SalesItemDAO salesItemDAO = new SalesItemDAOIMPL();
    private EmailService emailService = new EmailServiceImpl();
    private Connect dbConnect = new Connect();
    private InventoryService inventoryService = new InventoryServiceImpl();
    private static final double VAT_RATE = 0.15;
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("submit");
        HttpSession session = request.getSession();

        try {
            switch (action) {
                
                case "Retrieve-Sale":
                    retrieveSale(request, response, session);
                    break;
                case "Process-Return":
                    processReturn(request, response, session);
                    break;
                case "Handle-Customer-Choice":
                    handleCustomerChoice(request, response, session);
                    break;
                default:
                    System.out.println("wrong selection");
                    break;
            }
        } catch (IOException e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            response.sendRedirect("returnSale.jsp");
        }
    }

    private void retrieveSale(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        TheReturn ret = new TheReturn();
        int salesId = Integer.parseInt(request.getParameter("sales_ID"));
        session.setAttribute("salesId", salesId);
        Sale sale = ret.getSaleById(salesId);
        List<SalesItem> salesItems = ret.getSalesItemsBySaleId(salesId);

        if (sale != null) {
            session.setAttribute("sale", sale);
            session.setAttribute("salesItems", salesItems);
        } else {
            session.setAttribute("message", "Sale not found for ID: " + salesId);
        }
        response.sendRedirect("returnSale.jsp");
    }

    private void processReturn(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
       int salesId = (int)request.getSession(false).getAttribute("salesId");
       int salesItemId = Integer.parseInt(request.getParameter("salesItemId"));
       TheReturn ret = new TheReturn();
    
    SalesItem sales =ret.getSalesItemById(salesItemId);
         System.out.println("output "+sales);   
         BigDecimal getPrice = sales.getUnit_price();
         
    int productId = sales.getProduct_ID();
    
    int quantity = Integer.parseInt(request.getParameter("quantity"));
    String email = request.getParameter("email");
    String reason = request.getParameter("return_reason");
    request.getSession(false).setAttribute("email", email);

    Return returns = new Return();
    returns.setSales_ID(salesId);
    returns.setProduct_ID(productId);
    returns.setQuantity(quantity);
    returns.setReturn_date(new Timestamp(System.currentTimeMillis())); // Ensure return_date is set
    returns.setEmail(email);
    returns.setReason(reason);

    if(!(quantity<sales.getQuantity())|| (sales.getQuantity()!=0)){
        ret.addReturn(returns);
        boolean decrease = ret.decreaseItems(quantity, salesItemId);
        
        ret.updateProductQuantity(productId, quantity);

        System.out.println("Added the return record to the database "+decrease);
        Sale ss = (Sale)request.getSession(false).getAttribute("sale");
        ss = ret.getSaleById(salesId);
        System.out.println("sale :"+ss);
        BigDecimal totalSale = ss.getTotal_amount();
        System.out.println("total :"+totalSale);

        BigDecimal remainingAmount = ret.updateSaleTotalAmount(salesId, getPrice, quantity,totalSale);
        System.out.println("Update total remaining "+remainingAmount);
        
         request.getSession(false);
       
        BigDecimal intAsBigDecimal = BigDecimal.valueOf(quantity);
        BigDecimal change = getPrice.multiply(intAsBigDecimal);
        System.out.println("The change is R"+change);
        session.setAttribute("change", change);
        
        
    }
    else{
        System.out.println("Error, Item already returned");
        request.setAttribute("message","Error, Item already returned");
           try {
               request.getRequestDispatcher("returnSale.jsp").forward(request, response);
           } catch (ServletException ex) {
               Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
           }
    }
        
        
        
      response.sendRedirect("returnSale.jsp");  
       
        
    }
    
    private void handleCustomerChoice(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        List<Product> scannedItems = (List<Product>)request.getSession(false).getAttribute("scannedItems");

          if (scannedItems == null) {
            scannedItems = new ArrayList<>();
            session.setAttribute("scannedItems", scannedItems);
        }  
            
            int salesId =(int)request.getSession(false).getAttribute("salesId");
            
            BigDecimal remainingAmount = (BigDecimal)request.getSession(false).getAttribute("change");
            session.setAttribute("remainingAmount", remainingAmount);
            
            String customerChoice = request.getParameter("customer_choice");
            if (customerChoice == null) {
            session.setAttribute("message", "Customer choice is required");
            response.sendRedirect("returnSale.jsp");
            return;
        }
            
            switch(customerChoice){
                case "Receive-Change":
                                    //Employee loggedInUser = (Employee) session.getAttribute("Employee");

//                                    String salespersonName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
//                                    String saleTime = ret.getSales_date().toString();
//                                    String customerEmail = (String) request.getSession(false).getAttribute("email");
//
//                                    emailService.sendSaleReceipt(customerEmail, salespersonName, saleTime, scannedItems, totalAmountWithoutVAT, vatAmount, change, newSale.getPayment_method());
//                                    //SmsSender.sendSms("+27631821265", saleTime);
                    request.getSession(false).setAttribute("message", "Give the customer R"+remainingAmount);
                     
                            List<SalesItem> returnedItems = (List<SalesItem>) session.getAttribute("salesItems");
                            session.setAttribute("message", "You returned item(s) successfully");

            {
                try {
                    // emailService.sendSaleReceipt((String) session.getAttribute("email"), returnedItems, remainingAmount);
                    
                    request.getRequestDispatcher("returnSale.jsp").forward(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                           
            {
                try {
                    request.getRequestDispatcher("returnSale.jsp").forward(request, response);
                } catch (ServletException ex) {
                    Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                    
                    break;


                
                case "Select-New-Item":
                    String cashPaidStr = request.getParameter("cash_amount");
                    Employee loggedInUser = (Employee) session.getAttribute("Employee");
                    String action = request.getParameter("submit");
                    switch(action){
                        case "Add-Item":

                case "auto-submit":
                    
                            String SKU = request.getParameter("productSKU");

                            List<Product> foundProducts = productService.getProductBySKU(SKU);

                            session.setAttribute("scannedItemsList", foundProducts);

                            if (foundProducts.isEmpty()) {
                                try {
                                    // Handle the case where no products are found
                                    request.setAttribute("errorMessage", "Product with SKU '" + SKU + "' not found.");
                                    request.getRequestDispatcher("returnSale.jsp").forward(request, response);
                                    return; // Return to avoid further processing
                                } catch (ServletException ex) {
                                    Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                            session.setAttribute("foundProducts", foundProducts);

                            // Assuming SKU format includes size and color
                            String[] skuParts = SKU.split("-");
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
                                request.getSession(false).setAttribute("ScannedItemsList", scannedItems);

                            }
                            
                                            response.sendRedirect("returnSale.jsp");

                            
                            break;
                            
                            case "Complete-Sale":
                            BigDecimal totalAmount = BigDecimal.valueOf(calculateTotalPrice(scannedItems));
                            BigDecimal vatAmount = totalAmount.multiply(BigDecimal.valueOf(VAT_RATE));
                            // The total amount with VAT is not needed in the final price; instead, show VAT separately
                            BigDecimal returnedAmount = (BigDecimal) request.getAttribute("change");
                            BigDecimal totalAmountWithoutVAT = totalAmount.subtract(returnedAmount);
                            BigDecimal change = BigDecimal.ZERO;

                            try {
                                cashPaidStr = cashPaidStr.trim().replace(",", "");
                                BigDecimal cashPaid = new BigDecimal(cashPaidStr);

                                // Calculate change to be returned
                                change = cashPaid.subtract(totalAmountWithoutVAT);
                                if (change.compareTo(BigDecimal.ZERO) < 0) {
                                    request.setAttribute("errorMessage", "Cash paid is less than the total amount.");
                                    break;
                                }

                                Sale newSale = new Sale();
                                newSale.setSales_date(new Timestamp(System.currentTimeMillis()));
                                newSale.setTotal_amount(totalAmountWithoutVAT);
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

                                    inventoryService.processSale(newSalesID);

                                    String salespersonName = loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
                                    String saleTime = newSale.getSales_date().toString();
                                    String customerEmail = (String) request.getSession(false).getAttribute("email");

                                    emailService.sendSaleReceipt(customerEmail, salespersonName, saleTime, scannedItems, totalAmountWithoutVAT, vatAmount, change, newSale.getPayment_method());
                                    //SmsSender.sendSms("+27631821265", saleTime);

                                    scannedItems.clear();

                                    request.setAttribute("totalAmount", totalAmountWithoutVAT);
                                    request.setAttribute("vatAmount", vatAmount);
                                    request.setAttribute("change", change);
                                    request.getRequestDispatcher("saleReceipt.jsp").forward(request, response);
                                    return;
                                } else {
                                    request.setAttribute("errorMessage", "Failed to save sale.");
                                }
                            } catch (NumberFormatException e) {
                                Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Invalid number format for cash paid: " + cashPaidStr, e);
                                request.setAttribute("errorMessage", "Invalid number format for cash paid.");
                            } catch (Exception e) {
                                Logger.getLogger(ProductServlet.class.getName()).log(Level.SEVERE, "Error processing sale: ", e);
                                request.setAttribute("errorMessage", "Error processing sale.");
                            }
                                            response.sendRedirect("returnSale.jsp");

                            break;

                    }//clossing inner Switch    
                    
                    break;
                
                
                default:
                    break;
             
            }
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