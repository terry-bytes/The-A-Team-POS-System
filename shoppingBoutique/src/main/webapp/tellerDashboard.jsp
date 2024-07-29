
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
                border-radius: 5px; /* Rounded corners */
                position: relative; /* For positioning the arrow */
                display: inline-block; /* Make sure it behaves like a button */
            }

            .green-arrow-button::after {
                content: ""; /* No text */
                position: absolute; /* Position the arrow */
                top: 50%; /* Center vertically */
                right: 10px; /* Position from the right */
                width: 0; /* Zero width */
                height: 0; /* Zero height */
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
                flex-wrap: wrap;
                justify-content: flex-start;
                gap: 5px;
            }
            .key {
                background: #82caeb;
                padding: 15px;
                border: none;
                cursor: pointer;
                border-radius: 4px;
                text-align: center;
                font-size: 1.2em;
                flex: 0 0 auto;
            }
            .key:hover {
                background: #ddd;
            }
            .manual-entry {
                display: flex;
                align-items: center;
                margin-bottom: 10px;
            }

            .transaction-buttons {
                display: flex;
                flex-wrap: wrap; /* Allows buttons to wrap to a new line if space is insufficient */
                gap: 22px; /* Increased spacing between buttons */
                justify-content: center; /* Center buttons horizontally */
                padding: 10px; /* Add padding around the container */
            }

            .transaction-buttons button {
                background: none; /* Remove default background */
                border: none; /* Remove default border */
                cursor: pointer; /* Change cursor on hover */
                padding: 10px; /* Add padding around the icon */
                display: flex; /* Flexbox for centering content */
                justify-content: center; /* Center content horizontally */
                align-items: center; /* Center content vertically */
                width: 60px; /* Fixed width for buttons */
                height: 60px; /* Fixed height for buttons */
                border-radius: 8px; /* Optional: rounded corners */
                transition: background-color 0.3s, opacity 0.3s; /* Smooth transition for background and opacity */
            }

            .transaction-buttons button:hover {
                opacity: 0.8; /* Slightly fade icon on hover */
                background-color: #444; /* Optional: background color on hover */
            }

            .transaction-buttons .icon {
                width: 70px; /* Fixed size for icons */
                height: 70px; /* Fixed size for icons */
                object-fit: contain; /* Ensure the icon fits within the dimensions */
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
                            <button type="submit" name="submit" value="Add-Item" class="green-arrow-button">Enter</button>
                            <button type="submit" name="submit" value="auto-submit" id="auto-submit" style="display: none"></button>
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
                    </form>
                     <p>   </p>
                    <div class="keyboard">
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
                    <div class="transaction-buttons">
                        <form action="ReturnItemServlet" method="post">
                            <button type="submit" title="Return Item">
                                <img src="https://th.bing.com/th/id/OIP.-YCUILzwkqhEWv0dTnBCxgHaHa?w=800&h=800&rs=1&pid=ImgDetMain" alt="Return Item" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                        <form action="LayawayDashboard.jsp" method="post">
                            <button type="submit" onclick="redirectToAnotherPage()" title="Lay Away">
                                <img src="https://th.bing.com/th/id/R.0f694a16ad88cb05e475b40be9d6ce90?rik=Cpc%2ftKvFXCuHEQ&riu=http%3a%2f%2feastonfranklinbooks.com%2fimages%2flayaway_box-lg.png&ehk=huFrJkO9u1eSmYEOHBxVPQcPfqgfjq85uY4WSXUzz4M%3d&risl=&pid=ImgRaw&r=0" alt="Lay Away" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                        <form action="VoidSaleServlet" method="post">
                            <button type="submit" title="Void Sale">
                                <img src="https://th.bing.com/th/id/OIP.EpcMhvdK5Fj7MltlPwNzUgAAAA?rs=1&pid=ImgDetMain" alt="Void Sale" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                        <form action="Search.jsp" method="post">
                            <button type="submit" title="Search Item">
                                <img src="https://th.bing.com/th/id/OIP.MLu0Ae5k7MD3gTU1TQ5svQHaHa?rs=1&pid=ImgDetMain" alt="Search Item" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                        <form action="ViewReportsServlet" method="post">
                            <button type="submit" title="View Reports">
                                <img src="https://th.bing.com/th/id/OIP.9EG55S-fBqneTnNdtmXPHAHaHa?rs=1&pid=ImgDetMain" alt="View Reports" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                        <form action="ProductServlet" method="post">
                            <button type="submit" name="submit" value="Inventory" title="Inventory Management">
                                <img src="https://th.bing.com/th/id/R.73e53d4939d9d0277b14bd80d2a3ca91?rik=RlFgLZ6PqgjZew&riu=http%3a%2f%2fwww.newdesignfile.com%2fpostpic%2f2014%2f04%2fwarehouse-inventory-management_359862.jpg&ehk=YsRn9EnHehUI%2fzbSDqnBvh6%2bl%2f8jxebTg3Pik91f5Bg%3d&risl=&pid=ImgRaw&r=0" alt="Inventory Management" class="icon"> <!-- Custom icon -->
                            </button>
                        </form>
                    </div>

                </div>

            </div>
        </div>

        <video id="barcode-scanner" autoplay></video>
        <audio id="beep-sound" src="beep.mp3" preload="auto"></audio>

        <script>
            function selectPaymentMethod(method) {
                document.getElementById("card-details").style.display = "none";
                document.getElementById("cash-card-amount").style.display = "none";
                if (method === 'card') {
                    document.getElementById("card-details").style.display = "block";
                } else if (method === 'cardAndcash') {
                    document.getElementById("cash-card-amount").style.display = "block";
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