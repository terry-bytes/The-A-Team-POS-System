<%-- 
    Document   : managerDashboard
    Created on : Jul 10, 2024, 1:27:16 PM
    Author     : Train 01
--%>

<%@page import="java.util.Map"%>
<%@page import="ateam.Models.Role"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report.css">
        
    </head>
    <body>
        <jsp:include page="navbar.jsp"/>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
           Map<String, Integer> salesData = (Map<String, Integer>) request.getAttribute("salesData");
           if(salesData != null && !salesData.isEmpty()){
           StringBuilder labels = new StringBuilder();
           StringBuilder data = new StringBuilder();
           for(Map.Entry<String, Integer> entry : salesData.entrySet()){
               labels.append("'").append(entry.getKey())
                       .append("',");
               data.append(entry.getValue()).append(",");
           }
           System.out.println("label size: "+labels.length());
           if(labels.length() > 0){
               labels.setLength(labels.length() - 1);
               data.setLength(data.length() - 1);
           }
        %>
        
        <h1>Manager</h1>
        
        <a href="employees?submit=getAddEmployee">Add Employee</a>
        
        <h3>Store with Most Sales</h3>

        
        <h4>Top achieving Store</h4>
        <div class="graphBox">
            
            <div class="box">
                <canvas id="salesChart" ></canvas>
            </div>
            <div class="box">
                <canvas id="salesPieChart" ></canvas>
            </div>
        </div>
        
        
        <script>
            var ctx = document.getElementById('salesChart').getContext('2d');
            var ctxP = document.getElementById('salesPieChart').getContext('2d');
            var salesChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: [<%= labels.toString() %>],
                    datasets: [{
                        label: 'Sales',
                        data: [<%= data.toString() %>],
                        backgroundColor: [
                            'rgba(255, 99, 132, 1)'
                        ]
                    }]
                },
                options: {
                    responsive: true
                }
            });
            
            var salesChart = new Chart(ctxP, {
                type: 'pie',
                data: {
                    labels: [<%= labels.toString() %>],
                    datasets: [{
                        label: 'Sales',
                        data: [<%= data.toString() %>],
                        backgroundColor: [
                            'rgba(255, 99, 132, 1)'
                        ]
                    }]
                },
                options: {
                    responsive: true
                }
            });
        </script>
        <%} else {%>
        <jsp:include page="unauthorized.jsp" />
        <%}%>
    </body>
</html>
