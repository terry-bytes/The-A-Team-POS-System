 v
<%@page import="ateam.Models.InventorySummary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Store Inventory Summary</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f0f0f0;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .filter-form {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .filter-form label {
            display: block;
            margin: 10px 0 5px;
            color: #333;
        }

        .filter-form input[type="text"] {
            
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .filter-form input[type="submit"] {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .filter-form input[type="submit"]:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        table th, table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        table th {
            background-color: #007BFF;
            color: white;
        }

        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        table img {
            display: block;
            margin: auto;
            width: 50px;
            height: 50px;
            border-radius: 4px;
        }

        table a {
            text-decoration: none;
        }
    </style>
</head>
<body>
    <h1>Store Inventory Summary</h1>

    <!-- Filter Form -->
    <div class="filter-form">
        <form action="storeInventorySummary" method="get">
            <label for="color">Filter By Color:</label>
            <input type="text" id="color" name="color" size="5">
            
            <label for="size">Filter By Size:</label>
            <input type="text" id="size" name="size"maxlength="2" size="2">
            
            <label for="store">Filter By Store ID:</label>
            <input type="text" id="store" name="store" size="3">
            
            <label for="sku">Filter By Product SKU:</label>
            <input type="text" id="sku" name="sku">
            
            <input type="submit" value="Filter">
        </form>
    </div>

    <table>
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
            <td><a href="<%= summary.getProductImagePath() %>" target="_blank"><img src="<%= summary.getProductImagePath() %>" alt="Product Image"></a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
   
</body>
</html>
