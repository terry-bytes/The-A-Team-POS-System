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
        input[type="number"],
        select {
            width: calc(100% - 22px);
            padding: 10px;
            margin-top: 5px;
            margin-bottom: 10px;
            border: 1px solid #cccccc;
            border-radius: 4px;
        }

        .payment-section {
            display: none;
        }

        .flex {
            display: flex;
            justify-content: space-between;
        }

        .inputBox {
            flex: 1;
            margin-right: 10px;
        }

        .inputBox:last-child {
            margin-right: 0;
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
    <script>
        function showAdditionalPaymentSection() {
            var paymentMethod = document.querySelector('select[name="additional_payment_method"]').value;
            var sections = document.querySelectorAll('.payment-section');
            sections.forEach(section => section.style.display = 'none');

            if (paymentMethod === 'cash') {
                document.getElementById('cash-section').style.display = 'block';
            } else if (paymentMethod === 'card') {
                document.getElementById('card-section').style.display = 'block';
            } else if (paymentMethod === 'cashAndCard') {
                document.getElementById('cash-card-section').style.display = 'block';
            }
        }

        document.addEventListener('DOMContentLoaded', function () {
            document.querySelector('select[name="additional_payment_method"]').addEventListener('change', showAdditionalPaymentSection);
        });
    </script>
</head>
<body>
    <%
       BigDecimal remainingAmount = (BigDecimal) request.getSession(false).getAttribute("remainingAmount");
       BigDecimal voucherAmount = (BigDecimal) request.getSession(false).getAttribute("voucherAmount");
       String voucherCode = (String) request.getSession(false).getAttribute("voucherCode");
    %>
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
                        <option value="">Select</option>
                        <option value="cash">Cash</option>
                        <option value="card">Card</option>
                        <option value="cashAndCard">Cash and Card</option>
                    </select>
                </div>

                <!-- Cash Section -->
                <div class="payment-section" id="cash-section">
                    <label for="additional_cash_amount">Enter Cash Amount:</label>
                    <input type="number" name="additional_cash_amount" step="0.01">
                </div>

                <!-- Card Section -->
                <div class="payment-section" id="card-section">
                    <div>
                        <label for="card_number">Card Number:</label>
                        <input type="text" id="card_number" name="card_number" placeholder="Card Number">
                    </div>
                    <div class="flex">
                        <div class="inputBox">
                            <label for="expiry_date">Expiry Date:</label>
                            <input type="text" id="expiry_date" name="expiry_date" placeholder="Expiry Date">
                        </div>
                        <div class="inputBox">
                            <label for="cvv">CVV:</label>
                            <input type="text" id="cvv" name="cvv" placeholder="CVV">
                        </div>
                    </div>
                </div>

                <!-- Cash and Card Section -->
                <div class="payment-section" id="cash-card-section">
                    <div class="flex">
                        <div class="inputBox">
                            <label for="cash_amount2">Enter Cash Amount:</label>
                            <input type="number" id="cash_amount2" name="cash_amount2" step="0.01">
                        </div>
                        <div class="inputBox">
                            <label for="card_amount2">Enter Card Amount:</label>
                            <input type="number" id="card_amount2" name="card_amount2" step="0.01">
                        </div>
                    </div>
                </div>

                <button type="submit">Complete Payment</button>
            </form>
        </div>
    </div>
</body>
</html>
