<%-- 
    Document   : inventory
    Created on : Jul 16, 2024, 12:10:25 PM
    Author     : Admin
--%>

<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Inventory</title>
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            
        %>
        <form>
            <p>Please enter the product SKU</p>
            <input name="productSku" type="text" placeholder="Product SKU" /> 
        </form>
        
        <div class="inventory" id="inventory">
            <p>Product name: Summer sleeve</p>
            <p>Current amount in stock: 5</p>
            <div class="input-box">
                <label>Enter amount of new stock</label>
                <input name="newStock" type="number" placeholder="New stock" />
            </div>
        </div>
            
            
        
    </body>
</html>
