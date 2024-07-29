<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sale Receipt</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <h1>Sale Receipt</h1>

    <c:if test="${not empty errorMessage}">
        <div class="error">
            <p>${errorMessage}</p>
        </div>
    </c:if>

    <c:if test="${empty errorMessage}">
        <table>
            <thead>
                <tr>
                    <th>Item</th>
                    <th>SKU</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${scannedItems}">
                    <tr>
                        <td>${item.product_name}</td>
                        <td>${item.product_SKU}</td>
                        <td>${item.product_price}</td>
                        <td>${item.scanCount}</td>
                        <td>${item.product_price * item.scanCount}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><strong>Total Amount (Excl. VAT):</strong> ${totalAmount.subtract(vatAmount)}</p>
        <p><strong>VAT (15%):</strong> ${vatAmount}</p>
        <p><strong>Total Amount (Incl. VAT):</strong> ${totalAmount}</p>
        <p><strong>Cash Paid:</strong> ${cashPaid}</p>
        <p><strong>Change:</strong> ${change}</p>

    <h2>Thank you for your purchase!</h2>
    </c:if>
</body>
</html>
