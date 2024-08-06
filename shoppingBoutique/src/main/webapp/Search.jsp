 
<%@page import="ateam.Models.Role"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.InventorySummary"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Store Inventory Summary</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/quagga/0.12.1/quagga.min.js"></script>

    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #72c2f1;
        }

        h1 {
            text-align: center;
            color: #333;
        }

        .filter-form {
            max-width: 600px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
        }

        .filter-form label {
            display: block;
            margin: 10px 0 5px;
            color: #333;
        }

        .filter-form input[type="text"] {
            
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        .filter-form input[type="submit"] {
            display: block;
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: #fff;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            font-size: 16px;
        }

        .filter-form input[type="submit"]:hover {
            background-color: #0056b3;
        }

        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
            background: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        table th, table td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        table th {
            background-color: #007BFF;
            color: white;
        }

        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        table img {
            display: block;
            margin: auto;
            width: 50px;
            height: 50px;
            border-radius: 4px;
        }

        table a {
            text-decoration: none;
        }
    
        .sidebar img {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .sidebar a {
            font: 100% bolder;
            text-decoration: none; /* Remove underlining */
            font-size: 25px;
            color: black;
            display: block;
            
        }
        .select-container {
    margin-bottom: 20px;
    position: relative;
}

.select-container label {
    display: block;
    margin-bottom: 5px;
}

.select-box {
    width: 200px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    box-sizing: border-box;
}

    </style>
    
    
</head>
<body>
    <% List<Store> stores = (List<Store>) request.getSession(false).getAttribute("stores");
        Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
    %>
    
    <% if (employee != null && employee.getRole() == Role.Teller){%>
    <div id="mySidenav" class="sidebar">
    <a href="replenishStock.jsp" >
        <img src="Icons/back.png" alt="Icon 1"> Back
    </a><br><br>
    </div>
    <% }%>
    
    <h1>Store Inventory Summary</h1>

    <!-- Filter Form -->
    <div class="filter-form">
        <form action="storeInventorySummary" method="get">
            <div class="select-container">
                <label for="color">Filter By Color:</label>
                <select class="select-box" name="color">
                    <option value="" disabled selected>Select</option>
                    <option value="Red">Red</option>
                    <option value="Black">Black</option>
                    <option value="White">White</option>
                    <option value="Purple">Purple</option>
                    <option value="Green">Green</option>
                    <option value="Yellow">Yellow</option>
                    <option value="Blue">Blue</option>
                </select>
            
            </div>
            
            <label for="size">Filter By Size:</label>
            <input type="text" id="size" name="size"maxlength="2" size="2">

             <div class="select-container" id="storeSelector" >
                        <label for="store">Filter By Store </label>

                        <select class="select-box" name="store" id="managerStoreId">
                            <option value="" disabled selected>Select</option>
                            <% if(stores != null) {
                                for(Store store : stores) { %>
                                <option value="<%=store.getStore_ID() %>"><%=store.getStore_name() %></option>
                                <% } } %>
                        </select>
                    </div>
            <label for="sku">Filter By Product SKU:</label>
            <input type="text" id="sku" name="sku">
            
            <input type="submit" value="Filter">
        </form>
    </div>
<video id="barcode-scanner" style="display: none;"></video>
<audio id="beep-sound" src="beep.mp3" preload="auto"></audio>

    <table>
        <tr>
            <th>Store ID</th>
            <th>Product ID</th>
            <th>Product Name</th>
            <th>Product SKU</th>
            <th>Color</th>
            <th>Size</th>
            <th>Total Quantity</th>
            <th>Product Image</th>
        </tr>
        <%
            List<InventorySummary> inventorySummaries = (List<InventorySummary>) request.getAttribute("inventorySummaries");
            if (inventorySummaries != null) {
                for (InventorySummary summary : inventorySummaries) {
        %>
        <tr>
            <td><%= summary.getStoreID() %></td>
            <td><%= summary.getProductID() %></td>
            <td><%= summary.getProductName() %></td>
            <td><%= summary.getProductSKU() %></td>
            <td><%= summary.getColor() %></td>
            <td><%= summary.getSize() %></td>
            <td><%= summary.getTotalQuantity() %></td>
            <td><a href="<%= summary.getProductImagePath() %>" target="_blank"><img src="<%= summary.getProductImagePath() %>" alt="Product Image"></a></td>
        </tr>
        <%
                }
            }
        %>
    </table>
    <script>
    document.addEventListener('DOMContentLoaded', (event) => {
      Quagga.init({
        inputStream: {
          name: "Live",
          type: "LiveStream",
          target: document.querySelector('#barcode-scanner'), // Or '#yourElement' (optional)
        },
        decoder: {
          readers: ["code_128_reader", "ean_reader", "ean_8_reader"] // Add other barcode types if needed
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
        let barcode = data.codeResult.code;
        console.log("Barcode detected and processed : [" + barcode + "]", data);

        document.getElementById('beep-sound').play();
         
        // Simulate keyboard input
        simulateKeyboardInput(barcode);
      });
    });

    function simulateKeyboardInput(barcode) {
      let inputField = document.querySelector('#barcode'); // Your input field where the barcode should be entered
      inputField.value = barcode;

      // If you need to trigger events as if it was typed
      let event = new Event('input', {
        bubbles: true,
        cancelable: true,
      });
      inputField.dispatchEvent(event);
    }
  </script>


</body>

</html>
