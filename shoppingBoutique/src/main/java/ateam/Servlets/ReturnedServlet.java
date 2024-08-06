import ateam.BDconnection.Connect;
import ateam.DAO.SaleDAO;
import ateam.DAO.SalesItemDAO;
import ateam.DAOIMPL.SaleDAOIMPL;
import ateam.DAOIMPL.SalesItemDAOIMPL;
import ateam.Models.Product;
import ateam.Models.Return;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Models.Voucher;
import ateam.Service.EmailService;
import ateam.Service.InventoryService;
import ateam.Service.ProductService;
import ateam.Service.ReturnService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.ProductServiceImpl;
import ateam.ServiceImpl.ReturnServiceImpl;
import ateam.ServiceImpl.TheReturn;
import ateam.ServiceImpl.InventoryServiceImpl;
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
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;
import java.util.*;

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
        BigDecimal change = (BigDecimal) request.getSession(false).getAttribute("change");

        switch (action) {
    case "Retrieve-Sale":
        retrieveSale(request, response, session);
        break;
        
    case "OK":
        try {
            request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;
    case "Process-Return":
        processReturn(request, response, session);
        break;
    case "Complete Return":
        request.setAttribute("message", "Refund the customer R" + change);
        try {
            request.getRequestDispatcher("returnSale.jsp").forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;
    case "Select New Item":
        TheReturn th = new TheReturn();
        EmailService emailServ = new EmailServiceImpl();
        StringBuilder vr= new StringBuilder();
       
        
        Random random = new Random();
        String voucher ;
        String email = (String) request.getSession(false).getAttribute("emails");

        for (int i = 0; i < 10; i++) {
            int digit = random.nextInt(10); // Generates a random number between 0 and 9
            vr.append(digit);
        }
        
        voucher = vr.toString();
        System.out.println(voucher);
        Voucher date = new Voucher();
        date.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        request.getSession(false).setAttribute("voucher", voucher);
        th.addVoucher(voucher, change);
        emailServ.VoucherEmail(email, change, voucher);

        try {
            request.getRequestDispatcher("voucher.jsp").forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(ReturnedServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        break;
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
            session.setAttribute("message", "Sale has been successfully retrieved");
        } else {
            session.setAttribute("errorMessage", "Sale not found for ID: " + salesId);
        }
        response.sendRedirect("returnSale.jsp");
    }

    private void processReturn(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException, ServletException {
        int salesId = (int) request.getSession(false).getAttribute("salesId");
        int salesItemId = Integer.parseInt(request.getParameter("salesItemId"));
        TheReturn ret = new TheReturn();

        SalesItem sales = ret.getSalesItemById(salesItemId);
        BigDecimal getPrice = sales.getUnit_price();
        List<Product> availableProducts = ret.getProductsByPrice(getPrice);
        request.getSession(false).setAttribute("availableProducts", availableProducts);

        int productId = sales.getProduct_ID();
        request.getSession(false).setAttribute("productId", productId);

        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String email = request.getParameter("email");
        String reason = request.getParameter("return_reason");
        request.getSession(false).setAttribute("emails", email);

        Return returns = new Return();
        returns.setSales_ID(salesId);
        System.out.println(salesId);
        returns.setProduct_ID(productId);
        returns.setQuantity(quantity);
        returns.setReturn_date(new Timestamp(System.currentTimeMillis())); // Ensure return_date is set
        returns.setEmail(email);
        returns.setReason(reason);

        if (quantity>sales.getQuantity() || sales.getQuantity() == 0) {
            
            request.setAttribute("errorMessage", "Error, Item already returned");
            request.getRequestDispatcher("returnSale.jsp").forward(request, response);
            
            
            
        } else {
            ret.addReturn(returns);
            boolean decrease = ret.decreaseItems(quantity, salesItemId);
            ret.updateProductQuantity(productId, quantity);

            Sale ss = (Sale) request.getSession(false).getAttribute("sale");
            ss = ret.getSaleById(salesId);
            BigDecimal totalSale = ss.getTotal_amount();

            BigDecimal remainingAmount = ret.updateSaleTotalAmount(salesId, getPrice, quantity, totalSale);
            request.getSession(false).setAttribute("remainingAmount", remainingAmount);

            BigDecimal intAsBigDecimal = BigDecimal.valueOf(quantity);
            BigDecimal change = getPrice.multiply(intAsBigDecimal);
            session.setAttribute("change", change);
            session.setAttribute("message", "Item has been successfully returned");
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
