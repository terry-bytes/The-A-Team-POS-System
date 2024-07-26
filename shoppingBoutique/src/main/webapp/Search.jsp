<%@page import="ateam.Models.InventorySummary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Store Inventory Summary</title>
</head>
<body>
    <h1>Store Inventory Summary</h1>

    <!-- Filter Form -->
    <form action="storeInventorySummary" method="get">
        <label for="color">Color:</label>
        <input type="text" id="color" name="color"><br><br>
        
        <label for="size">Size:</label>
        <input type="text" id="size" name="size"><br><br>
        
        <label for="store">Store ID:</label>
        <input type="text" id="store" name="store"><br><br>
        
        <label for="sku">Product SKU:</label>
        <input type="text" id="sku" name="sku"><br><br>
        
        <input type="submit" >
    </form>

    <table border="1">
        <tr>
            <th>Store ID</th>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Product SKU</th>
            <th>Color</th>
            <th>Size</th>
            <th>Total Quantity</th>
            <th>Product Image</th>
        </tr>
        <%
            List<InventorySummary> inventorySummaries = (List<InventorySummary>) request.getAttribute("inventorySummaries");
            if (inventorySummaries != null) {
                for (InventorySummary summary : inventorySummaries) {
        %>
        <tr>
            <td><%= summary.getStoreID() %></td>
            <td><%= summary.getProductID() %></td>
            <td><%= summary.getProductName() %></td>
            <td><%= summary.getProductSKU() %></td>
            <td><%= summary.getColor() %></td>
            <td><%= summary.getSize() %></td>
            <td><%= summary.getTotalQuantity() %></td>
            <td><a href="<%= summary.getProductImagePath() %>" target="_blank"><img src="<%= summary.getProductImagePath() %>" alt="Product Image" width="50" height="50"></a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
