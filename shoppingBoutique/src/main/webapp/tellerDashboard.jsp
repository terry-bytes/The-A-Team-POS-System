
<%@page import="ateam.Models.Layaway"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<!DOCTYPE html>
<html>
    <head>
        <title>Barcode Scanner</title>
        <!-- Include jQuery library -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>

        <style>
/* General Popup Styles */
.popup {
    display: none; /* Hidden by default */
    position: fixed;
    z-index: 1;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    overflow: auto;
    background-color: rgba(0, 0, 0, 0.5); /* Black with opacity */
}

/* Popup Content */
.popup-content {
    background-color: #ffffff;
    margin: 10% auto; /* Center the popup */
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
    width: 80%;
    max-width: 500px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center; /* Center content horizontally */
}

/* Close Button */
.close {
    position: absolute;
    top: 10px;
    right: 10px;
    color: #888;
    font-size: 1.5em;
    font-weight: bold;
    cursor: pointer;
}

.close:hover,
.close:focus {
    color: #000;
    text-decoration: none;
}

/* Heading */
.popup-content h2 {
    margin-top: 0;
    color: #333;
    font-size: 1.6em;
}

/* Form Elements */
.popup-content label {
    display: block;
    margin-bottom: 8px;
    color: #555;
    font-weight: bold;
}

.popup-content input[type="text"] {
    width: calc(100% - 20px); /* Ensure full width minus padding */
    padding: 10px;
    margin-bottom: 15px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 1em;
    box-sizing: border-box; /* Ensure padding doesn't affect width */
}

/* Button Container */
.button-container {
    display: flex;
    justify-content: center; /* Center buttons horizontally */
    width: 100%; /* Ensure full width of the parent container */
    gap: 10px; /* Add space between buttons */
}

/* Button Styles */
.popup-content input[type="submit"] {
    background-color: #3498db; /* Blue background */
    border: none;
    color: white;
    padding: 10px 20px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    margin: 4px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s ease;
}

.popup-content input[type="submit"]:hover {
    background-color: #2980b9; /* Darker blue */
}

        </style>
        
        
        <style>
            
.payment-section {
    flex: 1;
}
.payment-icons{
    display: flex;
    width: 100%;
    justify-content: space-around
}
.payment-icons img {
    width: 50px;
    cursor: pointer;
    margin-left: 15px;
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

    background: #aecdf0;
}



.styled-button{
    background-color: #3498db; 

    border: none; 
    color: white; 
    padding: 15px 20px; /* Some padding */
    text-align: center; /* Centered text */
    text-decoration: none; /* Remove underline */
    display: inline-block; /* Make the button inline */
    font-size: 16px; /* Increase font size */
    margin: 4px 2px; /* Some margin */
    cursor: pointer; /* Pointer/hand icon */
    border-radius: 5px; /* Rounded corners */
    transition: background-color 0.3s;
}


#complete{
    background-color: #f48106;
}

#IBT{
    background-color: #ff0000;
}

#lay{
    background-color: #6bf406;}

#openPopupButton{
    background-color: red;
}
.styled-input {
    width: 100%; 
    padding: 12px 20px; 
    margin: 8px 0;
    border: 2px solid #630467; 
    border-radius: 4px; 
    font-size: 16px; 
    transition: border-color 0.3s; 
    background: rgba(254, 254, 254, 0.5);
}

.styled-input:focus {
    border-color: #3498db; 
    outline: none; 
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
    background: #fff;
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
.manual-entry {
    display: flex;
    align-items: center;
}

.manual-entry input {
    margin-right: 10px;
}

.manual-entry .green-arrow-button {
    margin-right: 10px;
}

.manual-entry-section {
    background: rgba(254, 254, 254, 0.3);
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
    /*display: flex;
    justify-content: flex-start; /* Aligns the keyboard to the left 
    align-items: center;
    margin-left: 5px;*/
}

.keyboard-wrapper {
    display: grid;
    grid-template-columns: repeat(12, 1fr); /* 12 columns for numbers and special characters */
    gap: 5px; /* Space between keys */
}

.key {
    padding: 10px;
    background-color: #3498db;
    border: 1px solid #ccc;
    text-align: center;
    cursor: pointer;
    border-radius: 4px;
    user-select: none;
}

.key.big-key {
    grid-column: span 3; /* Make the Shift and Backspace keys wider */
}

.key:hover {
    background-color: #2980b9;
}



/* Adjust the layout for the QWERTY keys */
.key:nth-child(n+13):nth-child(-n+22) {
    grid-column: span 1;
}

.key:nth-child(n+23):nth-child(-n+31) {
    grid-column: span 1;
}

.key:nth-child(n+32):nth-child(-n+39) {
    grid-column: span 1;
}



.manual-entry {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}
.transaction-buttons {
    display: flex;
    justify-content: space-between; 
    
    flex-wrap: wrap;
    gap: 20px; /* Adjust the spacing between buttons as needed */
}

.transaction-buttons form {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.transaction-buttons button {
    background: none;
    border: none;
    padding: 0;
    cursor: pointer;
}

.transaction-buttons .icon {
    width: 50px; /* Adjust the size as needed */
    height: auto;
}

.transaction-buttons label {
    margin-top: 5px; /* Adjust the spacing as needed */
    text-align: center;
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
    flex-direction: row;

}

.user-info img {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 10px;
}



.user-details {
    display: flex;
    align-items: center;
}



.logout {
    list-style: none;
    right: 10px;
}

.payment-method {
    margin-top: 20px;
}

.payment-method div {
    margin-bottom: 15px;
}

.flex {
    display: flex;
    justify-content: space-between;
}

.flex .inputBox {
    flex: 1;
    margin-right: 10px;
}

.flex .inputBox:last-child {
    margin-right: 0;
}

.payment-method label {
    display: block;
    margin-bottom: 5px;
    color: #555;
}

.payment-method input {
    width: 100%;
    padding: 10px;
    border: 2px solid #ccc; 
    border-radius: 4px; 
    transition: border-color .3s;
}

.payment-method input:focus{
    border-color: #3498db; 
    outline: none;
}

.submit-btns{
    margin-top: 10px;
    display: flex;
    justify-content: space-between;
}
        </style>
        
        <style>
    /* The Popup Background */
    .popup {
        display: none; /* Hidden by default */
        position: fixed;
        z-index: 1;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgba(0, 0, 0, 0.5); /* Black w/ opacity */
    }
    /* Popup Content */
    .popup-content {
        background-color: #ffffff;
        margin: 10% auto; /* Center the popup */
        padding: 20px;
        border: 1px solid #ccc;
        border-radius: 8px;
        width: 80%;
        max-width: 500px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    }
    /* Close Button */
    .close {
        color: #888;
        float: right;
        font-size: 1.5em;
        font-weight: bold;
        cursor: pointer;
    }
    .close:hover,
    .close:focus {
        color: #000;
        text-decoration: none;
    }
    /* Heading */
    .popup-content h2 {
        margin-top: 0;
        color: #333;
        font-size: 1.6em;
    }
    /* Form Elements */
    label {
        display: block;
        margin-bottom: 8px;
        color: #555;
        font-weight: bold;
    }
    input[type="text"] {
        width: calc(100% - 20px);
        padding: 10px;
        margin-bottom: 15px;
        border: 1px solid #ddd;
        border-radius: 4px;
        font-size: 1em;
    }
    /* Store Info */
    .store-info {
        margin-bottom: 20px;
    }
    .store-info label {
        font-weight: normal;
    }
    /* Button Group */
    .button-group {
        display: flex;
        justify-content: space-between;
    }
    /* Buttons */
    .btn {
        padding: 10px 15px;
        border: none;
        border-radius: 4px;
        color: #fff;
        font-size: 1em;
        cursor: pointer;
        transition: background-color 0.3s ease;
    }
    .btn-primary {
        background-color: #007bff;
    }
    .btn-primary:hover {
        background-color: #0056b3;
    }
    .btn-secondary {
        background-color: #28a745;
    }
    .btn-secondary:hover {
        background-color: #218838;
    }
</style>


        <!-- Add styles for popups -->
    <style>
    .popup {
        display: none; /* Hidden by default */
        position: fixed;
        z-index: 1;
        left: 0;
        top: 0;
        width: 100%;
        height: 100%;
        overflow: auto;
        background-color: rgb(0,0,0); /* Fallback color */
        background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
    }
    .popup-content {
        background-color: #fefefe;
        margin: 15% auto;
        padding: 20px;
        border: 1px solid #888;
        width: 80%;
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
            .header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 10px 20px;
                background-color: #f1f1f1;
                border-bottom: 1px solid #ddd;
            }

            .header .user-info {
                display: flex;
                align-items: center;
            }

            .header .user-info img {
                border-radius: 50%;
                width: 50px;
                height: 50px;
                margin-right: 10px;
            }

            .header .user-info .user {
                display: flex;
                flex-direction: column;
            }

            .header .logout a {
                text-decoration: none;
                color: #333;
                display: flex;
                align-items: center;
            }

            .header .logout i {
                font-size: 24px;
                margin-right: 8px;
            }
            .payment-icons {
                display: flex;
                gap: 10px; /* Adjust the spacing between options */
            }

            .payment-option {
                text-align: center;
                cursor: pointer;
            }

            .payment-option img {
                display: block;
                margin: 0 auto;
                width: 50px; /* Adjust the size of the icons */
                height: 50px;
            }

            .payment-option span {
                display: block;
                margin-top: 5px;
                font-size: 14px; /* Adjust the font size */
            }

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
            /* The Popup Background */
            .popup {
                display: none; /* Hidden by default */
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgba(0, 0, 0, 0.5); /* Black w/ opacity */
            }
            /* Popup Content */
            .popup-content {
                background-color: #ffffff;
                margin: 10% auto; /* Center the popup */
                padding: 20px;
                border: 1px solid #ccc;
                border-radius: 8px;
                width: 80%;
                max-width: 500px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }
            /* Close Button */
            .close {
                color: #888;
                float: right;
                font-size: 1.5em;
                font-weight: bold;
                cursor: pointer;
            }
            .close:hover,
            .close:focus {
                color: #000;
                text-decoration: none;
            }
            /* Heading */
            .popup-content h2 {
                margin-top: 0;
                color: #333;
                font-size: 1.6em;
            }
            /* Form Elements */
            label {
                display: block;
                margin-bottom: 8px;
                color: #555;
                font-weight: bold;
            }
            input[type="text"] {
                width: calc(100% - 20px);
                padding: 10px;
                margin-bottom: 15px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 1em;
            }
            /* Store Info */
            .store-info {
                margin-bottom: 20px;
            }
            .store-info label {
                font-weight: normal;
            }
            /* Button Group */
            .button-group {
                display: flex;
                justify-content: space-between;
            }
            /* Buttons */
            .btn {
                padding: 10px 15px;
                border: none;
                border-radius: 4px;
                color: #fff;
                font-size: 1em;
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
            .btn-primary {
                background-color: #007bff;
            }
            .btn-primary:hover {
                background-color: #0056b3;
            }
            .btn-secondary {
                background-color: #28a745;
            }
            .btn-secondary:hover {
                background-color: #218838;
            }
        </style>


        <!-- Add styles for popups -->
        <style>
            .popup {
                display: none; /* Hidden by default */
                position: fixed;
                z-index: 1;
                left: 0;
                top: 0;
                width: 100%;
                height: 100%;
                overflow: auto;
                background-color: rgb(0,0,0); /* Fallback color */
                background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
            }
            .popup-content {
                background-color: #fefefe;
                margin: 15% auto;
                padding: 20px;
                border: 1px solid #888;
                width: 80%;
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
            /* poooooooopuuups */



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
                <header class="header">
                    <div class="user-info">
                        <img src="images.jpeg" alt="User Avatar">
                        <div class="user">
                            <%
                                Employee loggedInUser = (Employee) session.getAttribute("Employee");
                            %>
                            <h3><%= loggedInUser.getFirstName()%> <%= loggedInUser.getLastName()%></h3>
                        </div>
                    </div>
                    <div class="logout">
                        <a href="EmployeeServlet?submit=logout">
                            <i class='bx bx-log-out icon'></i>
                            <span class="text nav-text">Logout</span>
                        </a>
                    </div>
                </header>

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
                    <form id="product-form" action="ProductServlet" method="post" onsubmit="return validateForm()">
                        <input type="hidden" id="payment-method" name="payment_method" value="">
                        <div class="manual-entry">
                            <input class="styled-input" type="text" id="manual-sku" name="input-field" placeholder="Enter SKU manually">
                            <button type="submit" name="submit" value="Add-Item" class="styled-button">OK</button>
                            <button type="submit" name="submit" value="auto-submit" id="auto-submit" style="display: none"></button>
                        </div>
                        <div class="payment-icons">

                            <div class="payment-option">
                                <img src="Icons/cashhh.png" alt="Cash" onclick="selectPaymentMethod('cash')">
                                <span>Cash</span>
                            </div>
                            <div class="payment-option">
                                <img src="Icons/290142_business_card_cash_credit_money_icon.png" alt="Card" onclick="selectPaymentMethod('card')">
                                <span>Card</span>
                            </div>
                            <div class="payment-option">
                                <img src="Icons/wallet.png" alt="Card & Cash" onclick="selectPaymentMethod('cardAndcash')">
                                <span>Card & Cash</span>
                            </div>
                            <div class="payment-option">
                                <img src="Icons/gift-card.png" alt="voucher" onclick="selectPaymentMethod('voucher')">
                                <span>voucher</span>
                            </div>

                        </div>
                        <c:if test="${not empty errorMessage}">
                            <div class="error-message">${errorMessage}</div>
                        </c:if>

                        <p>   </p>


                        <div class="payment-method" id="card-details" style="display:none;">
                            <div>
                                <label for="card_number">Card Number:</label>
                                <input type="text" id="card_number" name="card_number" placeholder="Card Number">
                            </div>
                            <div class="flex">
                                <div class="inputBox">
                                    <label for="expiry_date">Expiry Date:</label>
                                    <input type="text" id="expiry_date" name="expiry_date" placeholder="Expity Date">
                                </div>
                                <div class="inputBox">
                                    <label for="cvv">CVV:</label>
                                    <input type="text" id="cvv" name="cvv" placeholder="CVV">
                                </div>
                            </div>
                        </div>
                        <div class="payment-method" id="cash-amount" style="display:none;">
                            <div>
                                <label for="cash_amount">Cash Amount:</label>
                                <input type="text" id="cash_amount" name="cash_amount" placeholder="Cash Amount">
                            </div>
                        </div>


                        <div class="payment-method" id="cash-card-amount" style="display:none;">
                            <div class="flex">
                                <div class="inputBox">
                                    <label for="cash_amount2">Cash Amount:</label>
                                    <input type="text" id="cash_amount2" name="cash_amount2" placeholder="Cash Amount">
                                </div>
                                <div class="inputBox">
                                    <label for="card_amount2">Card Amount:</label>
                                    <input type="text" id="card_amount2" name="card_amount2" placeholder="Card Amount">
                                </div>
                            </div>
                        </div>

                        <div class="payment-method" id="voucher-code" style="display:none;">
                            <div>
                                <label for="voucher_code">Voucher Code:</label>
                                <input type="text" id="voucher_code" name="voucher_code" placeholder="Voucher Code">
                            </div>
                        </div>
                        <p>   </p>
                        <div>

                            <input class="styled-input" type="email" id="customer_email" name="customer_email" placeholder="Enter customer email" >
                        </div>
                        <p>   </p>
                        <div class="submit-btns">
                            <input type="hidden" id="scanned-items-count" name="scannedItemsCount" value="<c:out value='${fn:length(scannedItems)}'/>">

                            <button class="styled-button" id="complete" type="submit" name="submit" value="Complete-Sale">Complete Sale</button>
                            <input class="styled-button" id="lay" type="submit" value="Process Layaway" onclick="openPopup()">
                            <input class="styled-button"  type="button" value="Process IBT" id="openPopupButton">

                        </div>
                    </form>
                    <p>   </p>

                    <div class="transaction-buttons">

                        <form action="ReturnServlet" method="post">
                            <button type="submit" name="submit" value="return" title ="Return Item">
                                <img src="https://th.bing.com/th/id/OIP.-YCUILzwkqhEWv0dTnBCxgHaHa?w=800&h=800&rs=1&pid=ImgDetMain" alt="Return Item" class="icon"> 
                            </button>
                            <label>Return Item</label>
                        </form>
                        <form action="LayawayDashboard.jsp" method="post"  class="layaway-form">
                            <button type="submit" onclick="redirectToAnotherPage()" title="Lay Away">
                                <img src="Icons/172576_box_icon.png" alt="Lay Away" class="icon">
                            </button>
                            <label>Lay Away</label>
                        </form>
                        <form action="VoidSaleServlet" method="post">
                            <button type="submit" title="Void Sale">
                                <img src="Icons/8140875_pos_void_ticket_cancal_cinema_icon.png" alt="Void Sale" class="icon">
                            </button>
                            <label>Void Sale</label>
                        </form>
                        <form action="Search.jsp" method="post">
                            <button type="submit" title="Search Item">
                                <img src="Icons/211818_search_icon.png" alt="Search Item" class="icon">
                            </button>
                            <label>Search Items</label>
                        </form>
                        <form action="ProductServlet" method="post">
                            <button type="submit" name="submit" value="Inventory" title="Inventory Management">
                                <img src="https://static.vecteezy.com/system/resources/previews/015/890/404/non_2x/checklist-parcel-icon-outline-delivery-box-vector.jpg" alt="Inventory Management" class="icon">
                            </button>
                            <label>Inventory </label>
                        </form>

                    </div>
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
                            <div class="key" onclick="appendToInput('@')">@</div> 
                            <div class="key big-key" onclick="backspace()">&#9003;</div>
                        </div>
                    </div>

                </div>





            </div>
        </div>



        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Get the popup and button elements
                var popup = document.getElementById("popupForm");
                var btn = document.getElementById("openPopupButton");
                var close = document.getElementById("closePopup");

                // When the user clicks the button, open the popup
                btn.onclick = function () {
                    popup.style.display = "block";
                };

                // When the user clicks on <span> (x), close the popup
                close.onclick = function () {
                    popup.style.display = "none";
                };

                // When the user clicks anywhere outside of the popup, close it
                window.onclick = function (event) {
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
                document.getElementById("payment-method").value = method;

                // Hide or show payment details based on method
                document.getElementById("card-details").style.display = "none";
                document.getElementById("cash-card-amount").style.display = "none";
                document.getElementById("cash-amount").style.display = "none";
                document.getElementById("voucher-code").style.display = "none";

                if (method === 'card') {
                    document.getElementById("card-details").style.display = "block";
                } else if (method === 'cardAndcash') {
                    document.getElementById("cash-card-amount").style.display = "block";
                    document.getElementById('card-details').style.display = 'block';
                } else if (method === 'cash') {
                    document.getElementById("cash-amount").style.display = "block";
                } else if (method === "voucher") {
                    document.getElementById("voucher-code").style.display = "block";
                }
            }

            function validateForm() {
                var paymentMethod = document.getElementById("payment-method").value;

                if (paymentMethod === 'card' || paymentMethod === 'cardAndcash') {
                    var cardNumber = document.getElementById("card_number").value;
                    var expiryDate = document.getElementById("expiry_date").value;
                    var cvv = document.getElementById("cvv").value;

                    if (!cardNumber || !expiryDate || !cvv) {
                        alert("Please fill in all card details.");
                        return false;
                    }
                }

                if (paymentMethod === 'cash' || paymentMethod === 'cardAndcash') {
                    var cashAmount = document.getElementById("cash_amount").value;

                    if (!cashAmount || isNaN(cashAmount) || parseFloat(cashAmount) <= 0) {
                        alert("Please enter a valid cash amount.");
                        return false;
                    }
                }

                if (paymentMethod === 'cardAndcash') {
                    var cardAmount2 = document.getElementById("card_amount2").value;
                    var cashAmount2 = document.getElementById("cash_amount2").value;

                    if (!cardAmount2 || isNaN(cardAmount2) || parseFloat(cardAmount2) <= 0) {
                        alert("Please enter a valid card amount.");
                        return false;
                    }
                    if (!cashAmount2 || isNaN(cashAmount2) || parseFloat(cashAmount2) <= 0) {
                        alert("Please enter a valid cash amount.");
                        return false;
                    }
                }

                return true;
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
        
         <script>
    document.addEventListener('DOMContentLoaded', function() {
        // Get the popup elements
        var popup = document.getElementById("popupForm");
        var successPopup = document.getElementById("successPopup");
        var btn = document.querySelector('input[value^="Process Payment"]'); // Selects the "Process Payment" button
        var closePopup = document.getElementById("closePopup");
        var closeSuccessPopup = document.getElementById("closeSuccessPopup");
        // Function to open the success popup
        function showSuccessPopup() {
            successPopup.style.display = "block";
        }
        // When the user clicks the "Process Payment" button, show the success popup
        btn.onclick = function(event) {
            event.preventDefault(); // Prevent form submission for demonstration purposes
            showSuccessPopup();
        };
        // When the user clicks on <span> (x) in success popup, close the success popup
        closeSuccessPopup.onclick = function() {
            successPopup.style.display = "none";
        };
        // When the user clicks on <span> (x) in the main popup, close the main popup
        closePopup.onclick = function() {
            popup.style.display = "none";
        };
        // When the user clicks anywhere outside of the success popup, close it
        window.onclick = function(event) {
            if (event.target === successPopup) {
                successPopup.style.display = "none";
            }
        };
        // When the user clicks anywhere outside of the main popup, close it
        window.onclick = function(event) {
            if (event.target === popup) {
                popup.style.display = "none";
            }
        };
    });
</script>

        <script>
            document.addEventListener('DOMContentLoaded', function () {
                // Get the popup elements
                var popup = document.getElementById("popupForm");
                var successPopup = document.getElementById("successPopup");
                var btn = document.querySelector('input[value^="Process Payment"]'); // Selects the "Process Payment" button
                var closePopup = document.getElementById("closePopup");
                var closeSuccessPopup = document.getElementById("closeSuccessPopup");
                // Function to open the success popup
                function showSuccessPopup() {
                    successPopup.style.display = "block";
                }
                // When the user clicks the "Process Payment" button, show the success popup
                btn.onclick = function (event) {
                    event.preventDefault(); // Prevent form submission for demonstration purposes
                    showSuccessPopup();
                };
                // When the user clicks on <span> (x) in success popup, close the success popup
                closeSuccessPopup.onclick = function () {
                    successPopup.style.display = "none";
                };
                // When the user clicks on <span> (x) in the main popup, close the main popup
                closePopup.onclick = function () {
                    popup.style.display = "none";
                };
                // When the user clicks anywhere outside of the success popup, close it
                window.onclick = function (event) {
                    if (event.target === successPopup) {
                        successPopup.style.display = "none";
                    }
                };
                // When the user clicks anywhere outside of the main popup, close it
                window.onclick = function (event) {
                    if (event.target === popup) {
                        popup.style.display = "none";
                    }
                };
            });
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

<!-- The Popup Form -->
<div id="popupForm" class="popup">
    <div class="popup-content">
        <span class="close" id="closePopup">&times;</span>
        <h2>Enter IBT ID Number</h2>
        <form id="ibtForm" action="IBTServlet" method="post">
            <label for="ibtNumber">IBT ID:</label>
            <input type="text" id="ibtNumber" name="ibtNumber">
            <label>Store ID of Sent IBT: </label><label id="storeID"><%= request.getAttribute("retrievedStoreID")%></label>
            <label></label><label></label>
            <label></label><label></label>
            <div class="button-container">
                <input type="submit" value="Validate Store" name="IBT_switch">
                <input type="submit" value="Process Payment to store: <%= request.getAttribute("retrievedStoreID")%>">
            </div>
        </form>
    </div>
</div>

        <!-- Success Popup -->
        <div id="successPopup" class="popup">
            <div class="popup-content">
                <span class="close" id="closeSuccessPopup">&times;</span>
                <h2>Payment Successful</h2>
                <p>Your payment has been processed successfully to store: <%= request.getAttribute("retrievedStoreID")%></p>
            </div>
        </div>
          
    </body>
</html>
