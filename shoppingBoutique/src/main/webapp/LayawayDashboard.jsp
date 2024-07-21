<%-- 
    Document   : LayawayDashboard
    Created on : 18 Jul 2024, 10:01:59 AM
    Author     : carme
--%>

<%@page import="ateam.Models.Layaway"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Layaway Dashboard Page</title>
        <style>
        body {
            font-family: Arial, sans-serif;
            line-height: 1.6;
            margin: 20px;
            background-color: #f5f5f5;
        }
        h1, h2 {
            color: #333;
        }
        form {
            margin-bottom: 20px;
            padding: 10px;
            background-color: #fff;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        form h2 {
            margin-bottom: 10px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="submit"] {
            padding: 8px;
            width: 200px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 3px;
        }
        input[type="submit"] {
            cursor: pointer;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 3px;
        }
        input[type="submit"]:hover {
            background-color: #45a049;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
            background-color: #fff;
            border: 1px solid #ddd;
        }
        th, td {
            padding: 8px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        tr:nth-child(even) {
            background-color: #f9f9f9;
        }
        p {
            margin: 5px 0;
        }
    </style>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script type="text/javascript">
    $(document).ready(function() {
        $("#addLayawayForm").submit(function(event) {
            event.preventDefault(); // Prevent the form from submitting normally
            
            // Capture current time in JavaScript
            var buttonClickTime = new Date().toISOString();
            
            // Calculate time 10 seconds later
            var tenSecondsLater = new Date();
            tenSecondsLater.setSeconds(tenSecondsLater.getSeconds() + 10);
            var expiryTime = tenSecondsLater.toISOString();
            
            // Send AJAX request to store timestamps in database via LayawayServlet
            $.ajax({
                url: "LayawayServlet",
                type: "POST",
                data: {
                    action: "addLayaway",
                    product_ID: $("#product_ID").val(),
                    product_quantity: $("#product_quantity").val(),
                    customer_email: $("#customer_email").val(),
                    buttonClickTime: buttonClickTime,
                    expiryTime: expiryTime,
                    customer_number: $("#customer_number").val(),
                    customer_name: $("#customer_name").val(),
                    layaway_switch: $("input[name='layaway_switch']").val()  
                },
                success: function(response) {
                    console.log("Layaway added successfully");
                    // Optionally handle success response
                },
                error: function(xhr, status, error) {
                    console.error("Error adding layaway: " + error);
                    // Optionally handle error
                }
            });
        });
    });
</script>
    </head>
    <body>
        <h1>Choose an Option</h1>
        
         <%-- Display messages if they exist --%>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
        %>
            <p><%= message %></p>
        <% } %>
        
        <form id="addLayawayForm" action="LayawayServlet" method="post">
            <h2>Add a Layaway</h2>
            <label>Enter the Product ID:</label>
            <input type="text" id="product_ID" name="product_ID"><br><br>
            <label>Enter the quantity:</label>
            <input type="text" id="product_quantity" name="product_quantity"><br><br>
            <label>Enter customer name</label>
            <input type="text" id="customer_name" name="customer_name"><br><br>
            <label>Enter Customer's Email</label>
            <input type="text" id="customer_email" name="customer_email"><br><br>
            <label>Enter Customer's Cell Phone Number</label>
            <input type="text" id="customer_number" name="customer_number"><br><br>
            <input type="hidden" name="layaway_switch" value="Add Layaway">
            <input type="submit" value="Add Layaway"><br><br>
        </form> 
            
        <!-- Form for searching a layaway by ID -->
    <form action="LayawayServlet" method="post">
        <h2>Search for Layaway via Layaway ID</h2>
        <label>Enter the Layaway ID:</label>
        <input type="text" name="layaway_ID"><br><br>
        <input type="submit" value="View Layaway" name="layaway_switch"><br><br>
        
        <!-- Display searched layaway data in a table -->
        <% Layaway layawayID = (Layaway) request.getAttribute("LayawaySearch"); %>
        <% if (layawayID != null) { %>
        <table border="1">
            <thead>
                <tr>
                    <th>Layaway ID</th>
                    <th>Employee ID</th>
                    <th>Start Date</th>
                    <th>Expiry Date</th>
                    <th>Status</th>
                    <th>Customer Email</th>
                    <th>Product ID</th>
                    <th>Product Quantity</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td><%= layawayID.getLayaway_ID() %></td>
                    <td><%= layawayID.getEmployee_ID() %></td>
                    <td><%= layawayID.getStart_date() %></td>
                    <td><%= layawayID.getExpiry_date() %></td>
                    <td><%= layawayID.getLayaway_status() %></td>
                    <td><%= layawayID.getCustomerEmail() %></td>
                    <td><%= layawayID.getProductID() %></td>
                    <td><%= layawayID.getProductQuantity() %></td>
                </tr>
            </tbody>
        </table>
        <% } else { %>
        <p>No layaway found with the given ID.</p>
        <% } %>
    </form>

    <!-- Form for viewing all layaways -->
    <form action="LayawayServlet" method="post">
        <h2>View all Layaways</h2>
        <input type="submit" value="View all Layaways" name="layaway_switch"><br><br>
        
        <!-- Display all layaways in a table -->
        <table border="1">
            <thead>
                <tr>
                    <th>Layaway ID</th>
                    <th>Employee ID</th>
                    <th>Start Date</th>
                    <th>Expiry Date</th>
                    <th>Status</th>
                    <th>Customer Email</th>
                    <th>Product ID</th>
                    <th>Product Quantity</th>
                </tr>
            </thead>
            <tbody>
                <% 
                List<Layaway> layaways = (List<Layaway>) request.getAttribute("Layaways");
                if (layaways != null && !layaways.isEmpty()) {
                    for (Layaway layaway : layaways) {
                %>
                <tr>
                    <td><%= layaway.getLayaway_ID() %></td>
                    <td><%= layaway.getEmployee_ID() %></td>
                    <td><%= layaway.getStart_date() %></td>
                    <td><%= layaway.getExpiry_date() %></td>
                    <td><%= layaway.getLayaway_status() %></td>
                    <td><%= layaway.getCustomerEmail() %></td>
                    <td><%= layaway.getProductID() %></td>
                    <td><%= layaway.getProductQuantity() %></td>
                </tr>
                <% 
                    }
                } else {
                %>
                <tr>
                    <td colspan="8">No layaways found.</td>
                </tr>
                <% 
                }
                %>
            </tbody>
        </table>
    </form>

            
        <form action="LayawayServlet" method="post">
            <h2>Update a Layaway</h2>
            <label>Enter the Layaway ID to update</label>
            <input type="text" name="new_layaway_ID"><br><br>
            <label>Enter the new Product ID</label>
            <input type="text" name="new_product_ID"><br><br>
            <label>Enter the new number of quantity</label>
            <input type="text" name="new_product_quantity"><br><br>
            <label>Enter the customer's new Email Address</label>
            <input type="text" name="new_customer_email"><br><br>
            <label>Enter the customer's new contact number</label>
            <input type="text" name="new_customer_number"><br><br>
            <input type="submit" value="Update Layaway" name="layaway_switch"><br><br>
        </form>
            
        <form action="LayawayServlet" method="post">
            <h2>Delete a Layaway</h2>
            <label>Enter Layaway ID</label>
            <input type="text" name="drop_layaway_ID"><br><br>
            <input type="submit" value="Delete Layaway" name="layaway_switch"><br><br>
        </form>
    </body>
</html>
