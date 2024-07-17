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
            <!-- Script to trigger notification update on IBT Main Dashboard -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        $(document).ready(function() {
            // Trigger a click on "IBT Requests" button in IBTMainDashboard.jsp
            $("a[href='IBTMainDashboard.jsp']").click(function() {
                // Post a message to parent window (IBTMainDashboard.jsp)
                window.postMessage("refreshIBTNotifications", "*");
            });
        });
    </script>
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
            if(employee != null && employee.getRole() == Role.Manager){
        %>
        
        <h1>Manager</h1>
        
        <a href="employees?submit=getAddEmployee">Add Employee</a>
        
        <form action="IBTServlet" method="post">
            <input type="submit" value="Manage IBT's" name="IBT_switch">
        </form>
        
    
        
        <%} else {%>
        <jsp:include page="unauthorized.jsp"/>
        <%}%>
    </body>
</html>