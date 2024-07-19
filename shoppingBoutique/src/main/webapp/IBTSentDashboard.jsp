<%@page import="ateam.Models.IBT"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Send IBT Dashboard Page</title>
    
     <script>
        function validateQuantity(inputField) {
            var max = parseInt(inputField.getAttribute('max'));
            if (parseInt(inputField.value) > max) {
                alert("Quantity cannot exceed " + max);
                inputField.value = max; // Set the input field back to the maximum allowed value
            }
        }
    </script>
</head>
<body>
    <h1>Send IBT Dashboard</h1>
    
    <form action="IBTServlet" method="post">
        <label>Please enter the product ID</label><br><br>
        <input type="text" name="e_product_ID"><br><br>
        <input type="submit" value="Check Stores" name="IBT_switch"><br><br>
    </form>
    
    <% 
    List<IBT> stores = (List<IBT>) request.getAttribute("Stores");
    if (stores != null && !stores.isEmpty()) {
        for (IBT i : stores) {
    %>
    <form action="IBTServlet" method="post">
        <div style="border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;">
            <p>Store ID: <%= i.getStoreID() %></p>
            <p>Product ID: <%= i.getProductID() %></p>
            <p>Product Quantity: <%= i.getQuantity() %></p>
            <label>Please enter the quantity needed  :</label><br><br>
            <input type="text" name="e_product_qautity"  min="1" max="<%= i.getQuantity() %>"  oninput="validateQuantity(this)"><br><br>
            <input type="submit" value="Request IBT" name="IBT_switch">
            <input type="hidden" value="<%= i.getProductID() %>" name="product_id">
            <input type="hidden" value="<%= i.getStoreID() %>" name="store_id"> 
        </div>
    </form>
    <% 
        }
    } else {
    %>
    <p><%= request.getAttribute("message") %></p>
    <p>No stores found.</p>
    <% 
    } 
    %>
</body>
</html>
