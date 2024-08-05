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
    body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 0;
        background-color: #72c2f1;
    }

    .container {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        min-height: 100vh;
        padding: 20px;
        background-color: #f4f4f4;
    }

    .content {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 0 10px rgba(0,0,0,0.1);
        max-width: 800px;
        width: 100%;
    }

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
        background-color: rgba(0,0,0,0.4); 
        padding-top: 60px; 
    }

    .modal-content {
        background-color: #bfd9e0;
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
        <div class="content">
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
                <div class="sale-details">
                    <p>Store Id: ${sale.store_ID}</p>
                    <p>Sale ID: ${sale.sales_ID}</p>
                    <p>Date: ${sale.sales_date}</p>
                    <p>Total Amount: ${sale.total_amount}</p>
                    <p>Payment Method: ${sale.payment_method}</p>
                    <p>Teller : ${sale.employee_ID}</p>
                </div>
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
                    <label class="total-price">Total: R${sessionScope.remainingAmount}</label><br>
                    <label class="total-price">Change: R${sessionScope.change}</label>
                    <br><br>
                </div>
                <!-- Process Return Section -->
                <h3>Process Return</h3>
                <form action="ReturnedServlet" method="post">
                    <input type="hidden" name="submit" value="Process-Return">
                    <label for="">Enter Sales Item ID to Return:</label>
                    <input type="number" id="salesItemId" name="salesItemId" required>
                    <label for="quantity">Enter Quantity to Return:</label>
                    <input type="number" id="quantity" name="quantity" required><br>
                    <label for="email">Customer Email:</label>
                    <input type="email" id="email" name="email" required>
                    <label for="return_reason">Reason for Return:</label>
                    <select id="return_reason" name="return_reason" required>
                        <option value="Damaged">Damaged</option>
                        <option value="Wrong Item/Size">Wrong Size</option>
                        <option value="Other">Other</option>
                    </select>
                    <br><br>
                    <button type="submit">Confirm</button>
                </form>
            </c:if>
            <br>
            <br>
            <form action="ReturnedServlet" method="post">
                <input type="hidden" name="submit" value="Complete Return">
                <button type="submit" id="completeReturn">Refund Customer</button>
            </form>
            <br>       
            <form action="ReturnedServlet" method="post">
                <input type="hidden" name="submit" value="Select New Item">
                <button type="submit">Pick New Item</button>
            </form>
        </div>
    </div>

    <!-- Modal Structure -->
    <div id="returnModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <div class="modal-header">
                <h2>Return Processed</h2>
            </div>
            <div class="modal-body">
                <p>Your return has been successfully processed! Please give the customer R${sessionScope.change}</p>
                <img src="button.png" alt="Success Image" style="width:50px;height:50px;">
            </div>
            <div class="modal-footer">
                <form action="ReturnedServlet" method="post">
                    <input type="hidden" name="submit" value="OK">
                    <button type="submit">OK</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        // Get modal element
        var modal = document.getElementById("returnModal");

        // Get close button
        var closeBtn = document.getElementsByClassName("close")[0];

        // When the user clicks on the button, open the modal
        document.getElementById("completeReturn").onclick = function(event) {
            event.preventDefault(); // Prevent form submission
            modal.style.display = "block";
        }

        // When the user clicks on close button, close the modal
        closeBtn.onclick = function() {
            modal.style.display = "none";
        }

        


        // When the user clicks on close button, close the modal
        closeBtn.onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks on close modal button, close the modal
        closeModalBtn.onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }
    </script>
</body>
</html>
