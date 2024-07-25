<%-- 
    Document   : allInventories
    Created on : 25 Jul 2024, 16:38:08
    Author     : user
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Inventory View</h1>
    <p>${message}</p>
    <table border="1">
        <thead>
            <tr>
                <th>Inventory ID</th>
                <th>Product ID</th>
                <th>Store ID</th>
                <th>Previous Quantity</th>
                <th>Quantity</th>
                <th>Reorder Point</th>
                <th>Last Updated</th>
                <th>Updated By Employee ID</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="inventory" items="${inventoryList}">
                <tr>
                    <td>${inventory.inventory_ID}</td>
                    <td>${inventory.product_ID}</td>
                    <td>${inventory.store_ID}</td>
                    <td>${inventory.previous_quantity }</td>
                    <td>${inventory.inventory_quantity}</td>
                    <td>${inventory.reorder_point}</td>
                    <td>${inventory.last_updated}</td>
                    <td>${inventory.updated_by_employee_ID}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    </body>
</html>
