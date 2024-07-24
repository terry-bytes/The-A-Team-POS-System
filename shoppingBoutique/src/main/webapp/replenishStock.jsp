<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Inventory"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <title>Add Stock</title>
    <style>
        body {
            font-family: 'Open Sans', sans-serif;
            color: #333;
        }
        .container {
            padding: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="number"] {
            width: 100%;
            padding: 8px;
            margin: 0;
            box-sizing: border-box;
        }
        button {
            background: #3498db;
            color: white;
            padding: 10px 15px;
            border: none;
            cursor: pointer;
            border-radius: 4px;
        }
        button:hover {
            background: #2980b9;
        }
        .message {
            margin: 15px 0;
            padding: 10px;
            border: 1px solid #ddd;
            background: #f9f9f9;
            border-radius: 4px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
        }
        th {
            background: #f5f5f5;
        }
    </style>
</head>
<body>
    
<%  
    Employee employee= (Employee)request.getSession(false).getAttribute("Employee");
    List<Product> product = (List<Product>)request.getSession(false).getAttribute("foundProducts");
%>
    <div class="container">
        <h1>Add Stock</h1>
        
            <form action="InventoryServlet" method="post">
                <label for="barcode">Barcode (SKU-Size-Color):</label>
                <input type="text" id="barcode" name="barcode" required>
                <label for="employeeId">Employee ID:</label>
                <input type="number" id ="employeeId" name="employeeId" value="<%=employee.getEmployee_ID()%>" readonly><br><br>
                <label for="productId">Product ID:</label>
                <input type="number" id="productId" name="productId" required><br><br>
                <label for="additionalStock">Additional Stock:</label>
                <input type="number" id="additionalStock" name="additionalStock" required><br><br>
                <label for="storeId">Store ID:</label>
                <input type="number" id="storeId" name="storeId" value="<%=employee.getStore_ID()%>" required readonly><br><br>
                <input type="submit" value="Replenish Stock">
            </form>


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
    </div>
</body>
</html>
