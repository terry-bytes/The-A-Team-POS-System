<%-- 
    Document   : sidebar
    Created on : Jul 17, 2024, 9:15:57 AM
    Author     : Admin
--%>

<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sidebar</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sidebar.css" />
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");%>
        
        <nav class="sidebar">
            <ul class="nav">
                <li class="logo">
                    <h4>CB</h4>
                    <span>Carols Boutique</span>
                </li>
                <li>
                    <i class="fa fa-bar-chart" aria-hidden="true"></i>
                    <a href="#" class="nav-item">Dashboard</a>
                </li>
                <li>
                    <i class="fa fa-users" aria-hidden="true"></i>
                    <a href="#" class="nav-item">My Employees</a>
                </li>
                <li>
                    <i class="fa fa-shopping-bag" aria-hidden="true"></i>
                    <a href="#" class="nav-item">My Store</a>
                </li>
                <li>
                    <i class="fa fa-bell" aria-hidden="true"></i>
                    <a href="#" class="nav-item">Notification</a>
                </li>
                <li>
                    <i class="fa fa-sign-out" aria-hidden="true"></i>
                    <a href="#" class="nav-item">Logout</a>
                </li>
                <li "class="logo">
                     <a href="IBTMainDashboard.jsp" class="nav-item">IBT</a>
                </li>
            </ul>
        </nav>
    </body>
</html>
