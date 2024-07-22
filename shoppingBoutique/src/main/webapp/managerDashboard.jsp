
<%@page import="ateam.DTO.EmployeeMonthSales"%>
<%-- 
    Document   : managerDashboard
    Created on : Jul 10, 2024, 1:27:16 PM
    Author     : Train 01
--%>

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
</head>
<body>
    <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
       List<Sale> sales = (List<Sale>) request.getSession(false).getAttribute("Sales");
       List<Employee> employees = (List<Employee>) request.getSession(false).getAttribute("Employees");
       List<Store> stores = (List<Store>) request.getSession(false).getAttribute("Stores");
       Map<String, Integer> salesData = (Map<String, Integer>) request.getAttribute("salesData");
       Map<String, Integer> monthSales = (Map<String, Integer>) request.getSession(false).getAttribute("report");
       Map<String, Integer> topEmp = (Map<String, Integer>) request.getSession(false).getAttribute("topSellingEmp");
       
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

    <div class="manager-container">
        <div class="sidebar">
            <jsp:include page="sidebar.jsp"/>
        </div>
        

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
                
                <!-- Monthly Sales in a Store -->
                <div class="two">
                    <h4>Sale of the Month</h4>
                        <form action="SalesDemo" method="post">
                            <div>
                                <select class="select-box" name="storeId">
                                    <% if(stores != null) {
                                        for(Store store : stores) { %>
                                        <option value="<%=store.getStore_ID() %>"><%=store.getStore_name() %></option>
                                        <% } } %>
                                </select>
                            </div>
                        
                            <input id="date" name="date" type="month" />
                            <button type="submit" name="submit" value="filter">Filter</button>
                        </form>
                    
                    <button onclick="filterSales()">Filter</button>
                    <div class="input-submit">
                        <input name="submit" value="download" hidden />
                        <button class="submit-btn" id="submit">Download</button>
                    </div>
                </div>
                <div class="graphBox">
                    <div class="box">
                        <canvas id="monthlySalesChart"></canvas>
                    </div>
                    <div class="box">
                        <canvas id="salesPieChart"></canvas>
                    </div>
                </div>
                                
                                
                                <!-- Top selling employeee  -->
                <div class="two">
                    <h4>Top Selling Employee</h4>
                        <form action="SalesDemo" method="post">
                            <div>
                                <select id="employeeSalesPerStore" class="select-box" name="storeId">
                                    <% if(stores != null) {
                                        for(Store store : stores) { %>
                                        <option value="<%=store.getStore_ID() %>"><%=store.getStore_name() %></option>
                                        <% } } %>
                                </select>
                            </div>
                        
                           
                            <button type="submit" name="submit" value="filter">Filter</button>
                        </form>
                    
                    
                    <div class="input-submit">
                        <input name="submit" value="download" hidden />
                        <button class="submit-btn" id="submit">Download</button>
                    </div>
                </div>
                <div class="graphBox">
                    <div class="box">
                        <canvas id="topEmpBar"></canvas>
                    </div>
                    <div class="box">
                        <canvas id="salesPieChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', () => {
            // Retrieve the sales data from JSP
       
            // Chart for Sales Data
            var ctx = document.getElementById('salesChart').getContext('2d');
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

            // Chart for Pie Data
            var ctxP = document.getElementById('salesPieChart').getContext('2d');
            var salesPieChart = new Chart(ctxP, {
                type: 'pie',
                data: {
                    labels: [<%= labels.toString() %>],
                    datasets: [{
                        label: 'Sales',
                        data: [<%= data.toString() %>],
                        backgroundColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(123, 89, 132, 1)',
                            'rgba(255, 165, 99, 1)',
                            'rgba(94, 83, 83, 1)'
                        ]
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
                                label: function(context){
                                    let label = context.label || '';
                                    if(context.parsed !== null){
                                        let percentage = context.raw + '%';
                                        label += ': ' + percentage;
                                    }
                                    return label;
                                }
                            }
                        }
                    }
                }
            });


            const monthYear = document.getElementById('date');

            // Function to Filter Sales
            
          

            
        });
    </script>
    
    
    <% if(monthSales != null){
        StringBuilder monthLabels = new StringBuilder();
        StringBuilder monthData = new StringBuilder();
        for(Map.Entry<String, Integer> entry : monthSales.entrySet()){
            monthLabels.append("'").append(entry.getKey()).append("',");
            monthData.append(entry.getValue()).append(",");
        }
        %>
        
        <script>
            var monthCtx = document.getElementById("monthlySalesChart").getContext('2d');
            var monthBar = new Chart(monthCtx, {
               type: 'bar',
               data: {
                   labels: [<%=monthLabels.toString() %>],
                   datasets: [{
                        label: 'Sales of the month',
                        data: [<%=monthData.toString()%>],
                        backgroundColor: 'rgba(61, 179, 242, 0.2)',  // Light blue background color
                        borderColor: 'rgba(61, 179, 242, 1)',
                        borderWidth: 2,
                        borderRadius: 5,
                        borderSkipped: false
                    }]
               },
               options: {
                    scales: {
                        y: {
                          beginAtZero: true
                        }
                    }
                }
            });
        </script>
        
    <% }%> 
        
    <!<!-- Top Selling Emp data -->    
    <% if(topEmp != null){
        StringBuilder topEmpLabels = new StringBuilder();
        StringBuilder topEmpData = new StringBuilder();
        for(Map.Entry<String, Integer> entry : topEmp.entrySet()){
            topEmpLabels.append("'").append(entry.getKey()).append("',");
            topEmpData.append(entry.getValue()).append(",");
        }
        %>    
        <script>
            document.addEventListener('DOMContentLoaded', function() {
                const emp = document.getElementById('employeeSalesPerStore');
                const topCtx = document.getElementById("topEmpBar").getContext('2d');

                // Initialize the chart with the sorted data
                const topEmpBar = new Chart(topCtx, {
                    type: 'bar',
                    data: {
                        labels: [<%= topEmpLabels.toString() %>],
                        datasets: [{
                            label: 'Top Selling Employee',
                            data: [<%= topEmpData.toString() %>],
                            backgroundColor: 'rgba(61, 179, 242, 0.2)',  // Light blue background color
                            borderColor: 'rgba(61, 179, 242, 1)',
                            borderWidth: 2,
                            borderRadius: 5,
                            borderSkipped: false
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });

                // Event listener for storeId changes
                emp.addEventListener('change', function() {
                    const storeId = emp.value;

                    // Fetch new data based on storeId
                    fetch('SalesDemo', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            storeId: storeId,
                            submit: 'topEmpByStore'  // Include an action parameter if needed for the server logic
                        })
                    })
                    .then(response => response.json())
                    .then(data => {
                        // Assuming `data` is an object with `labels` and `data` arrays
                        const labels = data.labels;
                        const salesData = data.data;
                        console.log(salesData);
                        // Update the chart with new data
                        topEmpBar.data.labels = labels;
                        topEmpBar.data.datasets[0].data = salesData;
                        topEmpBar.update();
                    })
                    .catch(error => console.error('Error fetching data:'+ error));
                            });
                        });
        </script>
        <% }%>
    <% }%> 

    <% } else { %>
    <jsp:include page="unauthorized.jsp" />
    <% } %>
</body>

</html>
