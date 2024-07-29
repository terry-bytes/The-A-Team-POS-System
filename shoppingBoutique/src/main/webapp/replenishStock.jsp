<%@page import="ateam.Models.Product"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Inventory"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Stock</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            max-width: 400px;
            width: 100%;
        }
        h1 {
            text-align: center;
            margin-bottom: 20px;
        }
        label {
            display: block;
            margin-bottom: 5px;
        }
        input[type="text"],
        input[type="number"] {
            width: calc(100% - 22px); /* Adjust width to account for padding */
            padding: 10px;
            margin-bottom: 10px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        input[type="submit"] {
            display: inline-block;
            width: 100%;
            padding: 10px;
            background-color: #48b2ef;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-align: center; /* Center-align button text */
        }
        input[type="submit"]:hover {
            background-color: #48b2ef;
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
   
                
        .sidebar img {
            width: 45px;
            height: 45px;
            border-radius: 50%;
            margin-right: 10px;
        }
        .time {
            font-size: 1rem;
            position: absolute;
            top:  20px;
            right: 20px;
            color: black;
            
        }
        p{
          font-size: 1rem;
            position: absolute;
            top:  10px;
            right: 10px;
            color: black;  
        }
        button {
            display: block;
            margin: 20px auto;
            background: none;
            border: none;
            cursor: pointer;
        }
        button img {
            width: 50px;
            height: 50px;
        }

        
        /* Modal styles */
        .modal {
            display: none;
            position: fixed;
            z-index: 1;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            overflow: auto;
            background-color: rgb(0, 0, 0);
            background-color: rgba(0, 0, 0, 0.4);
            padding-top: 60px;
        }
         .modal-content {
            background-color: #fefefe;
            margin: 5% auto;
            padding: 20px;
            border: 1px solid #888;
            width: 80%;
            max-width: 300px;
            text-align: center;
            position: relative;
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
        .modal-content img {
            width: 50px;
            height: 50px;
            display: block;
            margin: 0px auto 10px;
        }
        #okButton {
            display: inline-block;
            margin-top: 10px;
            padding: 10px 10px;
            background-color: #48b2ef;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        #okButton:hover {
            background-color: #48b2ef;
        }
        .sidebar {
            height: 100%;
            width: 300px;
            position: fixed;
            z-index: 1;
            top: 0;
            left: 0;
            background-color: #48b2ef;
            
            overflow-x: hidden;
            padding-top: 16px;
        }
        .sidebar a {
            font: 100% bolder;
            text-decoration: none; /* Remove underlining */
            font-size: 25px;
            color: black;
            display: block;
            
        }
        .sidebar a:hover {
            background-color: white;
            color:black;
        }
        .h2{
            font:100% bold;
        }

    </style>
</head>
<body>
<%  
    List<Product> product = (List<Product>)request.getSession(false).getAttribute("foundProducts");
%>

<div class="user-info">
    <img src="images.jpeg" alt="User Avatar">
    <div>
        <%
            Employee employee = (Employee)request.getSession(false).getAttribute("Employee");
            if (employee != null) {
                out.print(employee.getFirstName() + " " + employee.getLastName());
            } else {
                out.print("Username");
            }
        %>
    </div>
</div>
    
    <!-- Sidebar -->
<div id="mySidenav" class="sidebar">
    <h2>Inventory Management </h2>
    <br><br>
    <a href="tellerDashboard.jsp" >
        <img src="Icons/back.png" alt="Icon 1"> Back
    </a><br><br>
    <a href="InventoryServlet?submit=viewAll" >
        <img src="Icons/viewList.png" alt="Icon 2"> View Inventory
    </a><br><br>
    <a href="Search.jsp" >
        <img src="Icons/searcher.png" alt="Icon 3"> Search in Stores
    </a><br><br>
    <a href="InventoryServlet?submit=logout" >
        <img src="Icons/logout.png" alt="Icon 4"> LogOut
    </a>
</div>

<div class="container">
    <h1>Add Stock</h1>
    <form action="InventoryServlet" method="post">
        <label for="barcode">Barcode (SKU-Size-Color):</label>
        <input type="text" id="barcode" name="barcode" required>
        <label for="employeeId">Employee ID:</label>
        <input type="number" id="employeeId" name="employeeId" value="<%=employee.getEmployee_ID()%>" readonly>
        <label for="additionalStock">Additional Stock:</label>
        <input type="number" id="additionalStock" name="additionalStock" required>
        <label for="storeId">Store ID:</label>
        <input type="number" id="storeId" name="storeId" value="<%=employee.getStore_ID()%>" required readonly>
        <input type="submit" name = "submit" class="button" value="Replenish Stock">
    </form>
</div>

<div class="time" id="current-time"></div>



<!-- The Modal -->
<div id="myModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span>
        <img src="button.png" alt="Success" id = "modal-content img">
        <p>Product successfully added to inventory!</p>
        <button id="okButton">OK</button>
    </div>
</div>

<script>
    var modal = document.getElementById("myModal");
    var span = document.getElementsByClassName("close")[0];
    var okButton = document.getElementById("okButton");

    span.onclick = function() {
        modal.style.display = "none";
    }

    okButton.onclick = function() {
        modal.style.display = "none";
    }

    <% if (request.getAttribute("success") != null && (Boolean) request.getAttribute("success")) { %>
        modal.style.display = "block";
    <% } %>
function openNav() {
  document.getElementById("mySidenav").style.width = "250px";
}

   function updateTime() {
        const now = new Date();
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const currentTime = `${hours}:${minutes}:${seconds}`;
        document.getElementById('current-time').innerText = currentTime;
    }

    setInterval(updateTime, 1000);
    window.onlaod =updateTime;
</script>
</body>
</html>
