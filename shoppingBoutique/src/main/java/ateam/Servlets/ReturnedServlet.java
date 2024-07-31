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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

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
        int salesId = (int) request.getSession(false).getAttribute("salesId");
        int salesItemId = Integer.parseInt(request.getParameter("salesItemId"));
        TheReturn ret = new TheReturn();
        String managerPassword = request.getParameter("manager_password");
        SalesItem sales = ret.getSalesItemById(salesItemId);
        System.out.println("output " + sales);
        BigDecimal getPrice = sales.getUnit_price();
        System.out.println("getPrice "+getPrice);
        List<Product> availableProducts =ret.getProductsByPrice(getPrice);
        request.getSession(false).setAttribute("availableProducts",availableProducts);
            
        
        int productId = sales.getProduct_ID();
        request.getSession(false).setAttribute("productId", productId);

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
        Employee loggedInUser = (Employee) session.getAttribute("Employee");
        if (!(quantity < sales.getQuantity()) || (sales.getQuantity() != 0)) {
//            
            if ( verifyManagerPassword(loggedInUser.getStore_ID(), managerPassword)) {

                ret.addReturn(returns);
                boolean decrease = ret.decreaseItems(quantity, salesItemId);

                ret.updateProductQuantity(productId, quantity);

                System.out.println("Added the return record to the database " + decrease);
                Sale ss = (Sale) request.getSession(false).getAttribute("sale");
                ss = ret.getSaleById(salesId);
                System.out.println("sale :" + ss);
                BigDecimal totalSale = ss.getTotal_amount();
                System.out.println("total :" + totalSale);

                BigDecimal remainingAmount = ret.updateSaleTotalAmount(salesId, getPrice, quantity, totalSale);
                System.out.println("Update total remaining " + remainingAmount);
                
                request.getSession(false).setAttribute("remainingAmount", remainingAmount);

                BigDecimal intAsBigDecimal = BigDecimal.valueOf(quantity);
                BigDecimal change = getPrice.multiply(intAsBigDecimal);
                System.out.println("The change is R" + change);
                session.setAttribute("change", change);

            }

        } else {
            System.out.println("Error, Item already returned");
            request.setAttribute("message", "Error, Item already returned");
            try {
                request.getRequestDispatcher("returnSale.jsp").forward(request, response);
            } catch (ServletException ex) {
                Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        response.sendRedirect("returnSale.jsp");

    }

    private void handleCustomerChoice(HttpServletRequest request, HttpServletResponse response, HttpSession session)throws IOException{
        int salesId = (int) request.getSession(false).getAttribute("salesId");
        int productId = Integer.parseInt(request.getParameter("selectedProduct"));
        BigDecimal change = (BigDecimal)request.getSession(false).getAttribute("change");
        String customerChoice = request.getParameter("customer_choice");
        TheReturn ret = new TheReturn();
        if(ret.handleCustomerOptions(salesId,change, customerChoice,productId)){
        
        request.setAttribute("message","Return has been successfully completed");
        response.sendRedirect("returnSale.jsp");
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

        
    
   

    }