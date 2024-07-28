import ateam.Models.InventorySummary;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/storeInventorySummary")
public class StoreInventorySummaryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private final InventorySummaryService inventorySummaryService;

    public StoreInventorySummaryServlet() {
        this.inventorySummaryService = new InventorySummaryService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String colorFilter = request.getParameter("color");
        String sizeFilter = request.getParameter("size");
        String storeFilter = request.getParameter("store");
        String skuFilter = request.getParameter("sku");

        List<InventorySummary> inventorySummaries = inventorySummaryService.getInventorySummaries(colorFilter, sizeFilter, storeFilter, skuFilter);
        request.setAttribute("inventorySummaries", inventorySummaries);
        request.getRequestDispatcher("Search.jsp").forward(request, response);
    }
}
