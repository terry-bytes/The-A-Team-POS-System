<%@page import="ateam.Models.Product"%>
<%@ page import="ateam.Models.Employee" %>
<%@ page import="java.util.List" %>
<%@page import="java.util.List"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Barcode Scanner</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        <style>
            body {
                font-family: Arial, sans-serif;
                display: flex;
                justify-content: space-between;
                padding: 20px;
            }

            .container {
                display: flex;
                width: 100%;
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
            <div class="left-section">
                <div>
                    <div class="user-info">
                        <img src="images.jpeg" alt="User Avatar">
                        <div>
                            <%
                                Employee loggedInUser = (Employee) session.getAttribute("Employee");
                                if (loggedInUser != null) {%>
                            <p> <%=loggedInUser.getFirstName()%>
                                <%}%>

                        </div>
                    </div>
                    <form action="ProductServlet" method="post">
                        <video id="barcode-scanner" style="display: none;"></video>
                        <input name="input-field" type="text" id="input-field" placeholder="Scanned barcode will appear here">
                        <div class="scanned-items">
                            <h2>Scanned Items</h2>
                            <%

                                List<Product> getItem = (List<Product>) request.getAttribute("getItem");
                                if (getItem == null || getItem.isEmpty()) {
                            %>
                            <p class="message">No Items found</p>
                            <%
                            } else {
                            %>

                            <table>
                                <tr>
                                    <th>Product SKU</th>
                                    <th>Name</th>
                                    <th>Quantity</th>
                                    <th>Price</th>

                                </tr>
                                <% for (Product item : getItem) {%>    
                                <tr>
                                    <td><%= item.getProduct_SKU()%></td>
                                    <td><%= item.getProduct_name()%></td>
                                    <td><%= item.getScanCount()%></td>
                                    <td><%= item.getProduct_price()%></td>

                                </tr>
                                <% } %>
                            </table>


                            <% }%>
                            <br>
                            <br>
                            <br>
                            <div class="total-price">
                                Total: <span id="total-price">0.00</span>
                            </div>
                        </div>


                        <input name ="submit" type ="submit" value ="Add-Item">
                        <button type="submit" name="submit" id="auto-submit" value ="auto-submit" style="display: none;"></button>
                    </form>
                </div>
                <button id="complete-sale">Complete Sale</button>
            </div>

            <div class="right-section">
                <div class="transaction-buttons">
                    <button>Return Item</button>
                    <button>IBT Purchase</button>
                    <button>View Reports</button>
                    <button>Void Sale</button>
                    <button>Remove Item</button>
                    <button>Search Item</button>
                    <button>Lay Away</button>
                </div>
                <div class="keyboard">
                    <input type="text" id="input-field" placeholder="Enter barcode manually">
                    <div class="keyboard-row">
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
                    </div>
                    <div class="keyboard-row">
                        <div class="key" onclick="appendToInput('Q')">Q</div>
                        <div class="key" onclick="appendToInput('W')">W</div>
                        <div class="key" onclick="appendToInput('E')">E</div>
                        <div class="key" onclick="appendToInput('R')">R</div>
                        <div class="key" onclick="appendToInput('T')">T</div>
                        <div class="key" onclick="appendToInput('Y')">Y</div>
                        <div class="key" onclick="appendToInput('U')">U</div>
                        <div class="key" onclick="appendToInput('I')">I</div>
                        <div class="key" onclick="appendToInput('O')">O</div>
                        <div class="key" onclick="appendToInput('P')">P</div>
                    </div>
                    <div class="keyboard-row">
                        <div class="key" onclick="appendToInput('A')">A</div>
                        <div class="key" onclick="appendToInput('S')">S</div>
                        <div class="key" onclick="appendToInput('D')">D</div>
                        <div class="key" onclick="appendToInput('F')">F</div>
                        <div class="key" onclick="appendToInput('G')">G</div>
                        <div class="key" onclick="appendToInput('H')">H</div>
                        <div class="key" onclick="appendToInput('J')">J</div>
                        <div class="key" onclick="appendToInput('K')">K</div>
                        <div class="key" onclick="appendToInput('L')">L</div>
                        <div class="key" onclick="appendToInput(';')">;</div>
                    </div>
                    <div class="keyboard-row">
                        <div class="key" onclick="appendToInput('Z')">Z</div>
                        <div class="key" onclick="appendToInput('X')">X</div>
                        <div class="key" onclick="appendToInput('C')">C</div>
                        <div class="key" onclick="appendToInput('V')">V</div>
                        <div class="key" onclick="appendToInput('B')">B</div>
                        <div class="key" onclick="appendToInput('N')">N</div>
                        <div class="key" onclick="appendToInput('M')">M</div>
                        <div class="key" onclick="appendToInput(',')">,</div>
                        <div class="key" onclick="appendToInput('.')">.</div>
                        <div class="key" onclick="appendToInput('/')">/</div>
                    </div>
                    <div class="keyboard-row">
                        <div class="key" id="toggleCase">Caps Lock</div>
                        <div class="key" id="atKey" onclick="appendToInput('@')"> @ </div>
                        <div class="key" onclick="appendToInput(' ')">Space</div>
                        <div class="key" id="backspace" onclick="removeLastCharacter()">Backspace</div>
                    </div>

                </div>
                <div id="interactive" class="viewport"></div>
                <div class="ready-to-scan" id="scan-status">Ready to scan...</div>
            </div>
        </div>
        <audio id="beep-sound" src="beep.mp3" preload="auto"></audio>



        <script>
            let scanningPaused = false;

            document.addEventListener('DOMContentLoaded', (event) => {
                initQuagga();
            });

            function initQuagga() {
                Quagga.init({
                    inputStream: {
                        name: "Live",
                        type: "LiveStream",
                        target: document.querySelector('#barcode-scanner'),
                    },
                    decoder: {
                        readers: ["code_128_reader", "ean_reader", "ean_8_reader"]
                    }
                }, function (err) {
                    if (err) {
                        console.log(err);
                        return;
                    }
                    console.log("Initialization finished. Ready to start");
                    Quagga.start();
                });

                Quagga.onDetected(function (data) {
                    if (!scanningPaused) {
                        let barcode = data.codeResult.code;
                        console.log("Barcode detected and processed : [" + barcode + "]", data);

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
                            initQuagga(); // Restart Quagga after pause
                        }, 2000); // 2 seconds pause
                    }
                });
            }

            function simulateKeyboardInput(barcode) {
                let inputField = document.querySelector('#input-field');
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

            function appendToInput(value) {
                document.getElementById('input-field').value += value;
            }

        </script>
    </body>
</html>
