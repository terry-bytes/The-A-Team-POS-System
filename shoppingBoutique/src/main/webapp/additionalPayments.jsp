<%@page import="java.math.BigDecimal"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #e0f7fa;
        }

        .container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 20px;
            background-color: #ffffff;
        }

        .content {
            background-color: #ffffff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            max-width: 600px;
            width: 100%;
        }

        h2 {
            margin-top: 0;
            color: #00796b;
        }

        p {
            color: #555555;
        }

        form {
            display: flex;
            flex-direction: column;
        }

        label {
            margin-top: 10px;
            font-weight: bold;
        }

        input[type="text"],
        select {
            width: calc(100% - 22px);
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 10px;
            border: 1px solid #cccccc;
            border-radius: 4px;
        }

        button {
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            background-color: #00796b;
            color: white;
            font-size: 16px;
            cursor: pointer;
        }

        button:hover {
            background-color: #004d40;
        }
    </style>
    <title>Additional Payment Required</title>
</head>
<body>
    <div class="container">
        <div class="content">
            <h2>Additional Payment Required</h2>
            <p>The voucher amount is less than the total amount. Please choose an additional payment method to complete the sale.</p>
            <form action="ProductServlet" method="post">
                <input type="hidden" name="payment_method" value="additionalPayment">
                <input type="hidden" name="remaining_amount" value="${remainingAmount}">
                <input type="hidden" name="voucher_amount" value="${voucherAmount}">
                <input type="hidden" name="voucher_code" value="${voucherCode}">
                <div>
                    <label for="additional_payment_method">Select Payment Method:</label>
                    <select name="additional_payment_method" required>
                        <option value="cash">Cash</option>
                        <option value="card">Card</option>
                        <option value="cashAndCard">Cash and Card</option>
                    </select>
                </div>
                <div>
                    <label for="additional_amount">Enter Amount:</label>
                    <input type="text" name="additional_amount" required>
                </div>
                <button type="submit">Complete Payment</button>
            </form>
        </div>
    </div>
</body>
</html>
