<%-- 
    Document   : report
    Created on : 26 Jul 2024, 10:41:26
    Author     : T440
--%>

<%@page import="ateam.DTO.StorePerfomanceInSales"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="ateam.DTO.TopProductDTO"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Integer"%>
<%@page import="java.util.Map"%>
<%@page import="java.lang.String"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Report</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/reports.css">
        <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        
    </head>
    <body>
        <% List<TopProductDTO> topSellingProducts = (List<TopProductDTO>) request.getSession(false).getAttribute("topSellingProducts");
           Map<String, StorePerfomanceInSales> topAchievingStores = (Map<String, StorePerfomanceInSales>) request.getSession(false).getAttribute("topAchievingStores");
           
           if(topAchievingStores != null){
               StringBuilder storeNames = new StringBuilder();
               StringBuilder storePercent = new StringBuilder();
               for(Map.Entry<String, StorePerfomanceInSales> entry : topAchievingStores.entrySet()){
                   storeNames.append("'").append(entry.getKey()).append("',");
                   storePercent.append(entry.getValue().getPercentageSold()).append(",");
               }
           
        %>
        <div class="holy-grail-grid">
            <header class="header">Header</header>
            <main class="main-content">
                <div class="report">
                    <div>
                        <h4>Top Achieving stores</h4>
                    </div>
                    <div class="graphBox">
                        <div class="box">
                            <canvas id="topAchievingStoreBar"></canvas>
                        </div>
                        <div class="box">
                            <canvas id="topAchievingStoreBie"></canvas>
                        </div>
                    </div>
                </div>
                
                <div class="report" id="storeAchievedTarget" >
                    <div>
                        <h4>Store Achieve the target</h4>
                        <input id="storeAchievedTargetInput" type="month" required>
                        <button>Check</button>
                    </div>
                    <div class="graphBox">
                        <div class="box">
                            <canvas id="storeAchievedTargetBar"></canvas>
                        </div>
                        <div class="box">
                            <canvas id="storeAchievedTargetPie"></canvas>
                        </div>
                    </div>
                </div>
                
                <div class="report" id="leastPerformingStore" >
                    <div>
                        <h4>Least performing store</h4>
                        <label>Interval</label>
                        <select name="interval" id="intervalSelect">
                            <option value="3">3 Months</option>
                            <option value="6">6 Months</option>
                        </select>
                        <button type="submit">Check</button>
                    </div>
                    <div class="graphBox">
                        <div class="box">
                            <canvas id="leastPerfomingStoreBar"></canvas>
                        </div>
                        <div class="box">
                            <canvas id="leastPerformingPie"></canvas>
                        </div>
                    </div>
                </div>
                
                <% if(topSellingProducts != null){%>
                    <div>
                        <div class="header">
                            <h4>Top Selling Products</h4>
                        </div>
                        <table>
                            <thead>
                                <tr>
                                    <td>Product Name</td>
                                    <td>Store Name</td>
                                    <td>Total quantity sold</td>
                                </tr>
                            </thead>
                            <tbody>
                                <% for(TopProductDTO topStore : topSellingProducts){%>
                                <tr>
                                    <td><%=topStore.getProductName()%></td>
                                    <td><%=topStore.getStoreName()%></td>
                                    <td><%=topStore.getTotalQuantitySold()%></td>
                                </tr>
                                    <%}%>
                            </tbody>
                        </table>
                    </div>
                    <%}%>
            </main>
            <section class="left-sidebar">Left sidebar</section>
            <aside class="right-sidebar">
                Reports
            </aside>
            <footer class="footer">Footer</footer>
        </div>
         <script>
            

            document.addEventListener('DOMContentLoaded', function() {
                const ctxBar = document.getElementById('topAchievingStoreBar').getContext('2d');
                const ctxPie = document.getElementById('topAchievingStorePie').getContext('2d');

                // Bar Chart
                const barChart = new Chart(ctxBar, {
                    type: 'bar',
                    data: {
                        labels: [<%=storeNames.toString()%>],
                        datasets: [{
                            label: 'Percentage Sold',
                            data: [<%=storePercent.toString()%>],
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: {
                                    precision: 0
                                }
                            }
                        }
                    }
                });

                // Pie Chart
                const pieChart = new Chart(ctxPie, {
                    type: 'pie',
                    data: {
                        labels: [<%=storeNames.toString()%>],
                        datasets: [{
                            label: 'Percentage Sold',
                            data: [<%= storePercent.toString()%>],
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            borderColor: 'rgba(54, 162, 235, 0.2)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: {
                            legend: {
                                position: 'top'
                            },
                            tooltip: {
                                callbacks: {
                                    label: function(context) {
                                        let label = context.label || '';
                                        if (label) {
                                            label += ': ';
                                        }
                                        if (context.parsed !== null) {
                                            label += context.parsed + '%';
                                        }
                                        return label;
                                    }
                                }
                            }
                        }
                    }
                });
            });
        </script>
        <%}%>
        <script src="${pageContext.request.contextPath}/js/reports.js"></script>
    </body>
</html>
