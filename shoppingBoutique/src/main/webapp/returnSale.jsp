<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Return Sale</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <style>
    /* Basic reset for body */
    body {
        font-family: Arial, sans-serif;
        line-height: 1.6;
        margin: 0;
        padding: 0;
        background-color: #f4f4f4;
    }

    /* Container styles */
    .container {
        width: 80%;
        margin: 0 auto;
        background: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }

    h1 {
        color: #333;
        text-align: center;
        margin-bottom: 20px;
    }

    h2, h3 {
        color: #444;
        margin-top: 20px;
        border-bottom: 2px solid #eee;
        padding-bottom: 5px;
    }

    /* Form styles */
    form {
        margin-bottom: 20px;
    }

    label {
        display: block;
        margin: 10px 0 5px;
        font-weight: bold;
    }

    
    input[type="text"],
    input[type="email"],
    select {
        width: 180px;
        padding: 8px;
        margin-bottom: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
    }
    
    input[type="number"]{
        width: 50px;
        padding: 8px;
        margin-bottom: 10px;
        border: 1px solid #ddd;
        border-radius: 4px;
        box-sizing: border-box;
    }
    button {
        background-color: #007bff;
        color: #fff;
        border: none;
        padding: 10px 15px;
        border-radius: 4px;
        cursor: pointer;
        font-size: 16px;
        font-weight: bold;
    }

    button:hover {
        background-color: #0056b3;
    }

    /* Table styles */
    table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
    }

    th, td {
        padding: 10px;
        border: 1px solid #ddd;
        text-align: left;
    }

    th {
        background-color: #f4f4f4;
        color: #333;
    }

    tr:nth-child(even) {
        background-color: #f9f9f9;
    }

    /* Modal styles */
    .modal {
        display: none; 
        position: fixed; 
        z-index: 1; 
        padding-top: 60px; 
        left: 0;
        top: 0;
        width: 100%; 
        height: 100%; 
        overflow: auto; 
        background-color: rgba(0,0,0,0.5); 
    }

    .modal-content {
        background-color: #fff;
        margin: 5% auto; 
        padding: 20px;
        border: 1px solid #888;
        width: 80%; 
        border-radius: 8px;
        box-shadow: 0 0 15px rgba(0,0,0,0.2);
    }

    .close {
        color: #aaa;
        float: right;
        font-size: 28px;
        font-weight: bold;
    }

    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
        cursor: pointer;
    }

    /* Responsive design */
    @media (max-width: 768px) {
        .container {
            width: 95%;
        }

        .modal-content {
            width: 90%;
        }
    }

    </style>
</head>
<body>
    <div class="container">
        <h1>Return Sale</h1>

        <!-- Retrieve Sale Section -->
        <form action="ReturnServlet" method="post">
            <input type="hidden" name="submit" value="Retrieve-Sale">
            <label for="sales_ID">Enter Sales ID:</label>
            <input type="number" id="sales_ID" name="sales_ID" required>
            <button type="submit">Retrieve Sale</button>
        </form>

        <!-- Display Sale and Sales Items if available -->
        <c:if test="${not empty sale}">
            <h2>Sale Details</h2>
            <p>Store Id:${sale.store_ID}
            <p>Sale ID: ${sale.sales_ID}</p>
            <p>Date: ${sale.sales_date}</p>
            <p>Total Amount: ${sale.total_amount}</p>
            <p>Payment Method: ${sale.payment_method}</p>
            <p>Teller :${sale.employee_ID}</p>

            <h3>Items in Sale</h3>
            <table>
                <thead>
                    <tr>
                        <th>Sale ID</th>
                        <th>Product ID</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                    </tr>
                    
                </thead>
                <tbody>
                    <c:forEach var="item" items="${salesItems}">
                        <tr>
                            <td>${item.sales_ID}</td>
                            <td>${item.product_ID}</td>
                            <td>${item.quantity}</td>
                            <td>${item.unit_price}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <!-- Process Return Section -->
            <h3>Process Return</h3>
            <form action="ReturnServlet" method="post">
                <input type="hidden" name="submit" value="Process-Return">
                <input type="hidden" name="sales_ID" value="${sale.sales_ID}">
                <label for="product_SKU">Enter Product SKU to Return:</label>
                <input type="text" id="product_SKU" name="product_SKU" required>
                <label for="quantity">Enter Quantity to Return:</label>
                <input type="number" id="quantity" name="quantity" required>
                <label for="email">Customer Email:</label>
                <input type="email" id="email" name="email" required>
                <label for="return_reason">Reason for Return:</label>
                <select id="return_reason" name="return_reason" required>
                    <option value="Defective">Damaged</option>
                    <option value="Wrong Item">Wrong Item/Size</option>
                    <option value="Other">Other</option>
                </select>
                <button type="submit">Process Return</button>
            </form>
        </c:if>

        <!-- Handle Customer Choice Section -->
        <c:if test="${not empty remainingAmount}">
            <h3>Handle Customer Choice</h3>
            <form action="ReturnServlet" method="post">
                <input type="hidden" name="submit" value="Handle-Customer-Choice">
                <input type="hidden" name="sales_ID" value="${sale.sales_ID}">
                <input type="hidden" name="remainingAmount" value="${remainingAmount}">
                <label for="customer_choice">Customer Choice:</label>
                <select id="customer_choice" name="customer_choice" required>
                    <option value="Select-New-Item">Select New Item</option>
                    <option value="Receive-Change">Receive Change</option>
                </select>
                <button type="submit">Submit Choice</button>
            </form>
        </c:if>

        <!-- Confirm Product Selection Section -->
        <c:if test="${not empty availableProducts}">
            <h3>Select a New Product</h3>
            <form action="ReturnServlet" method="post">
                <input type="hidden" name="submit" value="Confirm-Product-Selection">
                <input type="hidden" name="sales_ID" value="${sale.sales_ID}">
                <input type="hidden" name="remainingAmount" value="${remainingAmount}">
                <label for="selectedProduct">Select Product:</label>
                <select id="selectedProduct" name="selectedProduct" required>
                    <c:forEach var="product" items="${availableProducts}">
                        <option value="${product.product_ID}">${product.product_name} - ${product.product_price}</option>
                    </c:forEach>
                </select>
                <button type="submit">Confirm Selection</button>
            </form>
        </c:if>
    </div>

    <!-- Modal Structure -->
    <div id="messageModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <p id="modalMessage"></p>
        </div>
    </div>

    <script>
        // Get the modal
        var modal = document.getElementById("messageModal");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName("close")[0];

        // When the user clicks on <span> (x), close the modal
        span.onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        // Function to show modal with a message
        function showModal(message) {
            document.getElementById("modalMessage").innerText = message;
            modal.style.display = "block";
        }

        // Display message in modal if available
        <c:if test="${not empty message}">
            showModal("${message}");
        </c:if>

        <c:if test="${not empty errorMessage}">
            showModal("${errorMessage}");
        </c:if>
    </script>
</body>
</html>
