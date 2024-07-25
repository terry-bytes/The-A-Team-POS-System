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
});
