<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Barcode Scanner</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        <style>
            .green-arrow-button {
                background-color: #28a745; /* Green background */
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
                background: #eee;
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
            .transaction-buttons button {
                background: #3498db;
                color: white;
                padding: 10px 15px;
                border: none;
                margin-top: 5px;
                cursor: pointer;
                border-radius: 4px;
            }
            .transaction-buttons button:hover {
                background: #2980b9;
            }
            #barcode-scanner {
                display: none; /* Hide the camera element */
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
                            <video  id="barcode-scanner" autoplay style = "display: none"></video>
                            <input type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                            <button type="submit" name="submit" value="Add-Item" class="green-arrow-button">Enter</button>
                            <button type="submit" name="submit" value="auto-submit" id="auto-submit" style="display: none"></button>
                        </div>
                        <div>
                            <label for="payment_method">Payment Method:</label>
                            <select id="payment_method" name="payment_method" onchange="checkPaymentMethod()">
                                <option value="cash">Cash</option>
                                <option value="card">Card</option>
                                <option value="cardAndcash">Card & Cash</option>
                            </select>
                        </div>
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
                        <input type="hidden" id="scanned-items-count" name="scannedItemsCount" value="<c:out value='${fn:length(scannedItems)}'/>">
                        <button type="submit" name="submit" value="Complete-Sale">Complete Sale</button>
                    </form>
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
                </div>

                <div class="transaction-buttons">
                    <form action="ReturnItemServlet" method="post">
                        <button type="submit">Return Item</button>
                    </form>
                    <form action="LayawayDashboard.jsp" method="post">
                      <button onclick="redirectToAnotherPage()">Lay Away</button>
                    </form>
                    <form action="VoidSaleServlet" method="post">
                        <button type="submit">Void Sale</button>
                    </form>
                    <form action="RemoveItemServlet" method="post">
                        <button type="submit">Remove Item</button>
                    </form>
                    <form action="listProductVariants.jsp" method="post">
                        <button type="submit">Search Item</button>
                    </form>
                    <form action="ViewReportsServlet" method="post">
                        <button type="submit">View Reports</button>
                    </form>
                    <form action="ProductServlet" method="post">
                        <button type="submit" name ="submit" value ="Inventory">Inventory Management</button>
                    </form>
                </div>
            </div>
        </div>

        <video id="barcode-scanner" autoplay></video>
        <audio id="beep-sound" src="beep.mp3" preload="auto"></audio>

        <script>
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

function startScanner() {
    console.log("Starting Quagga...");
    Quagga.init({
        inputStream: {
            name: "Live",
            type: "LiveStream",
            target: document.querySelector('#barcode-scanner')
        },
        decoder: {
            readers: ["code_128_reader","QR_CODE"]
        }
    }, function (err) {
        if (err) {
            console.error("Quagga initialization failed: ", err);
            return;
        }
        console.log("Quagga initialization succeeded.");
        Quagga.start();
    });

    Quagga.onDetected(function (data) {
        if (!scanningPaused) {
            let barcode = data.codeResult.code;
            console.log("Barcode detected and processed: [" + barcode + "]", data);
            
            // Play beep sound
            document.getElementById('beep-sound').play();
            
            // Stop Quagga after successful detection
            Quagga.stop();

            // Simulate keyboard input
            simulateKeyboardInput(barcode);

            // Pause scanning for 2 seconds
            scanningPaused = true;
            setTimeout(() => {
                scanningPaused = false;
                startScanner(); // Restart Quagga after pause
            }, 2000); // 2 seconds pause
        }
    });
}

function simulateKeyboardInput(barcode) {
    let inputField = document.querySelector('#manual-sku');
    inputField.value = barcode;

    // Trigger the hidden auto-submit button
    document.getElementById('auto-submit').click();

    // If you need to trigger events as if it was typed
    let event = new Event('input', {
        bubbles: true,
        cancelable: true,
    });
    inputField.dispatchEvent(event);
}

document.addEventListener("DOMContentLoaded", function (event) {
    var scannedValue = document.getElementById("scannedValue").value;
    if (scannedValue) {
        document.getElementById("manual-sku").value = scannedValue;
        document.getElementById("auto-submit").click();
    }
});

startScanner();

        </script>
    </body>
</html>