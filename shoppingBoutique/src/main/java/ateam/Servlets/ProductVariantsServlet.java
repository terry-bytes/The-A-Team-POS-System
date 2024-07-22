package ateam.Servlets;

import ateam.Models.ProductVariants;
import ateam.Service.ProductVariantsService;
import ateam.ServiceIMPL.ProductVariantsServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductVariantsServlet", urlPatterns = "/productVariants")
public class ProductVariantsServlet extends HttpServlet {

    private ProductVariantsService productVariantsService;

    @Override
    public void init() throws ServletException {
        super.init();
        try {
            // Initialize the service here
            productVariantsService = new ProductVariantsServiceImpl();
        } catch (Exception e) {
            throw new ServletException("Failed to initialize ProductVariantsServiceImpl", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("search".equals(action)) {
            searchProductVariants(request, response);
        } else {
            listProductVariants(request, response);
        }
    }

    private void searchProductVariants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productSKU = request.getParameter("productSKU");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        int storeID = Integer.parseInt(request.getParameter("storeID"));

        List<ProductVariants> variants;
        if (color != null && !color.isEmpty()) {
            variants = productVariantsService.searchProductVariants(productSKU, size, color, storeID);
        } else {
            variants = productVariantsService.searchProductVariants(productSKU, size, storeID);
        }

        HttpSession session = request.getSession();
        session.setAttribute("variants", variants);
        request.getRequestDispatcher("/listProductVariants.jsp").forward(request, response);
    }

    private void listProductVariants(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // If no search is specified, show an empty list or some default message
        request.getRequestDispatcher("/listProductVariants.jsp").forward(request, response);
    }
}
