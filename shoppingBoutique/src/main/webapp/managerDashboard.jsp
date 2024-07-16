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
        
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
           Map<String, Integer> salesData = (Map<String, Integer>) request.getAttribute("salesData");
           
           if(employee != null){
            if(salesData != null && !salesData.isEmpty()){
            StringBuilder labels = new StringBuilder();
            StringBuilder data = new StringBuilder();
            int totalSales = 0;

            for(Map.Entry<String, Integer> entry : salesData.entrySet()){
                if(entry.getValue() > totalSales)
                     totalSales += entry.getValue();
                labels.append("'").append(entry.getKey())
                        .append("',");
                data.append(String.format("%.2f",(entry.getValue() * 100.0 / totalSales))).append(",");
            }


            if(labels.length() > 0){
                labels.setLength(labels.length() - 1);
                data.setLength(data.length() - 1);
            }
        %>
        <jsp:include page="navbar.jsp"/>
        <div class="manager-container">
            <div class="sidebar">
                
                    <a href="employees?submit=getAddEmployee" >Add Employee</a>
                    <a href="employees?submit=getAddEmployee" >Download Reports</a>
                    <a href="#" >Manage IBT</a>
            </div>
            
            <div class="menu-content">
                <div class='heading'>
                    <h1>Reports</h1>
                </div>

                
                <div class="report">
                    <div class="two">
                        <h4>Top achieving Store</h4>
                        <div class="input-submit">
                            <input name="submit" value="download" hidden>
                            <button class="submit-btn" id="submit">Download</button>
                        </div>
                    </div>
                    <div class="graphBox">

                        <div class="box">
                            <canvas id="salesChart" ></canvas>
                        </div>
                        <div class="box">
                            <canvas id="salesPieChart" ></canvas>
                        </div>
                    </div>
                </div>
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
                        barPercentage: 1,
                        label: 'Sales',
                        data: [<%= data.toString() %>],
                        backgroundColor: [
                            'rgba(255, 99, 132, 1)'
                        ]
                    }]
                },
                options: {
                    scales: {
                        y: {
                            ticks: {
                                callback: function(value) {
                                    return value + '%';
                                }
                            }
                        }
                    }
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
                            'rgba(255, 99, 132, 1)',
                            'rgba(123, 89, 132, 1)',
                            'rgba(255, 165, 99, 1)'
                        ]
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'top'
                        },
                        tooltip: {
                            label: function(context){
                                let label = context.label || '';
                                if(context.parsed !== null){
                                    let percentage = context.parsed + '%';
                                    label += ': ' + percentage;
                                }
                                return label;
                            }
                        }
                    }
                }
            });
        </script>
        <%} %> 
        
        <%} else {%>
        <jsp:include page="unauthorized.jsp" />
        <%}%>
    </body>
</html>
