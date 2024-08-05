<%-- 
    Document   : managerSidebar
    Created on : 05 Aug 2024, 14:06:10
    Author     : T440
--%>

<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sidebar.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/sidebar.css" />
        
        <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
        <script src="${pageContext.request.contextPath}/js/sidebar.js" defer></script>
    </head>
    <body>
         <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");%>
        
        <nav class="sidebar close">
            <div class="menu">
            <ul class="menu-link">
                <header>
                    <div class="image-text">
                   

                        <div class="text logo-text">
                            <span class="name">Carols Boutique</span>
                            <span class="profession">Where comfort meets style</span>
                        </div>
                    </div>

                    <i class='bx bx-chevron-right toggle'></i>
                </header>
                
                <li class="nav-link">
                    <a href="SalesDemo?submit=getReports">
                        <i class='bx bx-home-alt-2 icon' ></i>
                        <span class="text nav-text">Home</span>
                    </a>
                </li>
                
                <li class="nav-link">
                    <a href="SalesDemo?submit=getReports">
                        <i class='bx bxs-report icon' ></i>
                        <span class="text nav-text">Reports</span>
                    </a>
                </li>
                
                
                <li "class="nav-link">
                     <a href="IBTMainDashboard.jsp">
                     <i class='bx bx-transfer icon'></i>
                        <span class="text nav-text">IBT</span>
                     <%request.getSession().setAttribute("ibtNotifications", true);%>
                     </a>
                </li>
            </ul>
        </div>
                
               <div class="bottom-content">
                    <li class="">
                        <a href="EmployeeServlet?submit=logout">
                            <i class='bx bx-log-out icon' ></i>
                            <span class="text nav-text">Logout</span>
                        </a>
                    </li>

                </div>
                
            </ul>
        </nav>
    </body>
</html>
