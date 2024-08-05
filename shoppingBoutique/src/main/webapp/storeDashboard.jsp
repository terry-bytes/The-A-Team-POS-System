<%@page import="ateam.Models.Role"%>
<%@page import="ateam.DTO.SalesDTO"%>
<%@page import="ateam.Models.Sale"%>
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
        
        <style>
            *{
            margin: 0;
            padding: 0;
            font-family: 'Poppins', sans-serif;
            box-sizing: border-box;
        }
        body{
            background-image: linear-gradient(to right, #277af6, #0098f7, #48b2ef, #85c7e5, #bfd9e0);
            width: 100%;
            height: 100vh;
        }
        section{
            display: flex;
        }
        .left-main{
            margin-left: 300px;
            padding-left: 20px;
            margin-right: 300px;
        }
            h1 {
                color: #333;
            }
            form {
                margin-bottom: 20px;
            }
            label {
                font-weight: bold;
                margin-right: 10px;
            }
            .btn-submit {
                background-color: #3498db; 
                border: none; 
                color: white; 
                padding: 8px; /* Some padding */
                text-align: center; /* Centered text */
                text-decoration: none; /* Remove underline */
                display: inline-block; /* Make the button inline */
                font-size: 16px; /* Increase font size */
                margin: 4px 4px; /* Some margin */
                cursor: pointer; /* Pointer/hand icon */
                border-radius: 5px; /* Rounded corners */
                transition: background-color 0.3s;
            }
            .btn-submit:hover {
                background-color: #2980b9;
            }
            
            
            .pagination {
    display: flex;
    justify-content: center;
    margin-top: 20px;
}

.pagination a {
    margin: 0 5px;
    padding: 8px 16px;
    text-decoration: none;
    color: #007bff;
    border: 1px solid #dee2e6;
    border-radius: 4px;
}

.pagination a.active {
    background-color: #007bff;
    color: white;
    border: 1px solid #007bff;
}

.pagination a:hover:not(.active) {
    background-color: #ddd;
}


        .emp-tb {
            font-family: sans-serif;
            width: 100%;
            background-color: rgb(255, 255, 255);
            border-collapse: collapse;
            font-size: 1em;
            margin: 20px 0;
            border-radius: 5px 5px 0 0;
            overflow: hidden;
        }
        .emp-tb thead tr{
            font-family: sans-serif;
            background-color: #3498db;
            text-align: left;
            padding-top: 12px;
            padding-bottom: 12px;
            color: #ffffff;
        }
        
        .emp-tb th,
        .emp-tb td{ 
            padding: 12px 15px;
        }
        .emp-tb tbody tr{
            border-bottom: 1px solid #ddd;
        }
        .emp-tb tbody tr:nth-of-type(even){
            background-color: #f3f3f3;
        }
        .emp-tb tbody tr:last-of-type{
            border-bottom: 2px solid #3498db;
        }
        .emp-tb .icon{
            min-width: 60px;
            border-radius: 6px;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            color: #5ba4e7;
            transition: 0.3s ease;
        }
        .icon-button {
            background: none;
            border: none;
            cursor: pointer;
        }

        .icon-button i {
            font-size: 1.2em;
        }
        .emp-tb tbody tr:hover{
            color: #3498db;
            font-weight: 400;
        }
        .myEmployees{
            margin-top: 30px;
        }
         h2{
            color: #fff;
            font-weight: 600;
        }
        .header { 
    padding: 20px; /* Space inside the header */
    border-bottom: 1px solid #ddd; /* Bottom border for separation */
    display: flex; /* Use flexbox for horizontal alignment */
    align-items: center; /* Center items vertically */
    justify-content: space-between; /* Distribute space between items */
}
.styled-input {
    
    padding: 5px 8px; 
    margin: 8px 0;
    border: 2px solid #007bff; 
    border-radius: 10px; 
    font-size: 16px; 
    transition: border-color 0.3s; 
    background: rgba(254, 254, 254, 0.5);
}

.styled-input:focus {
    border-color: #3498db; 
    outline: none; 
}
         </style>
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            List<Employee> employees = (List<Employee>) request.getSession(false).getAttribute("employees");
            List<SalesDTO> sales = (List<SalesDTO>) request.getSession(false).getAttribute("myStoreSales");
           String message = (String) request.getAttribute("message");
           if(employee != null){
        %>
        <% if (employee.getRole() == Role.Manager){%>
        <jsp:include page="sidebar.jsp" />
        <% } else {%>
        <jsp:include page="managerSidebar.jsp"/>
        <% }%>
        <div class="left-main">
            <div class="header">
                <label>Search</label>
                <input class="styled-input" type="text" id="searchInput" onkeyup="filterTable()" placeholder="Search for employees..">
            </div>
                
                <% if (sales != null && !sales.isEmpty()){%>
                    <table id="sales" class="emp-tb">
                        <thead>
        <tr>
          <th>Sale Id</th>
          <th>Teller name</th>
          <th>Payment Method</th>
          <th>Sales Date</th>
          <th>Total Amount</th>
          <th>Action</th>
        </tr>
                        </thead>
        <tbody>
            <% for (SalesDTO sale : sales){%>
            <tr>
                <td><%= sale.getSaleId() %></td>
                <td><%= sale.getTeller() %></td>
                <td><%= sale.getPaymentMethod() %></td>
                <td><%= sale.getSalesDate() %></td>
                <td>R <%= sale.getTotalAmount() %></td>
                <td>
                    <form action="AdminServlet" method="post">
                        <input name="saleId" value="<%= sale.getSaleId()%>" type="hidden">
                        <button class="btn-submit" name="admin_switch" value="viewSaleItems" type="submit" >View Items</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </tbody>
        </table><% } %>
                
        </div>
        <%}%>
        <script>
            function filterTable() {
    var input, filter, table, tr, td, i, j;
    input = document.getElementById('searchInput');
    filter = input.value.toUpperCase();
    table = document.getElementById('sales');
    tr = table.getElementsByTagName('tr');

    for (i = 1; i < tr.length; i++) { // Start at 1 to skip the header row
        tr[i].style.display = 'none'; // Hide all rows initially
        td = tr[i].getElementsByTagName('td');
        for (j = 0; j < td.length; j++) {
            if (td[j] && td[j].innerHTML.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = ''; // Show row if it matches
                break; // Stop checking other columns in this row
            }
        }
    }
}
</script>
    </body>
</html>
