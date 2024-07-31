<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Confirm Item Removal</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #d9e2da;
                margin: 0;
                padding: 0;
                display: flex;
                justify-content: center;
                align-items: center;
                height: 100vh;
            }

            .container {
                background-color: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                width: 400px;
                text-align: center;
            }

            h2 {
                margin-bottom: 20px;
                color: #333;
            }

            p {
                margin-bottom: 20px;
                color: #555;
            }

            input[type="password"] {
                width: 100%;
                padding: 10px;
                margin-bottom: 20px;
                border: 1px solid #ddd;
                border-radius: 5px;
            }

            input[type="submit"], a {
                background-color: #007bff;
                color: white;
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                border: none;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            input[type="submit"]:hover, a:hover {
                background-color: #0056b3;
            }

            a {
                display: inline-block;
                margin-top: 10px;
                background-color: #007bff;
                color: white; /* Text color set to white */
                padding: 10px 20px;
                text-decoration: none;
                border-radius: 5px;
                transition: background-color 0.3s;
            }

            a:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h2>Confirm Item Removal</h2>

            <form action="ProductServlet" method="post">
                <input type="hidden" name="submit" value="Confirm-Remove">
                <p>Are you sure you want to remove the item with SKU: ${sessionScope.itemToRemoveSKU}?</p>
                <p>Please enter the manager password to confirm:</p>
                <input type="password" name="manager_password" placeholder="Enter Manager Password" required>
                <input type="submit" value="Confirm">
            </form>
            <a href="tellerDashboard.jsp">Cancel</a>
        </div>
    </body>
</html>
