/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.IBTDAOIMPL;
import ateam.Models.IBT;
import ateam.Models.SmsSender;
import ateam.Models.Store;
import ateam.Service.IBTService;
import ateam.ServiceImpl.IBTServiceIMPL;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ateam.Models.SmsSender;
import static ateam.Models.SmsSender.sendSms;

/**
 *
 * @author carme
 */
@WebServlet(name = "IBTServlet", urlPatterns = {"/IBTServlet"})
public class IBTServlet extends HttpServlet {
    
    private IBTService ibtService =  new IBTServiceIMPL(new IBTDAOIMPL(new Connect().connectToDB()));;
    

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
            out.println("<title>Servlet IBTServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet IBTServlet at " + request.getContextPath() + "</h1>");
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
       String action = request.getParameter("action");

        if ("handleIBTNotification".equals(action)) {
            handleIBTNotification(request, response);
                    
        } else {
            switch (request.getParameter("IBT_switch")) {
                case "Check Stores":
                    handleIBTStores(request, response);
                    break;

                case "Request IBT":
                    handleIBTRequest(request, response);
                    break;

                case "Approve":
                    handleDeletingIBTRequest(request, response);
                    break;

                case "Send IBT":
                    request.getRequestDispatcher("IBTSentDashboard.jsp").forward(request, response);
                    break;

                case "IBT-Requests":
                    handleReceivingIBTRequest(request, response);
                    break;

                case "Manage IBT's":
                     // Set session attribute to indicate there are pending IBT requests
                    request.getSession().setAttribute("ibtNotifications", true);
                    // Forward to the management page
                    request.getRequestDispatcher("IBTMainDashboard.jsp").forward(request, response);
                    break;
                   
                case "Send SMS":
                    handleRetrievingCustomerNumber(request, response);
                    break;
                    
                case"Validate Store":
                    handleRetrievingStoreID(request, response);
                    break;
                    
                case "Decline":
                    handleDeletingIBT(request, response);
                    break;
            }
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

    private void handleIBTStores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int product_id = (Integer.parseInt(request.getParameter("e_product_ID")));
        request.setAttribute("product_id", product_id);
        List<IBT> Stores = ibtService.getAllProducts(product_id);
        request.setAttribute("Stores", Stores);
        request.getRequestDispatcher("IBTSentDashboard.jsp").forward(request, response);
    }
    
    private void handleIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int product_id = (Integer.parseInt(request.getParameter("product_id")));
        System.out.println("AND CHECKER " + product_id);
        int product_quantity = (Integer.parseInt(request.getParameter("e_product_qautity")));
        int store_id = (Integer.parseInt(request.getParameter("store_id")));
        HttpSession session = request.getSession(false);
        Store sent_store_name = (Store) session.getAttribute("store");
        String store_name = sent_store_name.getStore_name();
        String customerName = request.getParameter("e_customer_name");
        String customerNumber = request.getParameter("e_customer_number");
        String customerEmail = request.getParameter("e_customer_email");
        boolean success = ibtService.sendIBTRequest(product_id, store_id,store_name, product_quantity, customerName, customerNumber, customerEmail, store_id);
        if (success) {
        request.setAttribute("message", "IBT sent successfully");
    } else {
        request.setAttribute("message", "Failed to send IBT");
    }
        request.getRequestDispatcher("IBTSentDashboard.jsp").forward(request, response);
    }
    
   
    private void handleDeletingIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
       HttpSession session = request.getSession(false);
        Store store_ID = (Store) session.getAttribute("store");
        int store_id = store_ID.getStore_ID();
         boolean success = ibtService.ApproveRequestedIBT(store_id);
          if (success) {
        request.setAttribute("message", "IBT Approved successfully");
    } else {
        request.setAttribute("message", "Failed to Approve IBT");
    }
          request.getRequestDispatcher("IBTReceiveDashboard.jsp").forward(request, response);
    }
    
    private void handleIBTNotification(HttpServletRequest request, HttpServletResponse response) {
//   HttpSession session = request.getSession(false);
//        Store sent_store_name = (Store) session.getAttribute("store");
//        int store_id = sent_store_name.getStore_ID();
    boolean success = false;
    HttpSession session = request.getSession(false);
    int store_id = 0;
    if (session != null) {
    Store sent_store_name = (Store) session.getAttribute("store");
    if (sent_store_name != null) {
        store_id = sent_store_name.getStore_ID();
        System.out.println("Store ID - Notification " + store_id );
        // Proceed with using store_id
    } else {
        // Handle case where sent_store_name is null
        // Maybe redirect or show an error message
    }
    } else {
    // Handle case where session is null
    // This should typically not happen unless session management is disabled
    }
         success = ibtService.checkForIBTNotification(store_id);
        System.out.println("Boolean result - Notification " + success);
        
        // Respond with success status (true or false)
        try (PrintWriter out = response.getWriter()) {
            out.print(success);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void handleReceivingIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Store store_ID = (Store) session.getAttribute("store");
        int store_id = store_ID.getStore_ID();
        List<IBT> Stores = ibtService.receiveIBTRequest(store_id);
        request.setAttribute("Stores", Stores);
        request.getRequestDispatcher("IBTReceiveDashboard.jsp").forward(request, response);
    }
    
    private void handleRetrievingCustomerNumber(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int IBTRequestID = Integer.parseInt(request.getParameter("ibt-id"));
        String customerNumber = ibtService.retrieveCustomerNumber(IBTRequestID); //USE THIS VARIABLE FOR SENDING IBT SMS
        request.getRequestDispatcher("IBTMainDashboard.jsp").forward(request, response);
        System.out.println("IBT ID BEING PRINTED OUT " + IBTRequestID);
        String message = "Your product is ready for Collection. Your IBT ID number is: " + IBTRequestID + "Please keep this IBT number safe";
        SmsSender.sendSms("+27631821265", message);

        System.out.println("CUSTOMER NUMBER " + customerNumber);
    }
    
    private void handleRetrievingStoreID(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int IBTRequestID = Integer.parseInt(request.getParameter("ibtNumber"));
        int retrievedStoreID = ibtService.retrieveStoreID(IBTRequestID);
        request.setAttribute("retrievedStoreID", retrievedStoreID);
        request.getRequestDispatcher("tellerDashboard.jsp").forward(request, response);
    }
    
    private void handleDeletingIBT(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Store store_ID = (Store) session.getAttribute("store");
        int store_id = store_ID.getStore_ID();
        boolean success = ibtService.declineIBTRequest(store_id);
        if (success) {
        request.setAttribute("message", "IBT Declined successfully");
    } else {
        request.setAttribute("message", "Failed to Decline IBT");
    }
          request.getRequestDispatcher("IBTReceiveDashboard.jsp").forward(request, response);
    }
}
