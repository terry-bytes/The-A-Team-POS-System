<%@page import="ateam.Models.Product"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Sale Receipt</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report.css">
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f8f8f8;
            }
            .container {
                width: 80%;
                margin: 20px auto;
                background-color: #fff;
                padding: 20px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }
            h1 {
                text-align: center;
                color:  #f9f9f9;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            table, th, td {
                border: 1px solid #ddd;
            }
            th, td {
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
            .total, .vat, .cash-paid, .card-paid, .change {
                text-align: right;
                font-weight: bold;
            }
            .total {
                background-color: #e9e9e9;
            }
            .vat {
                background-color: #f9f9f9;
            }
            .cash-paid, .card-paid {
                background-color: #f9f9f9;
            }
            .change {
                background-color: #e9e9e9;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Sale Successful</h1>
            <table>
                <tbody>
                    <%-- Assuming `scannedItems` is available in request scope and iterates over items --%>
                    <%
                        List<Product> scannedItems = (List<Product>) request.getAttribute("scannedItems");
                        if (scannedItems != null) {
                            for (Product item : scannedItems) {
                    %>
                    <tr>
                        <td><%= item.getProduct_name()%></td>
                        <td><%= item.getScanCount()%></td>
                        <td><%= item.getProduct_price()%></td>
                        <td><%= item.getScanCount() * item.getProduct_price()%></td>
                    </tr>
                    <%
                            }
                        }
                    %>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3" class="total">Total Amount (R)</td>
                        <td>${totalAmount}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="vat">VAT (15%) (R)</td>
                        <td>${vatAmount}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="cash-paid">Cash Paid (R)</td>
                        <td>${cashPaid}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="card-paid">Card Paid (R)</td>
                        <td>${cardPaid}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="change">Voucher Paid (R)</td>
                        <td>${voucherAmount}</td>
                    </tr>
                    <tr>
                        <td colspan="3" class="change">Change (R)</td>
                        <td>${change}</td>
                    </tr>
                </tfoot>
            </table>
            <a href="tellerDashboard.jsp" class="button">Back to Teller Dashboard</a>
        </div>
    </body>
</html>
