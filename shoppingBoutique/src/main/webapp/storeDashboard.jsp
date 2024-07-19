<%@page import="java.lang.String"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="javax.servlet.http.HttpSession"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Barcode Scanner</title>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/store.css" />
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
           String message = (String) request.getAttribute("message");
        %>
        <jsp:include page="navbar.jsp" />
        <div class="container">
            <div class="store-form">
                <div class="heading">
                    <h4>Add Store</h4>
                    <% if(message != null){%>
                    <p> <%=message%></p>
                       <% }%>
                </div>
                <form action="StoreServlet" method="post" class="">
                    <div class="input-box">
                        <input type="text"
                               placeholder='Branch'
                               name='storeName'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    <div class="input-box">
                        <input type="text"
                            placeholder='Address'
                            name='storeAddress'
                            class='input-field'
                            autocomplete="on" required
                        />

                    </div>
                    <div class="input-box">
                        <input type="text"
                            placeholder='City'
                            name='storeCity'
                            class='input-field'
                            autocomplete="on" required
                            />
                         
                    </div>
                    <div class="input-box">
                        <input type="text"
                            placeholder='province'
                            name='storeProvince'
                            class='input-field'
                            autocomplete="off" required
                            />
                         
                    </div>
                    <div class="input-box">
                        <input type="text"
                               placeholder='Zipcode'
                               name='storeZipcode'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    <div class="input-box">
                        <input type="text"
                               placeholder='Branch Contact number'
                               name='storePhone'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    <div class="input-box">
                        <input type="email"
                               placeholder='Branch email address'
                               name='storeEmailAddress'
                               class='input-field'
                               autocomplete="off" required
                               />

                    </div>
                    
                    <div class="input-submit">
                        <input name="submit" value="Submit_Store" hidden>

                        <button class="submit-btn" id="submit">Add Employee</button>

                    </div>
                </form>
            </div>
        </div>
<<<<<<< HEAD
=======

>>>>>>> cdb254db34f95fc361f467c4ecdf655822f956ca

        <script>
            let totalPrice = 0;
            let caseToggle = false; // To track case state

            function updateTotalPrice(price) {
                totalPrice += price;
                document.getElementById('total-price').innerText = totalPrice.toFixed(2);
            }

            function appendToInput(char) {
                if (caseToggle) {
                    char = char.toLowerCase();
                }
                document.getElementById('input-field').value += char;
            }

            function clearInput() {
                document.getElementById('input-field').value = '';
            }

            function deleteLastCharacter() {
                const inputField = document.getElementById('input-field');
                inputField.value = inputField.value.slice(0, -1);
            }

            function submitInput() {
                const inputField = document.getElementById('input-field');
                // Submit the input field value to the server or process it as needed
                console.log('Input submitted:', inputField.value);
            }

            function toggleCase() {
                caseToggle = !caseToggle;
                document.getElementById('toggleCase').innerText = caseToggle ? 'Caps' : 'Shift';
            }
        
            let scannerStarted = false;
            const startBarcodeScanner = () => {
                if (scannerStarted)
                    return; // Prevent starting multiple times

                scannerStarted = true;
                Quagga.init({
                    inputStream: {
                        type: 'LiveStream',
                        target: document.querySelector('#barcode-scanner'),
                        constraints: {
                            width: 640,
                            height: 480,
                            facingMode: 'environment'
                        },
                    },
                    decoder: {
                        readers: ['code_128_reader', 'ean_reader', 'ean_8_reader', 'code_39_reader', 'code_39_vin_reader', 'codabar_reader', 'upc_reader', 'upc_e_reader', 'i2of5_reader']
                    }
                }, (err) => {
                    if (err) {
                        console.error(err);
                        return;
                    }
                    Quagga.start();
                });

                Quagga.onDetected((data) => {
                    const code = data.codeResult.code;
                    document.querySelector('#input-field').value = code;
                    Quagga.stop(); // Stop the scanner once a barcode is detected
                    scannerStarted = false; // Reset scannerStarted flag
                    document.querySelector('button[name="submit"]').click(); // Trigger the form submission
                });
            };

            document.addEventListener('DOMContentLoaded', (event) => {
                document.querySelector('#input-field').addEventListener('focus', startBarcodeScanner);
            });
        </script>

    </body>
</html>
