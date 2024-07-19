<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
    <title>Barcode Scanner</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
    <style>
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


            .transaction-buttons {
                display: flex;
                flex-wrap: wrap;
                justify-content: flex-end;
                gap: 10px;
                margin-bottom: 20px;
            }

            .transaction-buttons button {
                padding: 8px;
                font-size: 14px;
                cursor: pointer;
                width: 150px;
            }

            .scanned-items {
                flex: 1;
                border: 1px solid #ccc;
                padding: 10px;
                margin-bottom: 10px;
                max-height: 400px; /* Increased height */
                overflow-y: auto;  /* Allow vertical scrolling */
            }

            .scanned-items h2 {
                margin-top: 0;
            }

            #item-list {
                list-style-type: none;
                padding: 0;
                overflow-y: auto;
                max-height: 500px; /* Increased height */
                margin-bottom: 20px;
            }

            .total-price {
                font-size: 1.5em;
                margin-top: auto;
                text-align: right;
            }

            .right-section {
                flex: 1;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
            }

            .keyboard {
                width: 100%;
                max-width: 400px;
                align-self: flex-end;
                border: 1px solid #ccc;
                padding: 10px;
                font-size: 16px;
            }

            .keyboard-row {
                display: flex;
            }

            .key {
                display: flex;
                justify-content: center;
                align-items: center;
                width: 50px;
                height: 50px;
                border: 1px solid #ccc;
                margin: 5px;
                cursor: pointer;
            }

            input[type="text"] {
                width: calc(100% - 20px);
                padding: 10px;
                margin-bottom: 10px;
                border: 1px solid #ccc;
            }

            #toggleCase {
                background-color: #ccc;
                font-weight: bold;
            }

            #complete-sale {
                padding: 10px;
                font-size: 16px;
                cursor: pointer;

                text-align: center;
                border: none;
                background-color: #4CAF50;
                color: white;
                width: 150px;
            }
            .container {
                display: flex;
            }
            .left-section, .right-section {
                padding: 20px;
                border: 1px solid #ccc;
            }
            .left-section {
                width: 50%;
            }
            .right-section {
                width: 50%;
            }
            .scanned-items ul {
                list-style: none;
                padding: 0;
            }
            .scanned-items ul li {
                display: flex;
                justify-content: space-between;
            }
            .keyboard {
                display: flex;
                flex-direction: column;
            }
            .keyboard-row {
                display: flex;
            }
            .key {
                padding: 10px;
                margin: 2px;
                border: 1px solid #ccc;
                cursor: pointer;
                user-select: none;
            }
            #interactive {
                width: 100%;
                height: 400px;
            }
            #barcode-scanner {
                position: fixed;
                top: -100px; /* Move off-screen */
                left: -100px; /* Move off-screen */
                width: 1px; /* Set minimal width */
                height: 1px; /* Set minimal height */
                opacity: 0; /* Hide visually */
                pointer-events: none; /* Disable interaction */
                z-index: -1; /* Ensure it's behind other elements */
            }
            table {
                width: 100%;
                border-collapse: collapse;
            }
            th, td {
                border: 1px solid black;
                padding: 8px;
                text-align: left;
            }
            th {
                background-color: #f2f2f2;
            }
        </style>
    </head>


<body>
    <div class="container">
        <div class ="left-section">
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
        </div>
                        
        <div class="payment-section">
            <div class="manual-entry-section">
                <form id="product-form" action="ProductServlet" method="post">
                    <div class="manual-entry">
                        <input type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                        <button type="submit" name="submit" value="Add-Item">Add Item</button>
                        <button type="submit" name="submit" value="auto-submit" id = "auto-submit" style ="display: none" ></button>
                    </div>
                    <div>
                        <label for="payment_method">Payment Method:</label>
                        <select id="payment_method" name="payment_method">
                            <option value="cash">Cash</option>
                            <option value="card">Card</option>
                            <option value="cardAndcash">Card & Cash</option>
                        </select>
                    </div>
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
                    <div class="key" onclick="clearInput()">C</div>
                    <div class="key" onclick="backspace()">Backspace</div>
                </div>
            </div>
             <form id="product-form" action="ProductServlet" method="post">

            <div class="transaction-buttons">
                <button>Return Item</button>
                <button>IBT Purchase</button>
                <button>View Reports</button>
                <button>Void Sale</button>
                <button>Remove Item</button>
                <button>Search Item</button>
                <button>Lay Away</button>
                <button type="submit" name ="submit" value ="Inventory">Inventory Management</button>
            </div>
            </form>
        </div>
    </div>
    <audio id="beep-sound" src="beep.mp3" preload="auto"></audio>
    <div id="barcode-scanner"></div>
    <script>
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
                    console.error('Quagga initialization failed: ', err);
                    return;
                }
                console.log("Quagga initialization finished. Ready to start");
                Quagga.start();
            });

            Quagga.onProcessed(function (result) {
                var drawingCtx = Quagga.canvas.ctx.overlay,
                    drawingCanvas = Quagga.canvas.dom.overlay;

                if (result) {
                    if (result.boxes) {
                        drawingCtx.clearRect(0, 0, drawingCanvas.width, drawingCanvas.height);
                        result.boxes.filter(function (box) {
                            return box !== result.box;
                        }).forEach(function (box) {
                            Quagga.ImageDebug.drawPath(box, {
                                x: 0,
                                y: 1
                            }, drawingCtx, {
                                color: "green",
                                lineWidth: 2
                            });
                        });
                    }
                    if (result.box) {
                        Quagga.ImageDebug.drawPath(result.box, {
                            x: 0,
                            y: 1
                        }, drawingCtx, {
                            color: "#00F",
                            lineWidth: 2
                        });
                    }
                    if (result.codeResult && result.codeResult.code) {
                        console.log("Code detected: " + result.codeResult.code);
                    }
                }
            });

            Quagga.onDetected(function (data) {
                let barcode = data.codeResult.code;
                console.log("Barcode detected and processed: [" + barcode + "]", data);
                
                // Play beep sound
                   document.getElementById('beep-sound').play();

                // Simulate keyboard input
                simulateKeyboardInput(barcode);

                // Pause scanning for 2 seconds
                Quagga.stop();
                setTimeout(() => Quagga.start(), 2000); // Restart Quagga after 2 seconds
            });
        }

        function simulateKeyboardInput(barcode) {
            let inputField = document.querySelector('#manual-sku');
            inputField.value = barcode;
            
            document.getElementById('auto-submit').click();
            document.forms['product-form'].submit();
        }

        function appendToInput(value) {
            document.querySelector('#manual-sku').value += value;
        }

        function clearInput() {
            document.querySelector('#manual-sku').value = '';
        }

        function backspace() {
            let inputField = document.querySelector('#manual-sku');
            inputField.value = inputField.value.slice(0, -1);
        }
    </script>
</body>
</html>
