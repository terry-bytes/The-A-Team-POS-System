<%@page import="java.math.BigDecimal"%>
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
    /* Modal styles */
    /* Step 2: CSS Styling */
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
        .modal {
            display: none; 
            position: fixed; 
            z-index: 1; 
            left: 0; 
            top: 0; 
            width: 100%; 
            height: 100%; 
            overflow: auto; 
            background-color: rgb(0,0,0); 
            background-color: rgba(0,0,0,0.4); 
            padding-top: 60px; 
        }

        .modal-content {
            background-color: #fefefe;
            margin: 5% auto; 
            padding: 20px;
            border: 1px solid #888;
            width: 80%; 
            max-width: 400px; 
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }

        .modal-header,
        .modal-footer {
            padding: 10px;
            background-color: #fefefe;
            border-bottom: 1px solid #ddd;
        }

        .modal-footer {
            border-top: 1px solid #ddd;
            text-align: right;
        }

        .modal-body {
            padding: 10px;
        }

        input[type="password"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            box-sizing: border-box;
        }

        button {
            padding: 10px 20px;
            background-color: #0098f7;
            color: white;
            border: none;
            cursor: pointer;
        }

        button:hover {
            background-color: #ddd;
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
    <%
        BigDecimal change =(BigDecimal)request.getSession(false).getAttribute("change");
        BigDecimal remainingAmount =(BigDecimal)request.getSession(false).getAttribute("remainingAmount");
        %>
    
    <div class="container">
        <h1>Return Sale</h1>

        <!-- Retrieve Sale Section -->
        <form action="ReturnedServlet" method="post">
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
            <div>
            <h3>Items in Sale</h3>
            <table>
                <thead>
                    <tr>
                        <th>Sales Item ID</th>
                        <th>Sale ID</th>
                        <th>Product ID</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                    </tr>
                    
                </thead>
                <tbody>
                    <c:forEach var="item" items="${salesItems}">
                        <tr>
                            <td>${item.sales_item_ID}</td> 
                            <td>${item.sales_ID}</td>
                            <td>${item.product_ID}</td>
                            <td>${item.quantity}</td>
                            <td>${item.unit_price}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <br><br>
            <label class="total-price">Total :${sessionScope.remainingAmount}</label><br>
            <label  class="total-price">Change :R${sessionScope.change}</label>
            <br><br>                   
            </div>
            <!-- Process Return Section -->
            <h3>Process Return</h3>
            <form action="ReturnedServlet" method="post">
                <input type="hidden" name="submit" value="Process-Return">
               
                <label for="">Enter Sales Item ID to Return:</label>
                <input type="number" id="salesItemId" name="salesItemId" required>
                <label for="quantity">Enter Quantity to Return:</label>
                <input type="number" id="quantity" name="quantity" required>
                <label for="email">Customer Email:</label>
                <input type="email" id="email" name="email" required>
                <label for="return_reason">Reason for Return:</label>
                <select type ="text" id="return_reason" name="return_reason" required>
                    <option value="Damaged">Damaged</option>
                    <option value="Wrong Item">Wrong Item/Size</option>
                    <option value="Other">Other</option>
                </select>
                <br><br>
                
               
                
                <button  type="submit"  >Confirm</button>
                
                <br><br>
            
                       
                 
            </form>
            </c:if>
            <br>
            
           <form action="ReturnedServlet" method="post">
    <input type="hidden" name="submit" value="Complete Return">
    <button type="submit">Refund Customer</button>
</form>

<form action="ReturnedServlet" method="post">
    <input type="hidden" name="submit" value="Select New Item">
    <button type="submit">Pick New Item</button>
</form>

                
        
    </div>

                
        
    <!-- Modal Structure -->
    <div id="messageModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <p id="modalMessage"></p>
        </div>
    </div>

     
        

    <script>
        // Handle modals for messages
        var messageModal = document.getElementById("messageModal");
        var messageSpan = document.getElementsByClassName("close")[1];

        messageSpan.onclick = function() {
            messageModal.style.display = "none";
        }

        window.onclick = function(event) {
            if (event.target == messageModal) {
                messageModal.style.display = "none";
            }
        }

        function showModal(message) {
            document.getElementById("modalMessage").innerText = message;
            messageModal.style.display = "block";
        }

        <c:if test="${not empty message}">
            showModal("${message}");
        </c:if>

        <c:if test="${not empty errorMessage}">
            showModal("${errorMessage}");
        </c:if>
    </script>
    

</body>
</html>
