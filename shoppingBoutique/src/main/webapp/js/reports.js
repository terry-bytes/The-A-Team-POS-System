/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

// reports.js

document.addEventListener('DOMContentLoaded', function() {
    const ctx = document.getElementById('topAchievingStoreBar').getContext('2d');

    // Sample data; replace with dynamic data from the server
    const storeNames = window.storeNames;
    const storePercentages = window.storePercentages;

    
    console.log(storeNames);
    console.log(storePercentages);
    const chart = new Chart(ctx, {
        type: 'bar',
        data: {
            labels: storeNames,
            datasets: [{
                label: 'Percentage Sold',
                data: storePercentages,
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
    
   
    const barCtx = document.getElementById('storeAchievedTargetBar').getContext('2d');
    const pieCtx = document.getElementById('storeAchievedTargetPie').getContext('2d');

    const barChart = new Chart(barCtx, {
        type: 'bar',
        data: {
            labels: [],
            datasets: [{
                label: 'Percentage Sold',
                data: [],
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

    const pieChart = new Chart(pieCtx, {
        type: 'pie',
        data: {
            labels: [],
            datasets: [{
                label: 'Percentage Sold',
                data: [],
                backgroundColor: [],
                borderColor: [],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'top',
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

    document.querySelector('#storeAchievedTarget button').addEventListener('click', function() {
        const inputDate = document.getElementById('storeAchievedTargetInput').value;
        
        
        if (!inputDate) {
            alert('Please select a date');
            return;
        }

        console.log(inputDate);
        fetch('SalesDemo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({submit: 'storeAchievedTarget', date: inputDate})
        })
        .then(response => response.json())
        .then(data => {
            const storeNames = data.labels;
            const storePercentages = data.data;

            console.log("store achieve target: "+ storeNames);
            // Update Bar Chart
            barChart.data.labels = storeNames;
            barChart.data.datasets[0].data = storePercentages;
            barChart.update();

            // Update Pie Chart
            pieChart.data.labels = storeNames;
            pieChart.data.datasets[0].data = storePercentages;
            pieChart.data.datasets[0].backgroundColor = storeNames.map((_, idx) => `rgba(${54 + idx * 20}, 162, 235, 0.2)`);
            pieChart.data.datasets[0].borderColor = storeNames.map((_, idx) => `rgba(${54 + idx * 20}, 162, 235, 1)`);
            pieChart.update();
        })
        .catch(error => console.error('Error fetching data:', error));
    });
    
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


