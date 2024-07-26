<%@page import="ateam.Models.Inventory"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>All Inventories</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            display: flex;
            flex-direction: column;
            height: 100vh;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 800px;
            width: 100%;
            margin: auto;
        }
        h1 {
            text-align: center;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ccc;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
        .sidebar img {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .sidebar a {
            font: 100% bolder;
            text-decoration: none; /* Remove underlining */
            font-size: 25px;
            color: black;
            display: block;
            
        }
    </style>
    
    <div id="mySidenav" class="sidebar">
    <a href="replenishStock.jsp" >
        <img src="Icons/back.png" alt="Icon 1"> Back
    </a><br><br>
    </div>
</head>
<body>
    <div class="container">
        <h1>All Inventory Logs</h1>
        <% 
            List<Inventory> inventoryList = (List<Inventory>) request.getAttribute("inventoryList");
            if (inventoryList == null || inventoryList.isEmpty()) { 
        %>
            <p>No Inventory available</p>
        <% } else { %>
            <table>
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
                            <td>${inventory.previous_quantity}</td>
                            <td>${inventory.inventory_quantity}</td>
                            <td>${inventory.reorder_point}</td>
                            <td><fmt:formatDate value="${inventory.last_updated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${inventory.updated_by_employee_ID}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        <% } %>
    </div>
</body>
</html>
