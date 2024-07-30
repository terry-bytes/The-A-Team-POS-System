<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Return Sale</title>
    <link rel="stylesheet" type="text/css" href="styles.css">
    <style>
    /* Basic reset for body */
    

    </style>
</head>
<body>
    <div class="container">
        <h1>Return Sale</h1>

        <!-- Retrieve Sale Section -->
        <form action="ReturnedServlet" method="post">
            <input type="hidden" name="submit" value="Retrieve-Sale">
            <label for="sales_ID">Enter Sales ID:</label>
            <input type="number" id="sales_ID" name="sales_ID" required>
            <button type="submit">Retrieve Sale</button>
        </form>

        <!-- Display Sale and Sales Items if available -->
        <c:if test="${not empty sale}">
            <h2>Sale Details</h2>
            <p>Store Id:${sale.store_ID}
            <p>Sale ID: ${sale.sales_ID}</p>
            <p>Date: ${sale.sales_date}</p>
            <p>Total Amount: ${sale.total_amount}</p>
            <p>Payment Method: ${sale.payment_method}</p>
            <p>Teller :${sale.employee_ID}</p>
            <div>
            <h3>Items in Sale</h3>
            <table>
                <thead>
                    <tr>
                        <th>Sales Item ID</th>
                        <th>Sale ID</th>
                        <th>Product ID</th>
                        <th>Quantity</th>
                        <th>Unit Price</th>
                    </tr>
                    
                </thead>
                <tbody>
                    <c:forEach var="item" items="${salesItems}">
                        <tr>
                            <td>${item.sales_item_ID}</td>
                            <td>${item.sales_ID}</td>
                            <td>${item.product_ID}</td>
                            <td>${item.quantity}</td>
                            <td>${item.unit_price}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="total-price">
                                Total: <span id="total-price">${totalPrice}</span>
                            </div>
            </div>
            <!-- Process Return Section -->
            <h3>Process Return</h3>
            <form action="ReturnedServlet" method="post">
                <input type="hidden" name="submit" value="Process-Return">
               
                <label for="">Enter Sales Item ID to Return:</label>
                <input type="number" id="salesItemId" name="salesItemId" required>
                <label for="quantity">Enter Quantity to Return:</label>
                <input type="number" id="quantity" name="quantity" required>
                <label for="email">Customer Email:</label>
                <input type="email" id="email" name="email" required>
                <label for="return_reason">Reason for Return:</label>
                <select type ="text" id="return_reason" name="return_reason" required>
                    <option value="Damaged">Damaged</option>
                    <option value="Wrong Item">Wrong Item/Size</option>
                    <option value="Other">Other</option>
                </select>
                <button type="submit" onclick="Handle-Customer-Choice">Process Return</button>
            </form>
        </c:if>

        
            <h3>Handle Customer Choice</h3>
            <form action="ReturnedServlet" method="post">
                <input type="hidden" name="submit" value="Handle-Customer-Choice">
                <label for="customer_choice">Customer Choice:</label>
                <select id="customer_choice" name="customer_choice" required>
                    <option value="Select-New-Item">Select New Item</option>
                    <option value="Receive-Change">Receive Change</option>
                </select>
                <button type="submit" name="submit" >Submit Choice</button>
            </form>
        
        
            <div id="scanned-items" class="scanned-items" style="display:none;">
                
                <div class="payment-section">
                <div class="manual-entry-section">
                    <form id="product-form" action="ReturnedServlet" method="post">
                        <div class="manual-entry">
                            <input type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                            <button type="submit" name="submit" value="Add-Item" class="green-arrow-button">OK</button>
                            <button type="submit" name="submit" value="auto-submit" id="auto-submit" style="display: none"></button>
                        </div>
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
                                <th>Size</th>
                                <th>Color</th>
                                <th>Quantity</th>
                                <th>Price</th>
                                
                            </tr>
                            <c:forEach var="item" items="${scannedItems}">
                                <tr>
                                    <td>${item.product_SKU}</td>
                                    <td>${item.product_name}</td>
                                    <td>${item.size}</td>
                                    <td>${item.color}</td>
                                    <td>${item.scanCount}</td>
                                    <td>${item.product_price}</td>
                                    <td>
                                        
                                    </td>
                                </tr>
                            </c:forEach>
                        </table>
                        <div class="total-price">
                            Total: <span id="total-price">${totalPrice}</span>
                        </div>
                        <div class="payment-icons">
                            <img src="https://th.bing.com/th/id/R.f50425b14e844bcb90abb0f96c63035f?rik=GB2zRtYycKegvQ&riu=http%3a%2f%2fclipart-library.com%2fimg%2f1867131.png&ehk=JHCBEE17zWHHjFLG3qhbaIoG2vKydXdPRPRWPVL9AME%3d&risl=&pid=ImgRaw&r=0" alt="Cash" onclick="selectPaymentMethod('cash')">
                            <img src="https://th.bing.com/th/id/OIP.zx9HaAZ6G-qacPDvkz7IhQHaHh?rs=1&pid=ImgDetMain" alt="Card" onclick="selectPaymentMethod('card')">
                            <img src="https://th.bing.com/th/id/OIP.WbM79d11TS0NkV_votIkhAHaHa?rs=1&pid=ImgDetMain" alt="Card & Cash" onclick="selectPaymentMethod('cardAndcash')">
                        </div>
                        <p>   </p>


                        <div id="card-details" style="display:none;">
                            <div>
                                <label for="card_number">Card Number:</label>
                                <input type="text" id="card_number" name="card_number">
                            </div>
                            <div>
                                <label for="expiry_date">Expiry Date:</label>
                                <input type="text" id="expiry_date" name="expiry_date">
                            </div>
                            <div>
                                <label for="cvv">CVV:</label>
                                <input type="text" id="cvv" name="cvv">
                            </div>
                        </div>
                        <div id="cash-amount" style="display:none;">
                            <div>
                                <label for="cash_amount">Cash Amount:</label>
                                <input type="text" id="cash_amount" name="cash_amount">
                            </div>
                        </div>
                        <div id="cash-card-amount" style="display:none;">
                            <div>
                                <label for="cash_amount">Cash Amount:</label>
                                <input type="text" id="cash_amount" name="cash_amount">
                            </div>
                            <div>
                                <label for="card_amount">Card Amount:</label>
                                <input type="text" id="card_amount" name="card_amount">
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

                
 

    <!-- Modal Structure -->
    <div id="messageModal" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <p id="modalMessage"></p>
        </div>
    </div>

     <video id="barcode-scanner" autoplay></video>
        <audio id="beep-sound" src="beep.mp3" preload="auto"></audio>

        <script>
            function selectPaymentMethod(method) {
                document.getElementById('payment-method').value = method;
            }
            function selectPaymentMethod(method) {
                document.getElementById("card-details").style.display = "none";
                document.getElementById("cash-card-amount").style.display = "none";
                document.getElementById("cash-amount").style.display = "none";

                if (method === 'card') {
                    document.getElementById("card-details").style.display = "block";
                } else if (method === 'cardAndcash') {
                    document.getElementById("cash-card-amount").style.display = "block";
                } else if (method === 'cash') {
                    document.getElementById("cash-amount").style.display = "block";
                }
            }

            function redirectToAnotherPage() {
                // Redirect to another JSP page
                window.location.href = 'LayawayDashboard.jsp'; // Replace 'AnotherPage.jsp' with your actual JSP page path
            }
            function validateForm() {
                const scannedRows = document.querySelectorAll('.scanned-items table tr');
                const itemCount = scannedRows.length - 1;

                if (itemCount === 0) {
                    alert("Add at least one item before completing the sale.");
                    return false;
                }
                return true;
            }

            function checkPaymentMethod() {
                var paymentMethod = document.getElementById("payment_method").value;
                var cardDetails = document.getElementById("card-details");
                var cashCardAmount = document.getElementById("cash-card-amount");

                if (paymentMethod === "card") {
                    cardDetails.style.display = "block";
                    cashCardAmount.style.display = "none";
                } else if (paymentMethod === "cardAndcash") {
                    cardDetails.style.display = "block";
                    cashCardAmount.style.display = "block";
                } else {
                    cardDetails.style.display = "none";
                    cashCardAmount.style.display = "none";
                }
            }

            let isCapsLock = false;

            function toggleCapsLock() {
                isCapsLock = !isCapsLock;
                const keys = document.querySelectorAll('.key');
                keys.forEach(key => {
                    if (key.textContent.length === 1) {
                        key.textContent = isCapsLock ? key.textContent.toUpperCase() : key.textContent.toLowerCase();
                    }
                });
            }

            function appendToInput(value) {
                if (isCapsLock) {
                    value = value.toUpperCase();
                }
                document.getElementById('manual-sku').value += value;
            }

            function appendToInput(value) {
                var inputField = document.getElementById("manual-sku");
                inputField.value += value;
            }

            function clearInput() {
                var inputField = document.getElementById("manual-sku");
                inputField.value = "";
            }
            function backspace() {
                var input = document.getElementById('manual-sku');
                input.value = input.value.slice(0, -1);
            }

            function checkPaymentMethod() {
                var paymentMethod = document.getElementById("payment_method").value;
                var cardDetails = document.getElementById("card-details");
                var cashCardAmount = document.getElementById("cash-card-amount");

                if (paymentMethod === "card") {
                    cardDetails.style.display = "block";
                    cashCardAmount.style.display = "none";
                } else if (paymentMethod === "cash") {
                    cardDetails.style.display = "none";
                    cashCardAmount.style.display = "none";
                } else if (paymentMethod === "cardAndcash") {
                    cardDetails.style.display = "block";
                    cashCardAmount.style.display = "block";
                } else {
                    cardDetails.style.display = "none";
                    cashCardAmount.style.display = "none";
                }
            }

            let scanningPaused = false;

            document.addEventListener('DOMContentLoaded', (event) => {
                initQuagga();
            });

            function initQuagga() {
                Quagga.init({
                    inputStream: {
                        name: "Live",
                        type: "LiveStream",
                        target: document.querySelector('#barcode-scanner')
                    },
                    decoder: {
                        readers: ["code_128_reader", "ean_reader", "ean_8_reader"]
                    }
                }, function (err) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    console.log("Barcode scanner initialized");
                    Quagga.start();
                });

                Quagga.onDetected(function (data) {
                    console.log("Detected code:", data.codeResult.code);
                    var sku = data.codeResult.code;
                    document.getElementById('manual-sku').value = sku;
                    // Play beep sound
                    document.getElementById('beep-sound').play();
                    document.getElementById('auto-submit').click();
                    Quagga.stop();
                    Quagga.start();
                });
            }

            $(document).ready(function () {
                $("#addLayawayForm").submit(function (event) {
                    event.preventDefault(); // Prevent the form from submitting normally

                    // Capture current time in JavaScript
                    var buttonClickTime = new Date().toISOString();

                    // Calculate time 10 seconds later
                    var tenSecondsLater = new Date();
                    tenSecondsLater.setSeconds(tenSecondsLater.getSeconds() + 10);
                    var expiryTime = tenSecondsLater.toISOString();

                    // Send AJAX request to store timestamps in database via LayawayServlet
                    $.ajax({
                        url: "LayawayServlet",
                        type: "POST",
                        data: {
                            action: "addLayaway",
                            product_ID: $("#product_ID").val(),
                            product_quantity: $("#product_quantity").val(),
                            customer_email: $("#customer_email").val(),
                            buttonClickTime: buttonClickTime,
                            expiryTime: expiryTime,
                            customer_number: $("#customer_number").val(),
                            customer_name: $("#customer_name").val(),
                            layaway_switch: $("input[name='layaway_switch']").val()
                        },
                        success: function (response) {
                            console.log("Layaway added successfully");
                            // Optionally handle success response
                        },
                        error: function (xhr, status, error) {
                            console.error("Error adding layaway: " + error);
                            // Optionally handle error
                        }
                    });
                });
            });
        // Get the modal
        var modal = document.getElementById("messageModal");

        // Get the <span> element that closes the modal
        var span = document.getElementsByClassName("close")[0];

        // When the user clicks on <span> (x), close the modal
        span.onclick = function() {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modal) {
                modal.style.display = "none";
            }
        }

        // Function to show modal with a message
        function showModal(message) {
            document.getElementById("modalMessage").innerText = message;
            modal.style.display = "block";
        }

        document.getElementById('customer_choice').addEventListener('change', function() {
                var scannedItemsDiv = document.getElementById('scanned-items');
                if (this.value === 'Select-New-Item') {
                    scannedItemsDiv.style.display = 'block';
                } else {
                    scannedItemsDiv.style.display = 'none';
                }
            });
        // Display message in modal if available
        <c:if test="${not empty message}">
            showModal("${message}");
        </c:if>

        <c:if test="${not empty errorMessage}">
            showModal("${errorMessage}");
        </c:if>
    </script>
</body>
</html>
