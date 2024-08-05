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
        <title>Manager Dashboard</title>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report.css">
        <!-- Script to trigger notification update on IBT Main Dashboard -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

        <script>
            $(document).ready(function () {
                // Trigger a click on "IBT Requests" button in IBTMainDashboard.jsp
                $("a[href='IBTMainDashboard.jsp']").click(function () {
                    // Post a message to parent window (IBTMainDashboard.jsp)
                    window.postMessage("refreshIBTNotifications", "*");
                });
            });
        </script>
    </head>
    <body>
        
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
            Map<Integer, Integer> salesInHour = (Map<Integer, Integer>) request.getSession(false).getAttribute("SalesRate");

            int pageSize = 10;
            int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int totalRows = topProduct.size();
            int totalPages = (int) Math.ceil((double) totalRows / pageSize);

            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, totalRows);

            StringBuilder monthDate = new StringBuilder();
            StringBuilder monthData = new StringBuilder();
            StringBuilder employeeNames = new StringBuilder();
            StringBuilder soldData = new StringBuilder();
            StringBuilder leastStoreLabels = new StringBuilder();
            StringBuilder leastStoreData = new StringBuilder();
            StringBuilder todaysLabels = new StringBuilder();
            StringBuilder todaysData = new StringBuilder();
            StringBuilder labels = new StringBuilder();
            StringBuilder data = new StringBuilder();
            StringBuilder saleRateLabels = new StringBuilder();
            StringBuilder saleRateData = new StringBuilder();
            
            if (employee != null) {
                if (getTopAchievingStores != null && !getTopAchievingStores.isEmpty()) {

                    for (Map.Entry<String, StorePerfomanceInSales> entry : getTopAchievingStores.entrySet()) {

                        labels.append("'").append(entry.getKey()).append("',");
                        data.append(entry.getValue().getPercentageSold()).append(",");
                    }

                    if (labels.length() > 0) {
                        labels.setLength(labels.length() - 1);
                        data.setLength(data.length() - 1);
                    }
        %>
        <% if (employee.getRole() == Role.Admin){%>
        <jsp:include page="managerSidebar.jsp" />
        <%} else {%>
        <jsp:include page="sidebar.jsp" />
        <%}%>
        <div class="manager-container">


            <div class="menu-content">
                <div class='heading'>
                    <h1>Reports</h1>
                </div>
                <form action="DownloadReportPDF" method="get">
                    <button type="submit" class="submit-btn">Download PDF</button>
                </form>
                <form action="SalesDemo" method="post">
                    <input type="hidden" name="submit" value="downloadReport">
                    <button type="submit">Download Sales Items Report</button>
                </form>

                <!------------------- Top Achieving Store ---------------------->
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
                    <!------------------- Month sales ----------------->
                    <div class="two">
                        <h4>Sales of the month</h4>
                        <label>Select a store</label>
                        <select id="storeMonthlySales">
                            <% for (Store store : stores) {%>
                            <option value="<%= store.getStore_ID()%>"><%= store.getStore_name()%></option>
                            <% } %>
                        </select>
                        <label>Select Month</label>
                        <input name="monthDate" type="month" id="monthDatePicker">

                        <button type="button" id="RequestMonthReport">Filter</button>



                        <div class="input-submit">
                            <input name="submit" value="download" hidden>
                            <button class="submit-btn"  id="submit">Download</button>
                        </div>
                    </div>
                    <div class="graphBox">
                        <div class="box" >
                            <canvas id="monthReportBar"></canvas>
                        </div>
                        <div class="box">
                            <canvas id="monthReportPie"></canvas>
                        </div>
                    </div>
                </div>
                        <% }%>

                <%if (topSellingEmployees != null && !topSellingEmployees.isEmpty()) {

                        for (Map.Entry<String, Integer> entry : topSellingEmployees.entrySet()) {
                            employeeNames.append("'").append(entry.getKey()).append("',");
                            soldData.append(entry.getValue()).append(",");
                        }

                %>
                <div class="report">
                    <div class="two">
                        <h4>Top Selling Employee</h4>
                        <label>Select a store</label>
                        <select id="topSellingEmployee">
                            <option >Select Store</option>
                            <% for (Store store : stores) {%>
                            <option value="<%= store.getStore_ID()%>"><%= store.getStore_name()%></option>
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

                <%if (leastPerformingStores != null && !leastPerformingStores.isEmpty()) {
                        for (Map.Entry<String, BigDecimal> entry : leastPerformingStores.entrySet()) {
                            leastStoreLabels.append("'").append(entry.getKey()).append("',");
                            leastStoreData.append(entry.getValue()).append(",");
                        }%>

                <div class="report">
                    <div class="two">
                        <h4>Least Performing Stores</h4>
                        <label>Stores who failed to reach in stock</label>
                        <select id="leastPerformingStoreTarget">
                            <option value="40.0">40%</option>
                            <option value="50">50%</option>
                            <option value="60">60%</option>
                        </select>

                        <label>Choose month interval </label>
                        <select id="monthInterval">
                            <option value="3">3 Months</option>
                            <option value="3">6 Months</option>
                        </select>

                        <button type="button" id="leastPerformingStore">Filter</button>
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

                <% if (todayReport != null && !todayReport.isEmpty()) {
                        for (Map.Entry<String, BigDecimal> entry : todayReport.entrySet()) {
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


                <div class="report tables">
                    <% if (topProduct != null && !topProduct.isEmpty()) {%>
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
                                <% for (int i = start; i < end; i++) {%>
                                <tr>
                                    <td><%= topProduct.get(i).getProductName()%></td>
                                    <td><%= topProduct.get(i).getStoreName()%></td>
                                    <td><%= topProduct.get(i).getTotalQuantitySold()%></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>

                        <div class="pagination">
                            <% if (currentPage > 1) {%>
                            <a href="?page=<%= currentPage - 1%>">Previous</a>
                            <% } %>
                            <% for (int i = 1; i <= totalPages; i++) {%>
                            <a href="?page=<%= i%>" <%= (i == currentPage) ? "class='active'" : ""%>><%= i%></a>
                            <% } %>
                            <% if (currentPage < totalPages) {%>
                            <a href="?page=<%= currentPage + 1%>">Next</a>
                            <% } %>
                        </div>
                        <%}%>
                    </div>
                    <%if (products != null && !products.isEmpty()) {%>
                    <div class="table2">
                        <div class="two">
                            <h4>Select the product to find a star teller for that product</h4>
                            <select id='topProductSeller'>
                                <option  disabled selected>Select Product</option>
                                <% for (Product product : products) {%>
                                <option value="<%= product.getProduct_ID()%>"><%= product.getProduct_name()%></option>
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
                <% } %>
                <% if (salesInHour != null && !salesInHour.isEmpty()){
                    for (Map.Entry<Integer, Integer> entry : salesInHour.entrySet()){
                        saleRateLabels.append("'").append(entry.getKey()).append("',");
                        saleRateData.append(entry.getValue()).append(",");
                    }
                %>
                <div class="report">
                    <div class="two">
                        <h4>Sales Rate</h4>

                        <h4>Check another stores</h4>
                            <select id='hourlyRateStoreId'>
                                <option  disabled selected>Select Product</option>
                                <% for (Store store : stores) {%>
                                <option value="<%= store.getStore_ID()%>"><%= store.getStore_name() %></option>
                                <% } %>
                            </select>
                        <div class="input-submit">
                            <input name="submit" value="download" hidden>
                            <button class="submit-btn" id="submit">Download</button>
                        </div>
                    </div>
                    <div class="graphBox">
                        <div class="box">
                            <canvas id="salesRateBar"></canvas>
                            <div id="noDataBar" class="no-data-message">No data available</div>
                        </div>
                        <div class="box">
                            <canvas id="salesRatePie"></canvas>
                            <div id="noDataPie" class="no-data-message">No data available</div>
                        </div>
                    </div>
                </div>
            <%} }%>
                <script>
                   document.addEventListener('DOMContentLoaded', () => {
        console.log("Screen loaded successfully...");

        // Data from server-side (replace with actual data from JSP)
        const salesLabels = [<%= labels.toString() %>];
        const salesData = [<%= data.toString() %>];
        const monthLabels = [<%= monthDate.toString() %>]; // Replace with actual month labels
        const monthData = [<%= monthData.toString() %>]; // Replace with actual month data
        const topSellingEmployees = [<%= employeeNames.toString() %>];
        const topSellingEmpData = [<%= soldData.toString() %>];
        const todaysReportLabels = [<%= todaysLabels.toString() %>];
        const todaysReportData = [<%= todaysData.toString() %>];
        const leastPerformingLabels = [<%= leastStoreLabels.toString() %>];
        const leastPerformingData = [<%= leastStoreData.toString() %>];
        const salesRateLabels = [<%= saleRateLabels.toString() %>];
        const salesRateData = [<%= saleRateData.toString() %>];

        let monthBarChart = null;
        let monthPieChart = null;
        let leastBarChart = null;
        let leastPieChart = null;
        let topEmpBarChart = null;
        let topEmpPieChart = null;
        let saleAnalyticBarChart = null;
        let saleAnalyticPieChart = null;

        // Colors
        const barBgColor = 'rgba(54, 162, 235, 0.2)';
        const barBorderColor = 'rgba(54, 162, 235, 1)';
        const pieBgColor = [
            'rgba(255, 99, 132, 1)',
            'rgba(123, 89, 132, 1)',
            'rgba(255, 165, 99, 1)',
            'rgba(94, 83, 83, 1)'
        ];

        // Initialize charts
        const salesCtx = document.getElementById('salesChart').getContext('2d');
        const salesPieCtx = document.getElementById('salesPieChart').getContext('2d');
        initBarChart(salesCtx, salesLabels, salesData, 'Sales', barBgColor, barBorderColor);
        initPieChart(salesPieCtx, salesLabels, salesData, pieBgColor);


        let monthBarCtx;
        let monthPieCtx;
        if(monthLabels.length > 0 && monthData.length > 0 ){
        monthBarCtx = document.getElementById('monthReportBar').getContext('2d');
        monthPieCtx = document.getElementById('monthReportPie').getContext('2d');
        BarChart(monthBarCtx, monthLabels, monthData, 'Total Amount', barBgColor, barBorderColor);
        initPieChart(monthPieCtx, monthLabels, monthData, pieBgColor);}

        
        const topSellingEmpBarCtx = document.getElementById('topSellingEmpReportBar').getContext('2d');
        const topSellingEmpPieCtx = document.getElementById('topSellingEmpReportPie').getContext('2d');
        BarChart(topSellingEmpBarCtx, topSellingEmployees, topSellingEmpData, 'Top Employee', barBgColor, barBorderColor);
        initPieChart(topSellingEmpPieCtx, topSellingEmployees, topSellingEmpData, pieBgColor);

        const leastPerformingBarCtx = document.getElementById('leastPerformingStoreBar').getContext('2d');
        const leastPerformingPieCtx = document.getElementById('leastPerformingStorePie').getContext('2d');
        BarChart(leastPerformingBarCtx, leastPerformingLabels, leastPerformingData, 'Least Performing Stores', barBgColor, barBorderColor);
        initPieChart(leastPerformingPieCtx, leastPerformingLabels, leastPerformingData, pieBgColor);

        const salesRateBarCtx = document.getElementById('salesRateBar').getContext('2d');
        const salesRatePieCtx = document.getElementById('salesRatePie').getContext('2d');
        BarChart(salesRateBarCtx, salesRateLabels, salesRateData, 'Hourly Sales for Last 30 days', barBgColor, barBorderColor);
        initPieChart(salesRatePieCtx, salesRateLabels, salesRateData, pieBgColor);
        
        // Event listeners
        if(monthLabels.length > 0 && monthData.length > 0 ){
        document.getElementById('RequestMonthReport').addEventListener('click', function () {
            const storeId = document.getElementById('storeMonthlySales').value;
            const monthDate = document.getElementById('monthDatePicker').value;

            if (!storeId || !monthDate) {
                alert('Please select both a store and a month.');
                return;
            }

            fetchMonthReport(storeId, monthDate);
        });
    }

        document.getElementById("leastPerformingStore").addEventListener('click', function () {
            const target = document.getElementById("leastPerformingStoreTarget").value;
            const month = document.getElementById('monthInterval').value;

            fetchLeastPerformingStore(target, month);
        });

        document.getElementById('topSellingEmployee').addEventListener('change', function () {
            const storeId = this.value;

            fetch('SalesDemo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    submit: 'topEmpByStore',
                    storeId: storeId
                })
            })
                .then(response => response.json())
                .then(data => {
                    const names = data.labels;
                    const topData = data.data;

                    topEmpBarChart = updateBarChart(topSellingEmpBarCtx, names, topData, 'Top Selling Employee', barBgColor, barBorderColor);
                    topEmpPieChart = updatePieChart(topSellingEmpPieCtx, names, topData, pieBgColor);
                })
                .catch(error => console.error('Error fetching data:', error));
        });

        document.getElementById("topProductSeller").addEventListener('change', function () {
            const productId = this.value;
            getTopProductSeller(productId);
        });

        document.getElementById("hourlyRateStoreId").addEventListener('change', function(){
            const storeId = this.value;
            getSaleAnalytics(storeId);
        });
        
        function getSaleAnalytics(storeId){
            fetch('SalesDemo', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: new URLSearchParams({storeId: storeId, submit: 'salesAnalytics'})
            })
                    .then(response => response.json())
                    .then(data => {
                        const salesAnalyticsLabel = data.labels;
                        const salesAnalyticsData = data.data;
                        
                        if(saleAnalyticPieChart){
                            saleAnalyticBarChart.destroy();
                            saleAnalyticPieChart.destroy();
                        }
                        
                        
                        if (salesAnalyticsData.length > 0) {
                            
                            document.getElementById('salesRateBar').style.display = 'block';
                            document.getElementById('noDataBar').style.display = 'none';
                            saleAnalyticBarChart = updateBarChart(salesRateBarCtx, salesAnalyticsLabel, salesAnalyticsData, 'Hourly Sales for Last 30 days', barBgColor, barBorderColor);

                            document.getElementById('salesRatePie').style.display = 'block';
                            document.getElementById('noDataPie').style.display = 'none';
                            saleAnalyticPieChart = updatePieChart(salesRatePieCtx, salesAnalyticsLabel, salesAnalyticsData, pieBgColor);
                        } else {
                            document.getElementById('salesRateBar').style.display = 'none';
                            document.getElementById('noDataBar').style.display = 'block';

                            document.getElementById('salesRatePie').style.display = 'none';
                            document.getElementById('noDataPie').style.display = 'block';
                        }
                    })
                    .catch(error => console.error('Error:', error));
        }

        function getTopProductSeller(productId) {
            fetch('SalesDemo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    productId: productId,
                    submit: 'getTopSellingEmployeeBasedOnProduct'
                })
            })
                .then(response => response.json())
                .then(data => {
                    const tableBody = document.getElementById('productSellerDetails');
                    tableBody.innerHTML = ''; // Clear previous results

                    if (data && data.length > 0) {
                        data.forEach(item => {
                            const row = document.createElement('tr');
                            row.innerHTML = `
                                <td>${item.productName}</td>
                                <td>${item.tellerName}</td>
                                <td>${item.amountSold}</td>
                            `;
                            tableBody.appendChild(row);
                        });

                        document.getElementById('topSellingProduct').style.display = 'block';
                    } else {
                        document.getElementById('topSellingProduct').style.display = 'none';
                    }
                })
                .catch(error => console.error('Error:', error));
        }

        function fetchMonthReport(storeId, monthDate) {
            fetch('SalesDemo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    storeId: storeId,
                    date: monthDate,
                    submit: 'getMonthReport'
                })
            })
                .then(response => response.json())
                .then(data => {
                    const monthLabels = data.labels;
                    const monthData = data.data;

                    if (monthBarChart) {
                        monthBarChart.destroy();
                    }
                    if (monthPieChart) {
                        monthPieChart.destroy();
                    }

                    monthBarChart = updateBarChart(monthBarCtx, monthLabels, monthData, 'Total Amount', barBgColor, barBorderColor);
                    monthPieChart = updatePieChart(monthPieCtx, monthLabels, monthData, pieBgColor);
                })
                .catch(error => console.error('Error fetching month report:', error));
        }

        function fetchLeastPerformingStore(target, months) {
            fetch('SalesDemo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: new URLSearchParams({
                    target: target,
                    month: months,
                    submit: 'getLeastPerformingStore'
                })
            })
                .then(response => response.json())
                .then(data => {
                    const leastLabels = data.labels;
                    const leastData = data.data;

                    if (leastBarChart) {
                        leastBarChart.destroy();
                    }
                    if (leastPieChart) {
                        leastPieChart.destroy();
                    }

                    leastBarChart = updateBarChart(leastPerformingBarCtx, leastLabels, leastData, 'Least Performing Stores', barBgColor, barBorderColor);
                    leastPieChart = updatePieChart(leastPerformingPieCtx, leastLabels, leastData, pieBgColor);
                })
                .catch(error => console.error('Error fetching least performing stores:', error));
        }
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
                                            callback: function (value) {
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
                                            label: function (context) {
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
                    function updateBarChart(ctx, labels, data, label, bgColor, borderColor) {
                        const chart = Chart.getChart(ctx);

                        if (chart) {
                            chart.data.labels = labels;
                            chart.data.datasets[0].data = data;
                            chart.data.datasets[0].backgroundColor = bgColor;
                            chart.data.datasets[0].borderColor = borderColor;
                            chart.update();
                        } else {
                            BarChart(ctx, labels, data, label, bgColor, borderColor);
                        }
                    }

                    function updatePieChart(ctx, labels, data, bgColor) {
                        const chart = Chart.getChart(ctx);

                        if (chart) {
                            chart.data.labels = labels;
                            chart.data.datasets[0].data = data;
                            chart.data.datasets[0].backgroundColor = bgColor;
                            chart.update();
                        } else {
                            initPieChart(ctx, labels, data, bgColor);
                        }
                    };
                </script>
                </body>
                </html>
