<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Confirm Item Removal</title>
</head>
<body>
    <h2>Confirm Item Removal</h2>
    
    <form action="ProductServlet" method="post">
        <input type="hidden" name="submit" value="Confirm-Remove">
        <p>Are you sure you want to remove the item with SKU: ${sessionScope.itemToRemoveSKU}?</p>
        <p>Please enter manager password to confirm:</p>
        <input type="password" name="manager_password" required>
        <br>
        <br>
        <input type="submit" value="Confirm">
        <a href="tellerDashboard.jsp">Cancel</a>
    </form>
</body>
</html>
