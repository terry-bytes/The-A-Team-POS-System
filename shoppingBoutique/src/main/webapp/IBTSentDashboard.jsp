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
        
        /*new popup script*/
         function validateQuantity(inputField) {
            var max = parseInt(inputField.getAttribute('max'));
            if (parseInt(inputField.value) > max) {
                alert("Quantity cannot exceed " + max);
                inputField.value = max; // Set the input field back to the maximum allowed value
            }
        }

        function showPopup() {
            document.getElementById('popup-form').style.display = 'flex'; // Show popup
        }

        function closePopup() {
            document.getElementById('popup-form').style.display = 'none'; // Hide popup
        }
    </script>
    
      <!-- Include external CSS -->
    <style>
        /* General Body Styling */
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f9;
            color: #333;
            margin: 0;
            padding: 0;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        h1 {
            color: #0056b3;
            margin-top: 30px;
            font-size: 2em;
            text-align: center;
            font-weight: 600;
        }

        /* Form Styling */
        form {
            background: #ffffff;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin: 20px;
            width: 80%;
            max-width: 600px;
            display: flex;
            flex-direction: column;
        }

        label {
            font-size: 16px;
            color: #333;
            margin-bottom: 8px;
        }

        input[type="text"] {
            width: calc(100% - 22px); /* Adjust for padding and border */
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 16px;
            margin-bottom: 15px;
            box-sizing: border-box; /* Includes padding and border in the width */
        }

        input[type="submit"] {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px;
            font-size: 16px;
            border-radius: 6px;
            cursor: pointer;
            transition: background-color 0.3s, transform 0.2s;
            margin-top: 10px;
        }

        input[type="submit"]:hover {
            background-color: #0056b3;
            transform: scale(1.05);
        }

        input[type="submit"]:active {
            background-color: #003d7a;
            transform: scale(1);
        }

        /* Card Styling for Store Information */
        .store-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            background-color: #ffffff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        .store-card p {
            margin: 5px 0;
        }

        .store-card input[type="text"] {
            width: calc(100% - 22px);
            margin-bottom: 10px;
        }

        /* Message Styling */
        .message {
            color: #ff0000;
            font-size: 16px;
            font-weight: 500;
            text-align: center;
            margin: 20px;
        }
    </style>

</head>
<body>
    <a href='IBTMainDashboard.jsp'>
    <h1>Send IBT Dashboard</h1>
    </a>
    
    <form action="IBTServlet" method="post">
        <label>Please enter the product ID</label><br><br>
        <input type="text" name="e_product_ID" required><br><br>
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
            <label>Please enter Customer name</label><br><br>
            <input type="text" name="e_customer_name"><br><br>
            <label>Please enter Customer cell phone number</label><br><br>
            <input type="text" name="e_customer_number" value="+27"><br><br>
            <label>Please enter Customer email</label><br><br>
            <input type="text" name="e_customer_email"><br><br>
            <input type="submit" value="Request IBT" name="IBT_switch" onclick="showPopup(); return false;">
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
