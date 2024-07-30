<%-- 
    Document   : managerDashboard
    Created on : Jul 10, 2024, 1:27:16 PM
    Author     : Train 01
--%>

<%@page import="ateam.DTO.TopProductDTO"%>
<%@page import="java.math.BigDecimal"%>
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
        Map<String, BigDecimal> monthReport = (Map<String, BigDecimal>) request.getSession(false).getAttribute("reportForThisMonth");
        Map<String, StorePerfomanceInSales> getTopAchievingStores = (Map<String, StorePerfomanceInSales>) request.getSession(false).getAttribute("topAchievingStores");
        List<Store> stores = (List<Store>) request.getSession(false).getAttribute("stores");
        Map<String, Integer> topSellingEmployees = (Map<String, Integer>) request.getSession(false).getAttribute("topSellingEmployee");
        Map<String, BigDecimal> leastPerformingStores = (Map<String, BigDecimal>) request.getSession(false).getAttribute("leastPerformingStores");
        Map<String, BigDecimal> todayReport = (Map<String, BigDecimal>) request.getSession(false).getAttribute("Today'sReport");
        List<TopProductDTO> topProduct = (List<TopProductDTO>) request.getSession(false).getAttribute("top40SellingProducts");
        List<Product> products = (List<Product>) request.getSession(false).getAttribute("Products");
        
        int pageSize = 10;
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int totalRows = topProduct.size();
        int totalPages = (int) Math.ceil((double) totalRows / pageSize);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRows);
        
        StringBuilder employeeNames = new StringBuilder();
        StringBuilder soldData = new StringBuilder();
        StringBuilder leastStoreLabels = new StringBuilder();
        StringBuilder leastStoreData = new StringBuilder();
        StringBuilder todaysLabels = new StringBuilder();
        StringBuilder todaysData = new StringBuilder();
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

        for (Map.Entry<String, BigDecimal> entry : monthReport.entrySet()) {
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
            <label>Select Month</label>
            <input name="monthDate" type="month" id="montDatePicker">
            
            <button id="RequestMonthReport">Filter</button>
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
     
    <%if(topSellingEmployees != null && !topSellingEmployees.isEmpty()){
        
        
       for(Map.Entry<String, Integer> entry : topSellingEmployees.entrySet()){
           employeeNames.append("'").append(entry.getKey()).append("',");
           soldData.append(entry.getValue()).append(",");
       }
       
       %>
    <div class="report">
        <div class="two">
            <h4>Top Selling Employee</h4>
            <label>Select a store</label>
            <select id="topSellingEmployee">
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
                <canvas id="topSellingEmpReportBar"></canvas>
            </div>
            <div class="box">
                <canvas id="topSellingEmpReportPie"></canvas>
            </div>
        </div>
    </div>
    <%}%>
    
    <%if (leastPerformingStores != null && !leastPerformingStores.isEmpty()){
        for (Map.Entry<String, BigDecimal> entry : leastPerformingStores.entrySet()){
            leastStoreLabels.append("'").append(entry.getKey()).append("',");
            leastStoreData.append(entry.getValue()).append(",");
        }%>
    
    <div class="report">
        <div class="two">
            <h4>Least Performing Stores</h4>
            <label>Stores who failed to reach in stock</label>
            <select id="leastPerformingStore">
                <option value="">40%</option>
                <option value="">50%</option>
                <option value="">60%</option>
            </select>
 
            <div class="input-submit">
                <input name="submit" value="download" hidden>
                <button class="submit-btn" id="submit">Download</button>
            </div>
        </div>
        <div class="graphBox">
            <div class="box">
                <canvas id="leastPerformingStoreBar"></canvas>
            </div>
            <div class="box">
                <canvas id="leastPerformingStorePie"></canvas>
            </div>
        </div>
    </div>
    <%}%>
    
    <% if (todayReport != null && !todayReport.isEmpty()){
        for (Map.Entry<String, BigDecimal> entry : todayReport.entrySet()){
            todaysLabels.append("'").append(entry.getKey()).append("',");
            todaysData.append(entry.getValue()).append(",");
        }
    %>
    <div class="report">
        <div class="two">
            <h4>Today's Report for All Stores</h4>
            
 
            <div class="input-submit">
                <input name="submit" value="download" hidden>
                <button class="submit-btn" id="submit">Download</button>
            </div>
        </div>
        <div class="graphBox">
            <div class="box">
                <canvas id="todayReportBar"></canvas>
            </div>
            <div class="box">
                <canvas id="todayReportPie"></canvas>
            </div>
        </div>
    </div>
    <%} else {%> <h4>No Sales on Progress today</h4><%}%>
    
    <% if (topProduct != null && !topProduct.isEmpty()){%>
    <div class="report tables">
        <div class="two">
            <h4>Top 40 Selling Products</h4>
            
 
            <div class="input-submit">
                <input name="submit" value="download" hidden>
                <button class="submit-btn" id="submit">Download</button>
            </div>
        </div>
        <div class="table1">
    <table>
        <thead>
            <tr>
                <th>Product Name</th>
                <th>Store Name</th>
                <th>Amount Sold</th>
            </tr>
        </thead>
        <tbody>
            <% for (int i = start; i < end; i++) { %>
            <tr>
                <td><%= topProduct.get(i).getProductName() %></td>
                <td><%= topProduct.get(i).getStoreName() %></td>
                <td><%= topProduct.get(i).getTotalQuantitySold() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>

                <div class="pagination">
    <% if (currentPage > 1) { %>
    <a href="?page=<%= currentPage - 1 %>">Previous</a>
    <% } %>
    <% for (int i = 1; i <= totalPages; i++) { %>
    <a href="?page=<%= i %>" <%= (i == currentPage) ? "class='active'" : "" %>><%= i %></a>
    <% } %>
    <% if (currentPage < totalPages) { %>
    <a href="?page=<%= currentPage + 1 %>">Next</a>
    <% } %>
</div>
        </div>
<%if (products != null && !products.isEmpty()){%>
<<div class="table2">
    <div class="two">
        <h4>Select the product to find a star teller for that product</h4>
        <select id='topProductSeller'>
            <option value='' disabled selected>Select Product</option>
            <% for (Product product : products) { %>
                <option value="<%= product.getProduct_ID() %>"><%= product.getProduct_name() %></option>
            <% } %>
        </select>
    </div>
    <div id='topSellingProduct' style="display: none">
        <table>
            <thead>
                <tr>
                    <th>Product Name</th>
                    <th>Teller Name</th>
                    <th>Amount Sold</th>
                </tr>
            </thead>
            <tbody id='productSellerDetails'>
                <!-- Dynamic content will be inserted here -->
            </tbody>
        </table>
    </div>
</div>
        <%}%>
    </div>
                <%}%>
</div>

<script>
document.addEventListener('DOMContentLoaded', () => {
    // Data from server-side (replace with actual data from JSP)
    const salesLabels = [<%= labels.toString() %>]; // Replace with actual labels
    const salesData = [<%= data.toString() %>]; // Replace with actual data
    const monthLabels = [<%= monthDate.toString() %>]; // Replace with actual month labels
    const monthData = [<%= monthData.toString() %>]; // Replace with actual month data
    const topSellingEmployees = [<%= employeeNames.toString() %>];
    const topSellingEmpData = [<%= soldData.toString() %>];
    const todaysReportLabels = [<%= todaysLabels.toString() %>];
    const todaysReportData = [<%= todaysData.toString() %>];
    

console.log(topSellingEmployees);
console.log(topSellingEmpData);
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
    BarChart(monthBarCtx, monthLabels, monthData, 'Total Amount', barBgColor, barBorderColor);
    initPieChart(monthPieCtx, monthLabels, monthData, pieBgColor);
    
    // Initialize topSelling employee charts
    const topSellingEmpBarCtx = document.getElementById('topSellingEmpReportBar').getContext('2d');
    const topSellingEmpPieCtx = document.getElementById('topSellingEmpReportPie').getContext('2d');
    BarChart(topSellingEmpBarCtx, topSellingEmployees, topSellingEmpData, 'Top Employee', barBgColor, barBorderColor);
    initPieChart(topSellingEmpPieCtx, topSellingEmployees, topSellingEmpData, pieBgColor);
    
    // Initialize Least Performing Stores Charts
    const leastPerformingBarCtx = document.getElementById('leastPerformingStoreBar').getContext('2d');
    const leastPerformingPieCtx = document.getElementById('leastPerformingStorePie').getContext('2d');
    BarChart(leastPerformingBarCtx, [<%= leastStoreLabels.toString() %>], [<%= leastStoreData.toString() %>],'Least Performing Stores', barBgColor, barBorderColor);
    initPieChart(leastPerformingPieCtx, [<%= leastStoreLabels.toString() %>], [<%= leastStoreData.toString() %>],pieBgColor);
    
    // Initialize today's progress reports
    const todayReportBarCtx = document.getElementById('todayReportBar').getContext('2d');
    const todayReportPieCtx = document.getElementyId('todayReportPie').getContext('2d');
    BarChart(todayReportBarCtx, todaysReportLabels, todaysReportData, 'Today\'s Progress', barBgColor, barBorderColor);
    initPieChart(todayReportPieCtx, todaysReportLabels, todaysReportData, pieBgColor);
    
    document.getElementById('RequestMonthReport').addEventListener('click', function(){
        const storeId = document.getElementById('storeMonthlySales').value;
        const month = document.getElementById('montDatePicker').value;
        
        if (!storeId || !month) {
            alert('Please select a store and month.');
            return;
        }
        
        fetch('SalesDemo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({submit: 'getMonthReport', date: month, storeId: storeId})
        }).then(response => response.json())
          .then(data => {
            const storeNames = data.labels;
            const storePercentages = data.data;
            console.log("store achieve target: "+ storeNames);
            // Update Bar Chart
            monthBarCtx.data.labels = storeNames;
            monthBarCtx.data.datasets[0].data = storePercentages;
            monthBarCtx.update();

            // Update Pie Chart
            monthPieCtx.data.labels = storeNames;
            monthPieCtx.data.datasets[0].data = storePercentages;
           
            monthPieCtx.update();
        })
        .catch(error => console.error('Error fetching data:', error));
});

const selectElement = document.getElementById('topProductSeller');

    selectElement.addEventListener('change', function() {
        const productId = this.value;

console.log("product id from topProduct: "+ productId);
        if (productId) {
            fetchTopSellingEmployee(productId);
        }
    });

    function fetchTopSellingEmployee(productId) {
        fetch('SalesDemo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: new URLSearchParams({
                submit: 'getTopSellingEmployeeBasedOnProduct',
                productId: productId
            })
        })
        .then(response => response.json())
        .then(data => {
            updateTable(data);
        })
        .catch(error => console.error('Error:', error));
    }

    function updateTable(data) {
        const tableBody = document.getElementById('productSellerDetails');
        tableBody.innerHTML = ''; // Clear existing rows

        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${data.productName}</td>
            <td>${data.tellerName}</td>
            <td>${data.amountSold}</td>
        `;

        tableBody.appendChild(row);
        document.getElementById('topSellingProduct').style.display = 'block';
    }

    document.getElementById('topSellingEmployee').addEventListener('change', function(){
        const storeId = this.value;
        console.log("store id need want to check for top employee"+storeId);
        
        fetch('SalesDemo',{
            method:'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams({
                submit: '',
                storeId: storeId
            })
        })
        .then(response => response.json())
            .then(data => {
                const names = data;
                
            })
            .catch(error => console.error('Error fetching data:', error));
    });
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

function BarChart(ctx, labels, data, label, bgColor, borderColor) {
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
                    beginAtZero: true
                    
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
