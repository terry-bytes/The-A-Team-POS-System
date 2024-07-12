<%-- 
    Document   : managerDashboard
    Created on : Jul 10, 2024, 1:27:16 PM
    Author     : Train 01
--%>

<%@page import="ateam.Models.Role"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <jsp:include page="navbar.jsp"/>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
            if(employee != null && employee.getRole() == Role.Manager){
        %>
        
        <h1>Manager</h1>
        
        <a href="employees?submit=getAddEmployee">Add Employee</a>
        
        <%} else {%>
        <jsp:include page="unauthorized.jsp"/>
        <%}%>
    </body>
</html>
