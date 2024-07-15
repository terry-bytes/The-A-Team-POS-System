package ateam.Servlets;
import ateam.DAO.ProductDAO;
import ateam.DAOIMPL.ProductDAOIMPL;
import ateam.Models.Product;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
    
    private ProductDAO productDAO = new ProductDAOIMPL();

    private final ProductService productService = new ProductServiceImpl(productDAO);
    private Map<String, Integer> scanCounts = new HashMap<>();
    
   

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String submit = request.getParameter("submit");
        String sku = request.getParameter("input-field");
        List<Product> getItem = new ArrayList();
        
        switch(submit){
            case "Add-Item":
            case "auto-submit":
                getItem = productService.getProductBySKU(sku);
                 System.out.println(getItem.size());
                if (!getItem.isEmpty()) {
                    Product product = getItem.get(0);
                    scanCounts.put(sku, scanCounts.getOrDefault(sku, 0) + 1);
                    product.setScanCount(scanCounts.get(sku));
                }
                HttpSession session = request.getSession(true);
                
                request.setAttribute("getItem", getItem);
                session.setAttribute("getItem", getItem);
                request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
                break;
            default:
                System.out.println("no input");
                
        }
        
        
        
          
    }
    
    
}


