
<%@page import="ateam.Models.Layaway"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Barcode Scanner</title>
        <!-- Include jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        
         <style>
        /* Popup Form Styles */
        .popup {
            display: none; /* Hidden by default */
            position: fixed;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgba(0,0,0,0.4); /* Black with opacity */
        }

        .popup-content {
            background-color: #fefefe;
            margin: 15% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 500px;
        }

        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }

        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
    </style>

        <style>
            .payment-section {
                flex: 1;
            }
            .payment-icons img {
                width: 50px;
                cursor: pointer;
                margin: 5px;
            }
            .payment-icons img:hover {
                transform: scale(1.1);
            }
            .green-arrow-button {
                background-color: #19a1e0; /* Green background */
                color: white; /* White text color */
                border: none; /* Remove border */
                padding: 10px 20px; /* Adjust padding */
                font-size: 1em; /* Font size */
                cursor: pointer; /* Pointer cursor */
                border-radius: 7px; /* Rounded corners */
                position: relative; /* For positioning the arrow */
                display: inline-block; /* Make sure it behaves like a button */
            }

            .green-arrow-button::after {
                content: ""; /* No text */
                position: absolute; /* Position the arrow */
                top: 50%; /* Center vertically */
                right: 10px; /* Position from the right */
                width: 10; /* Zero width */
                height: 10; /* Zero height */
                border-top: 10px solid transparent; /* Top part of the arrow */
                border-bottom: 10px solid transparent; /* Bottom part of the arrow */
                border-left: 10px solid #fff; /* Arrow color */
                transform: translateY(-50%); /* Center arrow vertically */
            }
            .big-key {
                flex: 2; /* Makes the key wider */
                font-size: 1.4em; /* Larger text */
                padding: 20px; /* Larger padding */
            }

            body {
                font-family: 'Open Sans', sans-serif;
                color: #333;
                background-color: #d9e2da;
            }
            .container {
                display: flex;
                padding: 20px;
            }
            .scanned-items {
                flex: 1;
                margin-right: 20px;
            }
            .payment-section {
                flex: 1;
            }
            .my-header {
                background: #f0f0f0;
                padding: 15px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .my-nav ul {
                list-style: none;
                padding: 0;
                margin: 0;
            }
            .my-nav li {
                display: inline;
                margin-right: 20px;
            }
            .my-nav a {
                text-decoration: none;
                color: #333;
            }
            .scanned-items h2 {
                color: #2980b9;
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 10px;
            }
            th {
                background: #f5f5f5;
            }
            .total-price {
                margin-top: 20px;
                font-size: 1.2em;
            }
            .manual-entry-section {
                background: #f9f9f9;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            #manual-sku {
                width: 100%;
                padding: 10px;
                margin-bottom: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }
            .keyboard {
                display: flex;
                justify-content: flex-start; /* Aligns the keyboard to the left */
                align-items: center;
                margin-left: 5px;
            }

            .keyboard-wrapper {
                display: flex;
                flex-wrap: wrap;
                width: 300px; /* adjust width as needed */
                gap: 5px; /* space between keys */
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #3498db;
            }

            .key {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 50px; /* adjust width as needed */
                height: 30px; /* adjust height as needed */
                border: 1px solid #ccc;
                border-radius: 5px;
                background-color: #3498db;
                cursor: pointer;
                user-select: none;
            }

            .big-key {
                width: 100%; /* makes the backspace key span the width */
            }

            .manual-entry {
                display: flex;
                align-items: center;
                margin-bottom: 10px;
            }
            .transaction-buttons {
                display: grid; /* Use grid layout */
                grid-template-columns: repeat(2, 1fr); /* 2 columns, each taking up an equal fraction of available space */
                gap: 5px; /* Space between buttons */
                padding: 5px; /* Padding inside the container */
                border: 1px solid #ccc;
                border-radius: 10px;
                background-color: #f5f5f5;
                width: 268px; /* Adjusted width to make the container smaller */
                max-width: 50%; /* Ensures the container doesn't overflow */
                box-sizing: border-box; /* Ensures padding and border are included in width */
                position: absolute; /* Positions the container absolutely */
                right: 60px; /* Positions the container 10px from the right edge */
                top: 250px; /* Positions the container 10px from the top edge */
            }

            .transaction-buttons button {
                display: flex;
                justify-content: center; /* Center the button content */
                align-items: center;
                width: 100%; /* Makes the button take the full width of the grid cell */
                padding: 5px; /* Padding inside the buttons */
                border: none;
                border-radius: 5px;
                background-color: #fff;
                cursor: pointer;
                transition: background-color 0.3s;
            }

            .transaction-buttons button:hover {
                background-color: #e0e0e0; /* Change background on hover */
            }

            .transaction-buttons img.icon {
                max-width: 100%; /* Ensures the icon fits within the button */
                height: auto;
            }





            #barcode-scanner {

            }
            .right-section {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            .left-section {
                flex: 1;
                padding-right: 20px;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }
            .user-info {
                display: flex;
                align-items: center;
                margin-bottom: 20px;
            }
            .user-info img {
                width: 40px;
                height: 40px;
                border-radius: 50%;
                margin-right: 10px;
            }

            /* Popup Overlay */
            .popup-overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.5);
                z-index: 1000;
            }

            .popup-content {
                position: relative;
                background-color: #fff;
                border: 1px solid #333;
                width: 50%; /* Adjust width as needed */
                margin: 10% auto;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }

            .popup-close {
                position: absolute;
                top: 10px;
                right: 10px;
                cursor: pointer;
                font-size: 20px;
                color: #aaa;
            }

            .popup-close:hover {
                color: #333;
            }

            .popup-content h2 {
                margin-bottom: 20px;
                color: #333;
            }

            .popup-content form {
                display: flex;
                flex-direction: column;
                align-items: center;
            }

            .popup-content label {
                font-weight: bold;
                margin-bottom: 10px;
            }

            .popup-content input[type="text"],
            .popup-content input[type="email"] {
                width: 100%;
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #ccc;
                border-radius: 4px;
                font-size: 1em;
                box-sizing: border-box;
            }

            .popup-content button[type="submit"] {
                background-color: #3498db;
                color: white;
                border: none;
                padding: 10px 20px;
                margin-top: 15px;
                cursor: pointer;
                border-radius: 4px;
                font-size: 1em;
            }

            .popup-content button[type="submit"]:hover {
                background-color: #2980b9;
            }
        </style>
    </head>

    <body>



        <div class="container">
            <div class="left-section">
                <div class="user-info">
                    <img src="images.jpeg" alt="User Avatar">
                    <div>
                        <%
                            Employee loggedInUser = (Employee) session.getAttribute("Employee");
                            if (loggedInUser != null) {
                                out.print(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
                            } else {
                                out.print("Username");
                            }
                        %>
                    </div>
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
                                    <th>Size</th>
                                    <th>Color</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Action</th>
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
                                            <form action="ProductServlet" method="post" style="display:inline;">
                                                <input type="hidden" name="sku" value="${item.product_SKU}">
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
            </div>

            <div class="payment-section">
                <div class="manual-entry-section">
                    <form id="product-form" action="ProductServlet" method="post">
                        <div class="manual-entry">
                            <input type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                            <button type="submit" name="submit" value="Add-Item" class="green-arrow-button">OK</button>
                            <button type="submit" name="submit" value="auto-submit" id="auto-submit" style="display: none"></button>
                        </div>
                        <div class="payment-icons">
                            <img src="Icons/cashhh.png" alt="Cash" onclick="selectPaymentMethod('cash')">
                            <img src="Icons/290142_business_card_cash_credit_money_icon.png" alt="Card" onclick="selectPaymentMethod('card')">
                            <img src="Icons/wallet.png" alt="Card & Cash" onclick="selectPaymentMethod('cardAndcash')">
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
                        <p>   </p>
                        <div>
                            <label for="customer_email">Customer Email:</label>
                            <input type="email" id="customer_email" name="customer_email" placeholder="Enter customer email" >
                        </div>
                        <p>   </p>
                        <input type="hidden" id="scanned-items-count" name="scannedItemsCount" value="<c:out value='${fn:length(scannedItems)}'/>">
                        <button type="submit" name="submit" value="Complete-Sale">Complete Sale</button>
                        <input type="submit" value="Process Layaway" onclick="openPopup()">
                        <input type="button" value="Process IBT" id="openPopupButton">
                    </form>
                    <p>   </p>
                    <div class="keyboard">
                        <div class="keyboard-wrapper">
                            <div class="key" onclick="appendToInput('1')">1</div>
                            <div class="key" onclick="appendToInput('2')">2</div>
                            <div class="key" onclick="appendToInput('3')">3</div>
                            <div class="key" onclick="appendToInput('4')">4</div>
                            <div class="key" onclick="appendToInput('5')">5</div>
                            <div class="key" onclick="appendToInput('6')">6</div>
                            <div class="key" onclick="appendToInput('7')">7</div>
                            <div class="key" onclick="appendToInput('8')">8</div>
                            <div class="key" onclick="appendToInput('9')">9</div>
                            <div class="key" onclick="appendToInput('0')">0</div>
                            <div class="key" onclick="appendToInput('-')">-</div>
                            <div class="key" onclick="appendToInput('.')">.</div>
                            <div class="key" onclick="appendToInput('@')">@</div>
                            <div class="key" onclick="appendToInput('q')">q</div>
                            <div class="key" onclick="appendToInput('w')">w</div>
                            <div class="key" onclick="appendToInput('e')">e</div>
                            <div class="key" onclick="appendToInput('r')">r</div>
                            <div class="key" onclick="appendToInput('t')">t</div>
                            <div class="key" onclick="appendToInput('y')">y</div>
                            <div class="key" onclick="appendToInput('u')">u</div>
                            <div class="key" onclick="appendToInput('i')">i</div>
                            <div class="key" onclick="appendToInput('o')">o</div>
                            <div class="key" onclick="appendToInput('p')">p</div>
                            <div class="key" onclick="appendToInput('a')">a</div>
                            <div class="key" onclick="appendToInput('s')">s</div>
                            <div class="key" onclick="appendToInput('d')">d</div>
                            <div class="key" onclick="appendToInput('f')">f</div>
                            <div class="key" onclick="appendToInput('g')">g</div>
                            <div class="key" onclick="appendToInput('h')">h</div>
                            <div class="key" onclick="appendToInput('j')">j</div>
                            <div class="key" onclick="appendToInput('k')">k</div>
                            <div class="key" onclick="appendToInput('l')">l</div>
                            <div class="key" onclick="appendToInput('z')">z</div>
                            <div class="key" onclick="appendToInput('x')">x</div>
                            <div class="key" onclick="appendToInput('c')">c</div>
                            <div class="key" onclick="appendToInput('v')">v</div>
                            <div class="key" onclick="appendToInput('b')">b</div>
                            <div class="key" onclick="appendToInput('n')">n</div>
                            <div class="key" onclick="appendToInput('m')">m</div>
                            <div class="key big-key" onclick="backspace()">&#9003; Backspace</div>
                        </div>
                    </div>

                    <div class="transaction-buttons">
                        <form action="ReturnItemServlet" method="post">
                            <button type="submit" name ="submit" value ="return">
                                <img src="Icons/11419687_return_icon.png" alt="Return Item" class="icon">
                            </button>
                        </form>
                        <form action="LayawayDashboard.jsp" method="post">
                            <button type="submit" onclick="redirectToAnotherPage()" title="Lay Away">
                                <img src="Icons/172576_box_icon.png" alt="Lay Away" class="icon">
                            </button>
                        </form>
                        <form action="VoidSaleServlet" method="post">
                            <button type="submit" title="Void Sale">
                                <img src="Icons/8140875_pos_void_ticket_cancal_cinema_icon.png" alt="Void Sale" class="icon">
                            </button>
                        </form>
                        <form action="Search.jsp" method="post">
                            <button type="submit" title="Search Item">
                                <img src="Icons/211818_search_icon.png" alt="Search Item" class="icon">
                            </button>
                        </form>
                        <form action="ViewReportsServlet" method="post">
                            <button type="submit" title="View Reports">
                                <img src="https://static.vecteezy.com/system/resources/previews/024/607/383/non_2x/data-analysis-icon-profit-graph-illustration-sign-data-science-symbol-or-logo-vector.jpg" alt="View Reports" class="icon">
                            </button>
                        </form>
                        <form action="ProductServlet" method="post">
                            <button type="submit" name="submit" value="Inventory" title="Inventory Management">
                                <img src="https://static.vecteezy.com/system/resources/previews/015/890/404/non_2x/checklist-parcel-icon-outline-delivery-box-vector.jpg" alt="Inventory Management" class="icon">
                            </button>
                        </form>
                    </div>


                </div>


            </div>
        </div>
                        
    <!-- The Popup Form -->
    <div id="popupForm" class="popup">
        <div class="popup-content">
            <span class="close" id="closePopup">&times;</span>
            <h2>Enter IBT ID Number</h2>
            <form id="ibtForm">
                <label for="ibtNumber">IBT ID:</label>
                <input type="text" id="ibtNumber" name="ibtNumber" required>
                <label>Store ID: </label><label></label>
                <label></label><label></label>
                <label></label><label></label>
                <input type="submit" value="Submit">
            </form>
        </div>
    </div>
         
         <script>
        document.addEventListener('DOMContentLoaded', function() {
            // Get the popup and button elements
            var popup = document.getElementById("popupForm");
            var btn = document.getElementById("openPopupButton");
            var close = document.getElementById("closePopup");

            // When the user clicks the button, open the popup
            btn.onclick = function() {
                popup.style.display = "block";
            };

            // When the user clicks on <span> (x), close the popup
            close.onclick = function() {
                popup.style.display = "none";
            };

            // When the user clicks anywhere outside of the popup, close it
            window.onclick = function(event) {
                if (event.target === popup) {
                    popup.style.display = "none";
                }
            };
        });
    </script>

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

        </script>

        <script>
            function openPopup() {
                document.getElementById('layawayPopup').style.display = 'block';
                event.preventDefault(); // Prevent form submission
            }

            function closePopup() {
                document.getElementById('layawayPopup').style.display = 'none';
            }

            function submitLayaway(event) {
                event.preventDefault(); // Prevent form from submitting normally

                // Get form data
                var customerName = document.getElementById('customerName').value;
                var customerEmail = document.getElementById('customerEmail').value;

                // Capture current time in JavaScript
                var buttonClickTime = new Date().toISOString();

                // Calculate time 10 seconds later
                var tenSecondsLater = new Date();
                tenSecondsLater.setSeconds(tenSecondsLater.getSeconds() + 10);
                var expiryTime = tenSecondsLater.toISOString();


                // You can perform validation here if needed

                // Example AJAX request to submit data
                $.ajax({
                    url: 'LayawayServlet', // Replace with your servlet URL
                    type: 'POST',
                    data: {
                        layaway_switch: 'Add Layaway',
                        customerName: customerName,
                        buttonClickTime: buttonClickTime,
                        expiryTime: expiryTime,
                        customerEmail: customerEmail
                    },
                    success: function (response) {
                        console.log('Layaway submitted successfully');
                        closePopup();
                        // Optionally handle success response
                    },
                    error: function (xhr, status, error) {
                        console.error('Error submitting layaway:', error);
                        // Optionally handle error
                    }
                });
            }
        </script>

        <div class="popup-overlay" id="layawayPopup">
            <div class="popup-content">
                <span class="popup-close" onclick="closePopup()">&times;</span>
                <h2>Process Layaway</h2>
                <form id="layawayForm" onsubmit="submitLayaway(event)" action="LayawayServlet" method="post">
                    <label for="customerName">Customer Name:</label>
                    <input type="text" id="customerName" name="customerName" required>
                    <label for="customerEmail">Customer Email:</label>
                    <input type="email" id="customerEmail" name="customerEmail" required>
                    <button type="submit" name="layaway_switch" value="Add Layaway">Submit</button>
                </form>
            </div>
        </div>
    </body>
</html>