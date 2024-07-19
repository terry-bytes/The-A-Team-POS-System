<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Inventory"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <title>Replenish Stock</title>
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
        <h1>Replenish Stock</h1>
        <form action="ReplenishStockServlet" method="post">
            <div class="form-group">
                <label for="product_ID">Product ID</label>
                <input type="number" id="product_ID" name="product_ID"  required>
            </div>
            <div class="form-group">
                <label for="store_ID">Store ID</label>
                <input type="number" id="store_ID" name="store_ID" value ="<%=employee.getStore_ID()%>" readonly  required>
            </div>
            <div class="form-group">
                <label for="quantity">Quantity to Add</label>
                <input type="number" id="quantity" name="quantity" placeholder ="0"  required>
            </div>
            <div class="form-group">
                <label for="employee_ID">Employee ID</label>
                <input type="number" id="employee_ID" name="employee_ID" value="<%=employee.getEmployee_ID()%>" readonly  required>
            </div>
            <button type="submit">Replenish Stock</button>
        </form>

        <c:if test="${not empty message}">
            <div class="message">
                <c:out value="${message}"/>
            </div>
        </c:if>

        <c:if test="${not empty inventoryList}">
            <h2>Updated Inventory</h2>
            <table>
                <thead>
                    <tr>
                        <th>Inventory ID</th>
                        <th>Product ID</th>
                        <th>Store ID</th>
                        <th>Quantity</th>
                        <th>Reorder Point</th>
                        <th>Last Updated</th>
                        <th>Added by Employee ID</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="inventory" items="${inventoryList}">
                        <tr>
                            <td>${inventory.inventory_ID}</td>
                            <td>${inventory.product_ID}</td>
                            <td>${inventory.store_ID}</td>
                            <td>${inventory.inventory_quantity}</td>
                            <td>${inventory.reorder_point}</td>
                            <td><fmt:formatDate value="${inventory.last_updated}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td>${inventory.added_by_employee_ID}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </div>
</body>
</html>
