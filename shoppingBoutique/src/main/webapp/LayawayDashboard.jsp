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
    margin: 0;
    padding: 0;
    background-color: #aecdf0; /* Updated background color */
    display: flex;
}

.sidebar {
    width: 250px;
    background-color: #aecdf0; /* Updated background color */
    color: #fff;
    height: 100vh;
    position: fixed;
    top: 0;
    left: 0;
    padding-top: 20px;
}

.sidebar h2 {
    color: #fff;
    padding-left: 20px;
}

.sidebar a {
    display: block;
    color: #fff;
    padding: 10px 20px;
    text-decoration: none;
    font-size: 16px;
    border-radius: 3px; /* Rounded corners for links */
}

.sidebar a:hover {
    background-color: #0056b3; /* Darker blue for hover effect */
}

.main-content {
    margin-left: 250px;
    padding: 20px;
    width: calc(100% - 250px);
}

form {
    margin-bottom: 20px;
    padding: 20px;
    background-color: rgba(255, 255, 255, 0.9); /* Less transparent background for form */
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
    background-color: #007bff; /* Blue background for buttons */
    color: white;
    border: none;
}

input[type="submit"]:hover {
    background-color: #0056b3; /* Darker blue for button hover */
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

                var buttonClickTime = new Date().toISOString();
                var tenSecondsLater = new Date();
                tenSecondsLater.setSeconds(tenSecondsLater.getSeconds() + 10);
                var expiryTime = tenSecondsLater.toISOString();

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
    <!-- Sidebar -->
    <div class="sidebar">
        <a href="tellerDashboard.jsp">
        <h2>Layaway Dashboard</h2>
        </a>
        <a href="#addLayaway">Add Layaway</a>
        <a href="#searchLayaway">Search Layaway</a>
        <a href="#viewAllLayaways">View All Layaways</a>
        <a href="#updateLayaway">Update Layaway</a>
        <a href="#deleteLayaway">Delete Layaway</a>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <%-- Display messages if they exist --%>
        <%
            String message = (String) request.getAttribute("message");
            if (message != null && !message.isEmpty()) {
        %>
            <p><%= message %></p>
        <% } %>

        <!-- Add Layaway Form -->
        <div id="addLayaway" class="form-section">
            <form id="addLayawayForm" action="LayawayServlet" method="post">
                <h2>Add a Layaway</h2>
                <label>Product ID:</label>
                <input type="text" id="product_ID" name="product_ID"><br><br>
                <label>Quantity:</label>
                <input type="text" id="product_quantity" name="product_quantity"><br><br>
                <label>Customer Name:</label>
                <input type="text" id="customer_name" name="customer_name"><br><br>
                <label>Customer Email:</label>
                <input type="text" id="customer_email" name="customer_email"><br><br>
                <label>Customer Phone Number:</label>
                <input type="text" id="customer_number" name="customer_number"><br><br>
                <input type="hidden" name="layaway_switch" value="Add Layaway">
                <input type="submit" value="Add Layaway"><br><br>
            </form>
        </div>

        <!-- Search Layaway Form -->
        <div id="searchLayaway" class="form-section">
            <form action="LayawayServlet" method="post">
                <h2>Search for Layaway by ID</h2>
                <label>Layaway ID:</label>
                <input type="text" name="layaway_ID"><br><br>
                <input type="submit" value="Search Layaway" name="layaway_switch"><br><br>

                <% Layaway layawayID = (Layaway) request.getAttribute("LayawaySearch"); %>
                <% if (layawayID != null) { %>
                <table>
                    <thead>
                        <tr>
                            <th>Layaway ID</th>
                            <th>Employee ID</th>
                            <th>Start Date</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th>Customer Email</th>
                            <th>Product ID</th>
                            
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
                            
                        </tr>
                    </tbody>
                </table>
                <% } else { %>
                <p>No layaway found with the given ID.</p>
                <% } %>
            </form>
        </div>

        <!-- View All Layaways Form -->
        <div id="viewAllLayaways" class="form-section">
            <form action="LayawayServlet" method="post">
                <h2>View All Layaways</h2>
                <input type="submit" value="View all Layaways" name="layaway_switch"><br><br>

                <% List<Layaway> layaways = (List<Layaway>) request.getAttribute("Layaways"); %>
                <table>
                    <thead>
                        <tr>
                            <th>Layaway ID</th>
                            <th>Employee ID</th>
                            <th>Start Date</th>
                            <th>Expiry Date</th>
                            <th>Status</th>
                            <th>Customer Email</th>
                            <th>Product ID</th>
                           
                        </tr>
                    </thead>
                    <tbody>
                        <% if (layaways != null && !layaways.isEmpty()) {
                            for (Layaway layaway : layaways) { %>
                        <tr>
                            <td><%= layaway.getLayaway_ID() %></td>
                            <td><%= layaway.getEmployee_ID() %></td>
                            <td><%= layaway.getStart_date() %></td>
                            <td><%= layaway.getExpiry_date() %></td>
                            <td><%= layaway.getLayaway_status() %></td>
                            <td><%= layaway.getCustomerEmail() %></td>
                            <td><%= layaway.getProductID() %></td>
                            
                        </tr>
                        <% } } else { %>
                        <tr>
                            <td colspan="8">No layaways found.</td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </form>
        </div>

        <!-- Update Layaway Form -->
        <div id="updateLayaway" class="form-section">
            <form action="LayawayServlet" method="post">
                <h2>Update a Layaway</h2>
                <label>Layaway ID:</label>
                <input type="text" name="new_layaway_ID"><br><br>
                <label>New Product ID:</label>
                <input type="text" name="new_product_ID"><br><br>
                <label>New Quantity:</label>
                <input type="text" name="new_product_quantity"><br><br>
                <label>New Customer Email:</label>
                <input type="text" name="new_customer_email"><br><br>
                <label>New Customer Phone Number:</label>
                <input type="text" name="new_customer_number"><br><br>
                <input type="submit" value="Update Layaway" name="layaway_switch"><br><br>
            </form>
        </div>

        <!-- Delete Layaway Form -->
        <div id="deleteLayaway" class="form-section">
            <form action="LayawayServlet" method="post">
                <h2>Delete a Layaway</h2>
                <label>Layaway ID:</label>
                <input type="text" name="drop_layaway_ID"><br><br>
                <input type="submit" value="Delete Layaway" name="layaway_switch"><br><br>
            </form>
        </div>
    </div>
</body>
</html>
