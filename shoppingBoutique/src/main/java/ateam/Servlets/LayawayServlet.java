
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.LayawayDaoImpl;
import ateam.Models.Email;
import ateam.Models.Employee;
import ateam.Models.Layaway;
import ateam.Service.EmailService;
import ateam.Service.LayawayService;
import ateam.ServiceImpl.EmailServiceImpl;
import ateam.ServiceImpl.LayawayServiceIMPL;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author carme
 */
@WebServlet(name = "LayawayServlet", urlPatterns = {"/LayawayServlet"})
public class LayawayServlet extends HttpServlet {
    
    private LayawayService layawayService =  new LayawayServiceIMPL(new LayawayDaoImpl(new Connect().connectToDB()));;
    private static final long serialVersionUID = 1L;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledExecutorService schedulerr;
    private final EmailService emailService;
    private String globalEmailAddress;
    private String globalCustomerName;
    
    @Override
     public void init() throws ServletException {
        super.init();
        schedulerr = Executors.newScheduledThreadPool(1);
    }
     
    @Override
     public void destroy() {
        super.destroy();
        schedulerr.shutdown();
    }
     
     public LayawayServlet() {
         this.emailService = new EmailServiceImpl();
     }


    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet LayawayServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet LayawayServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        
       String layawaySwitch = request.getParameter("layaway_switch");
       switch(layawaySwitch) {
           case "Add Layaway":
               handleNewLayawayAdding(request, response);
               sendFirstEmail(request, response);
               break;
           case "Search Layaway":
               handleSearchingLayawayByID(request, response);
               break;
           case "View all Layaways":
               handleViewAllLayaways(request, response);
               break;
           case "Update Layaway":
               handleUpdatingLayaways(request, response);
               break;
           case "Delete Layaway":
               handleDeletingLayaways(request, response);
               break;
       }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    private void handleAddingLayaway(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Employee employee = (Employee) session.getAttribute("Employee");
        
        Layaway layaway = new Layaway();
        
        // Parse buttonClickTime and expiryTime from request parameters
        String buttonClickTimeString = request.getParameter("buttonClickTime");
        String expiryTimeString = request.getParameter("expiryTime");
        
         // Parse Timestamps with correct format
        Timestamp buttonClickTime = Timestamp.valueOf(buttonClickTimeString.replace("T", " ").substring(0, 19));
        Timestamp expiryTime = Timestamp.valueOf(expiryTimeString.replace("T", " ").substring(0, 19));
    
        // Set other parameters in Layaway object
        layaway.setCustomerEmail(request.getParameter("customer_email"));
        //layaway.setCustomerNumber(request.getParameter("customer_number"));
        layaway.setEmployee_ID(employee.getEmployee_ID());
        layaway.setLayaway_status("PENDING");
        layaway.setStart_date(buttonClickTime);
        layaway.setExpiry_date(expiryTime);
        //layaway.setProductID(Integer.parseInt(request.getParameter("product_ID")));
        layaway.setProductQuantity(Integer.parseInt(request.getParameter("product_quantity")));
        layaway.setCustomerName(request.getParameter("customer_name"));
        globalEmailAddress = request.getParameter("customer_email");
        globalCustomerName = request.getParameter("customer_name");
        // Add layaway to database
        boolean success = layawayService.addLayaway(layaway);
        if (success) {
         request.setAttribute("message", "Layaway saved successfully");
 
            // Schedule the test method to run after 5 seconds
            schedulerr.schedule(() -> sendSecondEmail(), 5, TimeUnit.SECONDS);
        } else {
            request.setAttribute("message", "Failed to save Layaway");
    }
        request.getRequestDispatcher("LayawayDashboard.jsp").forward(request, response);
    }
    
    private void handleNewLayawayAdding(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Employee employee = (Employee) session.getAttribute("Employee");
        List<Layaway> scannedItems = (List<Layaway>) session.getAttribute("scannedItemsList");
        Layaway layaway = new Layaway();
        StringBuilder productIds = new StringBuilder();
        StringBuilder productName = new StringBuilder();
        
          // Parse buttonClickTime and expiryTime from request parameters
        String buttonClickTimeString = request.getParameter("buttonClickTime");
        String expiryTimeString = request.getParameter("expiryTime");
        
         // Parse Timestamps with correct format
        Timestamp buttonClickTime = Timestamp.valueOf(buttonClickTimeString.replace("T", " ").substring(0, 19));
        Timestamp expiryTime = Timestamp.valueOf(expiryTimeString.replace("T", " ").substring(0, 19));
        
        layaway.setCustomerEmail(request.getParameter("customerEmail"));
        //layaway.setCustomerNumber(request.getParameter("customer_number"));
        layaway.setEmployee_ID(employee.getEmployee_ID());
        layaway.setLayaway_status("PENDING");
        layaway.setStart_date(buttonClickTime);
        layaway.setExpiry_date(expiryTime);
        layaway.setCustomerName(request.getParameter("customerName"));
        globalEmailAddress = request.getParameter("customerEmail");
        globalCustomerName = request.getParameter("customerName");
        
        
        
        if (scannedItems != null) {
            for (Layaway item : scannedItems) {
//                //Output item details to console
//                System.out.println("Product SKU: " + item.getProductSKU());
//                System.out.println("Product Name: " + item.getProductName());
//                System.out.println("Product Price: " + item.getProductPrice());
//                System.out.println("------------------------------------");
                  //layaway.setProductID(item.getProductID());
                  layaway.setProductSKU(item.getProductSKU());
                  //layaway.setProductName(item.getProductName());
                  layaway.setProductPrice(item.getProductPrice());
                  
                  productName.append(item.getProductName()).append("+");
                  
                  // Append product ID to the StringBuilder with '-' separator
                  productIds.append(item.getProductID()).append("-");
                  String productIdsString = productIds.toString();
                  layaway.setProductID(productIds.toString());
                  System.out.println("APPENDED ID " + productIds);
                  System.out.println("APPENDED NAME " + productName);
                  String productNameString = productName.toString();
                  layaway.setProductName(productNameString);
                  System.out.println("TEST APPEND " + productNameString);
            }
        } else {
            // Output message if scannedItems is null or empty
            System.out.println("No scanned items found.");
        }

        // Set attribute to indicate showing popup
        //request.setAttribute("showPopup", true);
        
        boolean success = layawayService.addNewLayaway(layaway);
        if (success) {
         request.setAttribute("message", "Layaway saved successfully");
 
            // Schedule the test method to run after 5 seconds
            schedulerr.schedule(() -> sendSecondEmail(), 5, TimeUnit.SECONDS);
        } else {
            request.setAttribute("message", "Failed to save Layaway");
    }
        request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
    }
    
    private void handleSearchingLayawayByID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Layaway layaway = layawayService.getLayawayById(Integer.parseInt(request.getParameter("layaway_ID")));
        request.setAttribute("LayawaySearch", layaway);
        request.getRequestDispatcher("LayawayDashboard.jsp").forward(request, response);
    }
    
    private void handleViewAllLayaways(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Layaway> layaways = layawayService.getAllLayaways();
        request.setAttribute("Layaways", layaways);
        request.getRequestDispatcher("LayawayDashboard.jsp").forward(request, response);
    }
    
    private void handleUpdatingLayaways(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Layaway layaway = new Layaway();
        layaway.setLayaway_ID(Integer.parseInt(request.getParameter("new_layaway_ID")));
        //layaway.setProductID(Integer.parseInt(request.getParameter("new_product_ID")));
        layaway.setProductQuantity(Integer.parseInt(request.getParameter("new_product_quantity")));
        layaway.setLayaway_status(request.getParameter("new_layaway_status"));
        boolean success = layawayService.updateLayaway(layaway);
        if (success) {
         request.setAttribute("message", "Layaway Updated successfully");
 
           
        } else {
            request.setAttribute("message", "Failed to Update Layaway");
    }
        request.getRequestDispatcher("LayawayDashboard.jsp").forward(request, response);
    }
    
    private void handleDeletingLayaways(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean success = layawayService.deleteLayaway(Integer.parseInt(request.getParameter("drop_layaway_ID")));
        if (success) {
         request.setAttribute("message", "Layaway Deleted successfully");
 
            // Schedule the test method to run after 5 seconds
            //schedulerr.schedule(() -> sendFirstEmail(), 5, TimeUnit.SECONDS);
        } else {
            request.setAttribute("message", "Failed to Delete Layaway");
    }
        request.getRequestDispatcher("LayawayDashboard.jsp").forward(request, response);
    }
    
    private void sendFirstEmail(HttpServletRequest request, HttpServletResponse response) {
        //System.out.println("Hello World");
        String customerName = globalCustomerName;
        String customerEmail = globalEmailAddress;
        Layaway emailLayaway = layawayService.emailData(customerEmail);
        
        
        String msg = "Dear " + customerName + " "  + ",\nCongratulations! Your Layaway ID is " + emailLayaway.getLayaway_ID()
                        + ". We're excited to have your new Layaway. Please collect it before the " + emailLayaway.getExpiry_date() +" " +"Your layaway product is: " + emailLayaway.getProductName() +"\n\n\n\nBest regards,\n";
//                        + manager.getFirstName() + " " + manager.getLastName() + "\n"
//                        + manager.getRole() + "(" + stores.getStore_name() + ")\n"
//                        + "Carols Boutique\n"
//                        + stores.getStore_phone();

                Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
                emailDetails.setReceiver(customerEmail);
                emailDetails.setSubject("Hello From Carols Boutique! Your Layaway Request is Complete");
                emailDetails.setMessage(msg);

                emailService.sendMail(emailDetails);
    }
    
    private void sendSecondEmail() {
        String customerEmail = globalEmailAddress;
        String customerName = globalCustomerName;
        Layaway emailLayaway = layawayService.emailData(customerEmail);
        String msg = "Dear " + customerName + " "  + ",\nImportant! Your Layaway ID: " + emailLayaway.getLayaway_ID()
                        + ". This is a gentle reminder to collect your layaway. You have 12 hours left. Please collect it before the " + emailLayaway.getExpiry_date() + " " + "Your layaway product is: " + emailLayaway.getProductName() +"\n\n\n\nBest regards,\n";
//                        + manager.getFirstName() + " " + manager.getLastName() + "\n"
//                        + manager.getRole() + "(" + stores.getStore_name() + ")\n"
//                        + "Carols Boutique\n"
//                        + stores.getStore_phone();

                Email emailDetails = new Email("ramovhatp@gmail.com", "xaed clmt qpis ctvf");
                emailDetails.setReceiver(customerEmail);
                emailDetails.setSubject("Hello From Carols Boutique! Your Layaway Request is Going to expire!!!");
                emailDetails.setMessage(msg);

                emailService.sendMail(emailDetails);
    }
}
