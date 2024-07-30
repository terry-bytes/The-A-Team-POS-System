<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
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
                        <td>${fn:formatNumber(item.product_price, '0.00')}</td>
                        <td>${item.scanCount}</td>
                        <td>${fn:formatNumber(item.product_price * item.scanCount, '0.00')}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <p><strong>Total Amount (Excl. VAT):</strong> ${fn:formatNumber(totalAmount.subtract(vatAmount), '0.00')}</p>
        <p><strong>VAT (15%):</strong> ${fn:formatNumber(vatAmount, '0.00')}</p>
        <p><strong>Total Amount (Incl. VAT):</strong> ${fn:formatNumber(totalAmount, '0.00')}</p>
        <p><strong>Cash Paid:</strong> ${fn:formatNumber(cashPaid, '0.00')}</p>
        <p><strong>Change:</strong> ${fn:formatNumber(change, '0.00')}</p>

        <h2>Thank you for your purchase!</h2>
    </c:if>
</body>
</html>
