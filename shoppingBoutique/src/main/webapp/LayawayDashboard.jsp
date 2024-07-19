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
                    buttonClickTime: buttonClickTime,
                    expiryTime: expiryTime,
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
        <form id="addLayawayForm" action="LayawayServlet" method="post">
            <h2>Add a Layaway</h2>
            <label>Enter the Product ID:</label>
            <input type="text" id="product_ID" name="product_ID"><br><br>
            <label>Enter the quantity:</label>
            <input type="text" id="product_quantity" name="product_quantity"><br><br>
            <label>Enter Customer's Email</label>
            <input type="text" id="customer_email" name="customer_email"><br><br>
            <label>Enter Customer's Cell Phone Number</label>
            <input type="text" id="customer_number" name="customer_number"><br><br>
            <input type="hidden" name="layaway_switch" value="Add Layaway">
            <input type="submit" value="Add Layaway"><br><br>
        </form> 
            
        <form action="LayawayServlet" method="post">
            <h2>Search for Layaway via Layaway ID</h2>
            <label>Enter the Layaway ID:</label>
            <input type="text" name="layaway_ID"><br><br>
            <input type="submit" value="View Layaway" name="layaway_switch"><br><br>
            <% Layaway layawayID = (Layaway)request.getAttribute("LayawaySearch");
           if(layawayID != null) {
            %>
            <p>Layaway ID: <%=layawayID.getLayaway_ID()%></p>
            <p>Customer Email: <%=layawayID.getCustomerEmail()%></p>
            <p>Employee ID: <%=layawayID.getEmployee_ID()%></p
            <p>Start Date: <%=layawayID.getStart_date()%></p>
            <p>Expiray Date: <%=layawayID.getExpiry_date()%></p>
            <p>Layaway Status: <%=layawayID.getLayaway_status()%></p>
            <p>Product ID: <%=layawayID.getProductID()%></p>
            <p>Product Quantity: <%=layawayID.getProductQuantity()%></p>
            <%}%>
        </form>
            
            <form action="LayawayServlet" method="post">
            <h2>View all Layaways</h2>
            <input type="submit" value="View all Layaways" name="layaway_switch"><br><br>
            <ul>
            <% 
                List<Layaway> layaways = (List<Layaway>) request.getAttribute("Layaways");
                if (layaways != null && !layaways.isEmpty()) {
                    for (Layaway layaway : layaways) {
            %>
                        <li>
                            Layaway ID: <%= layaway.getLayaway_ID() %><br>
                            Employee ID: <%= layaway.getEmployee_ID() %><br>
                            Start Date: <%= layaway.getStart_date() %><br>
                            Expiry Date: <%= layaway.getExpiry_date() %><br>
                            Status: <%= layaway.getLayaway_status() %><br>
                            Customer Email: <%= layaway.getCustomerEmail() %><br>
                            Contact: <%= layaway.getCustomerNumber() %><br>
                            Product ID: <%= layaway.getProductID() %><br>
                            Product Quantity: <%= layaway.getProductQuantity() %><br>
                        </li>
            <% 
                    }
                } else {
            %>
                    <li>No layaways found.</li>
            <% 
                }
            %>
        </ul>
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
