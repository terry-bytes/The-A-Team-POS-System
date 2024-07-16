/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.IBTDAOIMPL;
import ateam.Models.IBT;
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
        switch(request.getParameter("IBT_switch")) {
            case "check_stores":
                handleIBTStores(request, response);
                break;
                
            case "Request IBT":
                handleIBTRequest(request, response);
                break;
                
            case "Check_IBT":
                handleReceivingIBTRequest(request, response);
                break;
            
            case "Approve":
                handleDeletingIBTRequest(request, response);
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

    private void handleIBTStores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        int product_id = (Integer.parseInt(request.getParameter("e_product_ID")));
        List<IBT> Stores = ibtService.getAllProducts(product_id);
        request.setAttribute("Stores", Stores);
        request.getRequestDispatcher("IBTSentDashboard.jsp").forward(request, response);
    }
    
    private void handleIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int product_id = (Integer.parseInt(request.getParameter("product_id")));
        int store_id = (Integer.parseInt(request.getParameter("store_id")));
        HttpSession session = request.getSession(false);
        Store sent_store_name = (Store) session.getAttribute("store");
        String store_name = sent_store_name.getStore_name();
        boolean success = ibtService.sendIBTRequest(product_id, store_id,store_name);
        if (success) {
        request.setAttribute("message", "IBT sent successfully");
    } else {
        request.setAttribute("message", "Failed to send IBT");
    }
        request.getRequestDispatcher("IBTSentDashboard.jsp").forward(request, response);
    }
    
    private void handleReceivingIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int store_id = 1;
        List<IBT> Stores = ibtService.receiveIBTRequest(store_id);
        request.setAttribute("Stores", Stores);
        request.getRequestDispatcher("IBTReceiveDashboard.jsp").forward(request, response);
    }
    
    private void handleDeletingIBTRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
         int store_id = 4;
         boolean success = ibtService.deleteRequestedIBT(store_id);
          if (success) {
        request.setAttribute("message", "IBT Approved successfully");
    } else {
        request.setAttribute("message", "Failed to Approve IBT");
    }
          request.getRequestDispatcher("IBTReceiveDashboard.jsp").forward(request, response);
    }
}
