<%@page import="ateam.Models.Product"%>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Scanned Items</title>
</head>
<body>
  <h1>Scanned Items</h1>
  <ul id="scannedItems">
    <% 
      Product product = (Product) request.getAttribute("product");
      if (product != null) {
    %>
      <li>SKU: <%= product.getProduct_SKU() %>, Name: <%= product.getProduct_name() %>, Price: <%= product.getProduct_price() %>, Quantity: <%= product.getQuantity_in_stock() %></li>
    <% 
      } else {
    %>
      <li>No product scanned.</li>
    <% 
      } 
    %>
  </ul>
  <a href="barcodeScanner.jsp">Back to Scanner</a>
</body>
</html>
