import ateam.Models.Product;
import ateam.Models.Sale;
import ateam.Models.SalesItem;
import ateam.Service.ReturnService;
import ateam.ServiceImpl.ReturnServiceImpl;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/ReturnServlet")
public class ReturnServlet extends HttpServlet {
    private final ReturnService returnService = new ReturnServiceImpl();

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
                case "Confirm-Product-Selection":
                    confirmProductSelection(request, response, session);
                    break;
                default:
                    session.setAttribute("message", "Invalid action.");
                    response.sendRedirect("returnSale.jsp");
                    break;
            }
        } catch (IOException e) {
            session.setAttribute("message", "Error: " + e.getMessage());
            response.sendRedirect("returnSale.jsp");
        }
    }

    private void retrieveSale(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int salesId = Integer.parseInt(request.getParameter("sales_ID"));
        Sale sale = returnService.getSaleById(salesId);
        List<SalesItem> salesItems = returnService.getSalesItemsBySaleId(salesId);

        if (sale != null) {
            session.setAttribute("sale", sale);
            session.setAttribute("salesItems", salesItems);
        } else {
            session.setAttribute("message", "Sale not found for ID: " + salesId);
        }
        response.sendRedirect("returnSale.jsp");
    }

    private void processReturn(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int salesId = Integer.parseInt(request.getParameter("sales_ID"));
        int salesItemId = Integer.parseInt(request.getParameter("salesItemId"));
        SalesItem sales = returnService.getSalesItemById(salesItemId);
        int productId = sales.getProduct_ID();
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String email = request.getParameter("email");
        String reason = request.getParameter("return_reason");

        BigDecimal remainingAmount = returnService.processReturn(salesId, productId, quantity, email, reason);
        session.setAttribute("remainingAmount", remainingAmount);
        session.setAttribute("sales_ID", salesId);
        response.sendRedirect("returnSale.jsp");
    }

    private void handleCustomerChoice(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int salesId = Integer.parseInt(request.getParameter("sales_ID"));
        BigDecimal remainingAmount = new BigDecimal(request.getParameter("remainingAmount"));
        String customerChoice = request.getParameter("customerChoice");

        if ("Select-New-Item".equals(customerChoice)) {
            List<Product> availableProducts = returnService.getProductsByPrice(remainingAmount);
            session.setAttribute("availableProducts", availableProducts);
        } else if ("Receive-Change".equals(customerChoice)) {
            returnService.handleCustomerOptions(salesId, remainingAmount, "Receive-Change");
            
            session.setAttribute("message", "Change provided: " + remainingAmount);
        }
        response.sendRedirect("returnSale.jsp");
    }

    private void confirmProductSelection(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws IOException {
        int salesId = Integer.parseInt(request.getParameter("sales_ID"));
        int selectedProductId = Integer.parseInt(request.getParameter("selectedProduct"));
        BigDecimal remainingAmount = new BigDecimal(request.getParameter("remainingAmount"));

        returnService.handleCustomerOptions(salesId, remainingAmount, "Select-New-Item", selectedProductId);
        session.setAttribute("message", "Product selected and added to the sale.");
        response.sendRedirect("returnSale.jsp");
    }
    
  
}
