/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

document.addEventListener('DOMContentLoaded', function() {
    var button = document.getElementById('getTopEmployeeButton');
    
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
    } else {
        console.error('Button not found.');
    }
});
