<%@page import="java.math.BigDecimal"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gift Voucher</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f8f9fa;
            margin: 0;
            padding: 0;
        }
        .voucher-container {
            max-width: 600px;
            margin: 50px auto;
            background-color: #ffffff;
            border: 1px solid #dedede;
            border-radius: 10px;
            overflow: hidden;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .voucher-header {
            background-color: #007BFF;
            color: #ffffff;
            padding: 20px;
            text-align: center;
        }
        .voucher-header h1 {
            margin: 0;
            font-size: 24px;
        }
        .voucher-body {
            padding: 20px;
            text-align: center;
        }
        .voucher-body h2 {
            margin-top: 0;
            font-size: 22px;
            color: #333333;
        }
        .voucher-code {
            margin: 20px 0;
            font-size: 30px;
            font-weight: bold;
            color: #e83e8c;
            letter-spacing: 2px;
        }
        .voucher-amount {
            font-size: 20px;
            color: #28a745;
        }
        .voucher-footer {
            background-color: #f8f9fa;
            padding: 10px;
            text-align: center;
            font-size: 12px;
            color: #6c757d;
        }
        .voucher-footer p {
            margin: 5px 0;
        }
    </style>
</head>

<body>
    <%
        BigDecimal change = (BigDecimal)request.getSession(false).getAttribute("change");
        int voucher =(int)request.getSession(false).getAttribute("voucher");
    %>
    <div class="voucher-container">
        <div class="voucher-header">
            <h1>Carol's Boutique</h1>
        </div>
        <div class="voucher-body">
            <h2>Gift Voucher</h2>
            <p>Congratulations! You have received a gift voucher worth</p>
            <div class="voucher-amount">R${sessionScope.change}</div>
            <p>Your voucher code is:</p>
            <div class="voucher-code">${sessionScope.voucher}</div>
        </div>
        <div class="voucher-footer">
            <p>Thank you for shopping at Carol's Boutique!</p>
            <p>Please present this voucher at the time of purchase.</p>
        </div>
    </div>
        
        <input type="button" href="tellerDashboard.jsp" value="OK">
</body>
</html>
