/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.EmployeeDAOIMPL;
import ateam.DTO.SalesDTO;
import ateam.Models.Employee;
import ateam.Models.SalesItem;
import ateam.Models.Store;
import ateam.Service.EmployeeService;
import ateam.Service.ReturnService;
import ateam.Service.SaleService2;
import ateam.Service.StoreService;
import ateam.ServiceImpl.EmployeeServiceImpl;
import ateam.ServiceImpl.ReturnServiceImpl;
import ateam.ServiceImpl.SaleServiceImpl;
import ateam.ServiceImpl.StoreServiceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carme
 */
@WebServlet(name = "AdminServlet", urlPatterns = {"/AdminServlet"})
public class AdminServlet extends HttpServlet {
    
    private EmployeeService employeeService = new EmployeeServiceImpl(new EmployeeDAOIMPL());
    private final StoreService storeService = new StoreServiceImpl();
    private final SaleService2 saleService = new SaleServiceImpl();
    private final ReturnService returnService = new ReturnServiceImpl();

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
        List<Employee> employeeList = employeeService.getAllEmployees();
        List<Store> stores = storeService.getAllStores();
        
        request.setAttribute("employeeList", employeeList);
        request.setAttribute("storeList", stores);
        request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
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
        String adminSwitch = request.getParameter("admin_switch");
        switch(adminSwitch) {
            case "View":
                handleViewEmployees(request, response);
                break;
                
            case "Add Manager":
                handleAddManager(request, response);
                break;
             
            case "View Report":
                handleViewReports(request, response);
                break;
                
            case "Add Store":
                handleAddStore(request, response);
                break;
            case "getStoreDashboard":
                int storeId = Integer.parseInt(request.getParameter("storeId"));
                List<SalesDTO> mySales = saleService.getStoreSales(storeId);
                System.out.println("My $ Store Sales: "+ mySales.size());
                
                request.getSession(false).setAttribute("myStoreSales", mySales);
                request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
                break;
            case "viewSaleItems":
                int saleId = Integer.parseInt(request.getParameter("saleId"));
                List<SalesItem> sales = returnService.getSalesItemsBySaleId(saleId);
                
                request.getSession(false).setAttribute("SalesItems", sales);
                request.getRequestDispatcher("salesItems.jsp").forward(request, response);
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
    
    private void handleViewEmployees(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Employee> employeeList = employeeService.getAllEmployees();
        request.setAttribute("employeeList", employeeList);
        request.getRequestDispatcher("AdminDashboard.jsp").forward(request, response);
    }
    
    private void handleAddManager(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("addEmployee.jsp").forward(request, response);
    }
    
    private void handleViewReports(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendRedirect("SalesDemo");
    }
    
    private void handleAddStore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
    }
}
