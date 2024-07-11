<%-- 
    Document   : navbar
    Created on : Jul 10, 2024, 9:00:26 AM
    Author     : Train 01
--%>

<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/navbar.css">
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Attribute");%>
        <nav class="navbar">
            <div class='logo'>
                <h3>Carol's Boutique</h3>
                <p>Name of the branch</p>
            </div>
            <div class="nav-menu">
                <ul>
                    <li><a href="" class="link">Dashboard</a></li>
                    <li><a href="" class="link">My Store</a></li>
                    <li><a href="" class="link">Notifications</a></li>
                </ul>
            </div>
        </nav>
    </body>
</html>
