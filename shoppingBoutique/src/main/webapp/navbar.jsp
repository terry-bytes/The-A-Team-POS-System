<%-- 
    Document   : navbar
    Created on : Jul 10, 2024, 9:00:26 AM
    Author     : Train 01
--%>

<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
        
        <link href="https://fonts.googleapis.com/icon?family=Material+Icons+Sharp"
      rel="stylesheet">
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            Store store = (Store) request.getSession(false).getAttribute("store");
        %>
        <nav class="navbar">
            <div class='logo'>
                <h3>Carol's Boutique</h3>
                <p>store</p>
            </div>
            <div class="nav-menu">
                <ul>
                    
                    <li><a href="SalesDemo?submit=getReports" class="link">Dashboard</a></li>
                    <li><a href="StoreServlet?submit=getStoreDashboard" class="link">My Store</a></li>
                    
                    
                </ul>
                
            </div>
        </nav>
    </body>
</html>
