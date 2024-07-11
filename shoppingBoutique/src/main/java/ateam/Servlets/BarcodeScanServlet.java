package ateam.Servlets;

import ateam.DAO.ProductDAO;
import ateam.Models.Product;
import ateam.Models.BarcodeScanner;
import ateam.DAO.BarcodeScanCallback;
import ateam.DAOIMPL.ProductDAOIMPL;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "BarcodeScanServlet", urlPatterns = {"/BarcodeScanServlet"})
public class BarcodeScanServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private BarcodeScanner barcodeScanner;
    private ProductDAO productDAO;

    @Override
    public void init() {
        barcodeScanner = new BarcodeScanner(new BarcodeScanCallback() {
            public void onBarcodeScanned(String barcodeData) {
                // Handle scanned barcode data here (e.g., store in database, process, etc.)
                System.out.println("Barcode scanned in servlet: " + barcodeData);
                
                // Fetch product details from database
                Product product = productDAO.getProductBySKU(barcodeData);
                if (product != null) {
                    // Product found, process further (e.g., add to sale)
                    // Optionally, forward to a JSP or send a response back to the client
                    System.out.println("Product found: " + product.getProduct_name());
                    System.out.println("Product Price: " + product.getProduct_price());
                    System.out.println("Product Quantity in stock: " + product.getQuantity_in_stock());
                    
                } else {
                    System.out.println("Product not found for barcode: " + barcodeData);
                }
            }
        });
        
        productDAO = new ProductDAOIMPL(); // Initialize your ProductDAO implementation here
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Start barcode scanning when servlet is initialized
        barcodeScanner.startScanning();
        response.getWriter().write("Barcode scanning started.");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Stop barcode scanning when POST request is received
        barcodeScanner.stopScanning();
        response.getWriter().write("Barcode scanning stopped.");
    }

    @Override
    public void destroy() {
        // Stop barcode scanning when servlet is destroyed (e.g., application shutdown)
        barcodeScanner.stopScanning();
    }

    @Override
    public String getServletInfo() {
        return "Barcode Scan Servlet";
    }
}
