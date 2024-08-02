<%@page import="ateam.Models.Employee"%>
<%@ page import="java.util.List" %>
<%@ page import="ateam.Models.ProductVariants" %>


<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Variants</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); 
        if (employee != null){
    %>
    <h1>Search Product Variants</h1>

    <form action="productVariants" method="get">
        <input type="hidden" name="action" value="search">
        <label for="productSKU">Product SKU:</label>
        <input type="text" id="productSKU" name="productSKU">
        <label for="size">Size:</label>
        <input type="text" id="size" name="size">
        <label for="color">Color:</label>
        <input type="text" id="color" name="color">
        <label for="storeID">Store ID:</label>
        <input type="number" id="storeID" name="storeID">
        <button type="submit">Search</button>
    </form>

    <h2>Product Variants List</h2>
    <table border="1">
        <thead>
            <tr>
                <th>Variant ID</th>
                <th>Product SKU</th>
                <th>Size</th>
                <th>Color</th>
                <th>Store ID</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<ProductVariants> variants = (List<ProductVariants>) session.getAttribute("variants");
                if (variants != null && !variants.isEmpty()) {
                    for (ProductVariants variant : variants) {
            %>
                        <tr>
                            <td><%= variant.getVariant_ID() %></td>
                            <td><%= variant.getProduct_SKU() %></td>
                            <td><%= variant.getSize() %></td>
                            <td><%= variant.getColor() %></td>
                            <td><%= variant.getStore_ID() %></td>
                        </tr>
            <%      }
                } else { %>
                    <tr>
                        <td colspan="5">No product variants found.</td>
                    </tr>
            <%    } %>
        </tbody>
    </table>
            <%} else {%> <jsp:include page="unauthorized.jsp"></jsp:include> <%}%>
    
</body>
</html>
