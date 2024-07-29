<%-- 
    Document   : managerDashboard
    Created on : Jul 10, 2024, 1:27:16 PM
    Author     : Train 01
--%>

<%@page import="ateam.DTO.StorePerfomanceInSales"%>
<%@page import="ateam.Models.Product"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.Sale"%>
<%@page import="java.util.List"%>
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
        <jsp:include page="sidebar.jsp"/>
        <% 
        Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
        Map<String, Integer> monthReport = (Map<String, Integer>) request.getSession(false).getAttribute("reportForThisMonth");
        Map<String, StorePerfomanceInSales> getTopAchievingStores = (Map<String, StorePerfomanceInSales>) request.getSession(false).getAttribute("topAchievingStores");
        List<Store> stores = (List<Store>) request.getSession(false).getAttribute("stores");
        
        if(employee != null){
            if(getTopAchievingStores != null && !getTopAchievingStores.isEmpty()){
                StringBuilder labels = new StringBuilder();
                StringBuilder data = new StringBuilder();

                for(Map.Entry<String, StorePerfomanceInSales> entry : getTopAchievingStores.entrySet()){
                    
                    labels.append("'").append(entry.getKey()).append("',");
                    data.append(entry.getValue().getPercentageSold()).append(",");
                }

                if(labels.length() > 0){
                    labels.setLength(labels.length() - 1);
                    data.setLength(data.length() - 1);
                }
        %>

        <div class="manager-container">
            
            
            <div class="menu-content">
    <div class='heading'>
        <h1>Reports</h1>
    </div>
    <div class="report">
        <div class="two">
            <h4>Top Achieving Store</h4>
            <div class="input-submit">
                <input name="submit" value="download" hidden>
                <button class="submit-btn" id="submit">Download</button>
            </div>
        </div>
        <div class="graphBox">
            <div class="box">
                <canvas id="salesChart"></canvas>
            </div>
            <div class="box">
                <canvas id="salesPieChart"></canvas>
            </div>
        </div>
    </div>

    <% if (monthReport != null && !monthReport.isEmpty()) {
        StringBuilder monthDate = new StringBuilder();
        StringBuilder monthData = new StringBuilder();

        for (Map.Entry<String, Integer> entry : monthReport.entrySet()) {
            monthDate.append("'").append(entry.getKey()).append("',");
            monthData.append(entry.getValue()).append(",");
        }

        if (monthData.length() > 0) {
            monthDate.setLength(monthDate.length() - 1);
            monthData.setLength(monthData.length() - 1);
        }
    %>

    <div class="report">
        <div class="two">
            <h4>Sales of the month</h4>
            <label>Select a store</label>
            <select id="storeMonthlySales">
                <% for (Store store : stores) { %>
                <option value="<%= store.getStore_ID() %>"><%= store.getStore_name() %></option>
                <% } %>
            </select>
            <div class="input-submit">
                <input name="submit" value="download" hidden>
                <button class="submit-btn" id="submit">Download</button>
            </div>
        </div>
        <div class="graphBox">
            <div class="box">
                <canvas id="monthReportBar"></canvas>
            </div>
            <div class="box">
                <canvas id="monthReportPie"></canvas>
            </div>
        </div>
    </div>
    
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    // Data from server-side (replace with actual data from JSP)
    const salesLabels = [<%= labels.toString() %>]; // Replace with actual labels
    const salesData = [<%= data.toString() %>]; // Replace with actual data
    const monthLabels = [<%= monthDate.toString() %>]; // Replace with actual month labels
    const monthData = [<%= monthData.toString() %>]; // Replace with actual month data

    // Colors
    const barBgColor = 'rgba(54, 162, 235, 0.2)';
    const barBorderColor = 'rgba(54, 162, 235, 1)';
    const pieBgColor = [
        'rgba(255, 99, 132, 1)',
        'rgba(123, 89, 132, 1)',
        'rgba(255, 165, 99, 1)',
        'rgba(94, 83, 83, 1)'
    ];

    // Initialize sales charts
    const salesCtx = document.getElementById('salesChart').getContext('2d');
    const salesPieCtx = document.getElementById('salesPieChart').getContext('2d');
    initBarChart(salesCtx, salesLabels, salesData, 'Sales', barBgColor, barBorderColor);
    initPieChart(salesPieCtx, salesLabels, salesData, pieBgColor);

    // Initialize month report charts
    const monthBarCtx = document.getElementById('monthReportBar').getContext('2d');
    const monthPieCtx = document.getElementById('monthReportPie').getContext('2d');
    initBarChart(monthBarCtx, monthLabels, monthData, 'Percentage Sold', barBgColor, barBorderColor);
    initPieChart(monthPieCtx, monthLabels, monthData, pieBgColor);
});

// Function to initialize a bar chart
function initBarChart(ctx, labels, data, label, bgColor, borderColor) {
    return new Chart(ctx, {
        type: 'bar',
        data: {
            labels: labels,
            datasets: [{
                label: label,
                data: data,
                backgroundColor: bgColor,
                borderColor: borderColor,
                borderWidth: 1
            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return value + '%';
                        }
                    }
                }
            }
        }
    });
}

// Function to initialize a pie chart
function initPieChart(ctx, labels, data, bgColor) {
    return new Chart(ctx, {
        type: 'pie',
        data: {
            labels: labels,
            datasets: [{
                data: data,
                backgroundColor: bgColor
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'right'
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let label = context.label || '';
                            if (context.parsed !== null) {
                                label += ': ' + context.raw + '%';
                            }
                            return label;
                        }
                    }
                }
            }
        }
    });
}
</script>

        <%}%>
      



        <% }} %>
    </body>
</html>
