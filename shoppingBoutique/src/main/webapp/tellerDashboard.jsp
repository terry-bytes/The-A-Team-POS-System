<%@page import="ateam.Models.Product"%>
<%@ page import="ateam.Models.Employee" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 

<!DOCTYPE html>
<html>
    <head>
        <title>Barcode Scanner</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        <style>
            /* ... your existing styles ... */
            .manual-entry {
                display: flex;
                align-items: center;
                margin-bottom: 10px;
            }
            #manual-sku {
                width: 200px;
                margin-right: 10px;
            }
        </style>
    </head>


    <body>
        
        <div class="container">
        </div>

        <div class="scanned-items">
            <h2>Scanned Items</h2>

            <c:choose>
                <c:when test="${empty scannedItems}">
                    <p class="message">No items found</p>
                </c:when>
                <c:otherwise>
                    <table>
                        <tr>
                            <th>Product SKU</th>
                            <th>Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Action</th> 
                        </tr>
                        <c:forEach var="item" items="${scannedItems}"> 
                            <tr>
                                <td>${item.product_SKU}</td>
                                <td>${item.product_name}</td>
                                <td>${item.scanCount}</td> 
                                <td>${item.product_price}</td>
                                <td>
                                    <form action="ProductServlet" method="post" style="display:inline;">
                                        <input type="hidden" name="input-field" value="${item.product_SKU}">
                                        <button type="submit" name="submit" value="Remove-Item">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>


                    <div class="total-price">
                        Total: <span id="total-price">${totalPrice}</span>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <form action="ProductServlet" method="post">
            <div class="manual-entry">
                <input type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                <button type="submit" name="submit" value="Add-Item">Add Item</button>
            </div>

            <div>
                <label for="payment_method">Payment Method:</label>
                <select id="payment_method" name="payment_method">
                    <option value="cash">Cash</option>
                    <option value="card">Card</option>
                    <option value="cardAndcash">Card & Cash</option>
                </select>
            </div>
            <video id="barcode-scanner" style="display: none;"></video>
            <input type="hidden" name="input-field" id="input-field" placeholder="Scanned barcode will appear here">
            <button type="submit" name="submit" id="auto-submit" value="auto-submit" style="display: none;"></button>



            <form action="ProductServlet" method="post" style="display:inline;">
                <button type="submit" name="submit" value="Complete-Sale">Complete Sale</button>
            </form>

            <script>
                // ... (your existing JavaScript code for Quagga and keyboard input) ...

                // Modify simulateKeyboardInput function to also populate the manual input field
                function simulateKeyboardInput(barcode) {
                    let inputField = document.querySelector('#input-field');
                    let manualInputField = document.querySelector('#manual-sku'); // Update
                    inputField.value = barcode;
                    manualInputField.value = barcode; // Update

                    // ... (rest of your existing code) ...
                }
            </script>

    </body>
</html>
