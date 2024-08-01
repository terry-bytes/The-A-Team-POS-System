<%-- 
    Document   : IBTReceiveDashboard
    Created on : 15 Jul 2024, 3:49:36 PM
    Author     : carme
--%>

<%@page import="ateam.Models.IBT"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>IBT Receive Dashboard Page</title>
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
            font-size: 2.5em;
            text-align: center;
            font-weight: 600;
        }

        h2 {
            color: #333;
            font-size: 1.5em;
            margin-bottom: 20px;
            text-align: center;
            font-weight: 500;
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
            align-items: center;
        }

        .request-card {
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 20px;
            background-color: #ffffff;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            width: 100%;
            max-width: 550px;
        }

        .request-card p {
            margin: 10px 0;
            font-size: 16px;
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
        <h1>IBT Receive Dashboard</h1>
        <form action="IBTServlet" method="post">
            <h2>IBT Requests needed for approval</h2>
            
        </form>
        
        <% 
    List<IBT> stores = (List<IBT>) request.getAttribute("Stores");
    if (stores != null && !stores.isEmpty()) {
        for (IBT i : stores) {
    %>
    <form action="IBTServlet" method="post">
        <div style="border: 1px solid #ccc; padding: 10px; margin-bottom: 10px;">
            <p> IBT ID: <%= i.getRequestID()%> </p>
            <p>Requested Store: <%= i.getRequestedtore() %></p>
            <p>Requested Product ID: <%= i.getProductID() %></p>
            <p>Quantity requested: <%= i.getQuantity() %></p>
            
            <input type="submit" value="Approve" name="IBT_switch">
            <input type="hidden" value="<%= i.getProductID() %>" name="product_id">
            <input type="hidden" value="<%= i.getStoreID() %>" name="store_id"> 
        </div>
    </form>
    <% 
        }
    } else {
    %>
    <p><%= request.getAttribute("message") %></p>
    <p>No requests found.</p>
    <% 
    } 
    %>
    </body>
</html>
