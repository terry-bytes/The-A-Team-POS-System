/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function() {
    var button = document.getElementById('getTopEmployeeButton');
    var currentSalesbutton = document.getElementById('getStoreCurrentSales');
    if (button) {
        button.addEventListener('click', function() {
            var productId = document.getElementById('topEmployeeBasedOnProduct').value;
            var xhr = new XMLHttpRequest();
            
            xhr.open('POST', 'SalesDemo?submit=getTopSellingEmployeeBasedOnProduct&productId=' + productId, true);
            
            xhr.onload = function() {
                if (xhr.status === 200) {
                    document.getElementById('topEmployeeResult').innerHTML = xhr.responseText;
                } else {
                    console.log('Request failed. Status: ' + xhr.status);
                }
            };
            
            xhr.send();
        });
    } 
    
    if(currentSalesbutton){
        currentSalesbutton.addEventListener('click', function() {
            var storeId = document.getElementById('currentSalesStoreId').value;
            var currentSales = new XMLHttpRequest();
            
            currentSales.open('POST', 'SalesDemo?submit=getCurrentSaleBasedOnStore&storeId='+storeId,true);
            
            currentSales.onload = function() {
                if(currentSales.status === 200) {
                    document.getElementById('storeDailyResult').innerHTML = currentSales.responseText;
                }else {
                    console.log('Request failed. status'+currentSales.status);
                }
            };
            currentSales.send();
        });
    }
 
    // Function to handle form submission for least performing stores
        document.getElementById('intervalForm').addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent the form from submitting normally
            
            // Fetch interval value
            var interval = document.getElementById('intervalSelect').value;
            
            // AJAX request to fetch data from servlet
            fetch('SalesDemo', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: new URLSearchParams({
                            interval: interval,
                            submit: 'getLeastPerformingStore'  // Include an action parameter if needed for the server logic
                        })
                    })
                .then(response => response.json())
                .then(data => {
                    // Assuming data.labels and data.data are arrays of labels and data points
                    drawCharts(data.labels, data.data);
                })
                .catch(error => console.error('Error fetching data:', error));
        });

        // Function to draw charts using Chart.js
        function drawCharts(labels, data) {
            // Bar chart
            var barCtx = document.getElementById('leastPerfomingStoreBar').getContext('2d');
            new Chart(barCtx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Performance',
                        data: data,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
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

            // Pie chart
            var pieCtx = document.getElementById('leastPerformingStorePieChart').getContext('2d');
            new Chart(pieCtx, {
                type: 'pie',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Performance',
                        data: data,
                        backgroundColor: [
                            'rgba(255, 99, 132, 0.2)',
                            'rgba(54, 162, 235, 0.2)',
                            'rgba(255, 206, 86, 0.2)',
                            'rgba(75, 192, 192, 0.2)',
                            'rgba(153, 102, 255, 0.2)'
                        ],
                        borderColor: [
                            'rgba(255, 99, 132, 1)',
                            'rgba(54, 162, 235, 1)',
                            'rgba(255, 206, 86, 1)',
                            'rgba(75, 192, 192, 1)',
                            'rgba(153, 102, 255, 1)'
                        ],
                        borderWidth: 1
                    }]
                }
            });
        }
});
