/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ateam.Servlets;

import ateam.BDconnection.Connect;
import ateam.DAOIMPL.StoreDAOIMPL;
import ateam.Exception.DuplicateStoreException;
import ateam.Models.Store;
import ateam.Service.StoreService;
import ateam.ServiceImpl.StoreServiceImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author carme
 */
@WebServlet(name = "StoreServlet", urlPatterns = {"/StoreServlet"})
public class StoreServlet extends HttpServlet {
    private StoreService storeService =  new StoreServiceImpl(new StoreDAOIMPL(new Connect().connectToDB()));;
    
    
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
        
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
        
        switch(request.getParameter("submit")){
            case "getStoreDashboard":
                request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
                break;
        }
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        switch(request.getParameter("submit")) {
            case "View_Stores":
                handleViewAllStores(request, response);
                break;
            case "Search_Store":
                handleSearchStore(request, response);
            case "Update_Store":
                handleUpdatingStore(request, response);
                break;
            case "Delete_Store":
                handleDeletingStore(request, response);
                break;
            case "Submit_Store":
                handleAddStore(request, response);
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
    
    private void handleAddStore(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        Store store = new Store();
        store.setStore_name(request.getParameter("storeName"));
        store.setStore_address(request.getParameter("storeAddress"));
        store.setStore_city(request.getParameter("storeCity"));
        store.setStore_province(request.getParameter("storeProvince"));
        try{
            store.setStore_zipcode(Integer.parseInt(request.getParameter("storeZipcode")));
        }catch(NumberFormatException num){
            request.setAttribute("message", "Zip Code must be a number");
            request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
        }
        store.setStore_phone(request.getParameter("storePhone"));
        store.setStore_email(request.getParameter("storeEmailAddress"));
        boolean success;
        try {
            success = storeService.addStore(store);
            if (success) {
                request.setAttribute("message", "Store added successfully");
            } else {
                request.setAttribute("message", "Failed to add store");
            }
        } catch (DuplicateStoreException ex) {
            request.setAttribute("message", ex.getMessage());
        }
        
        request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
    }
    
    private void handleViewAllStores(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         List<Store> stores = storeService.getAllStores();
    request.setAttribute("stores", stores);
    request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
    }
    
    public void handleSearchStore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int store_id = Integer.parseInt(request.getParameter("storeID"));
        Store store = storeService.getStoreById(store_id);
        request.setAttribute("Store", store);
        request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
        
    }
    
    public void handleUpdatingStore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Store store = new Store();
       store.setStore_ID(Integer.parseInt(request.getParameter("storeIDUpdate")));
       store.setStore_name(request.getParameter("storeName"));
       store.setStore_address(request.getParameter("storeAddress"));
       store.setStore_city(request.getParameter("storeCity"));
       store.setStore_province(request.getParameter("storeProvince"));
       store.setStore_zipcode(Integer.parseInt(request.getParameter("storeZipcode")));
       store.setStore_phone(request.getParameter("storePhone"));
       store.setStore_email(request.getParameter("storeEmailAddress"));
       boolean success = storeService.updateStore(store);
       if (success) {
        request.setAttribute("message", "Store updated successfully");
    } else {
        request.setAttribute("message", "Failed to update store");
    }
        request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
    }
    
    public void handleDeletingStore(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int store_id = Integer.parseInt(request.getParameter("StoreIDDelete")) ;
        boolean success = storeService.deleteStore(store_id);
        if (success) {
        request.setAttribute("message", "Store deleted successfully");
    } else {
        request.setAttribute("message", "Failed to delete store");
    }
        request.getRequestDispatcher("storeDashboard.jsp").forward(request, response);
    }
}

