<%-- 
    Document   : MyEmployees
    Created on : 30 Jul 2024, 22:48:58
    Author     : T440
--%>

<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.Role"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Set"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Employee</title>
        
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
                background-color: #007bff; 
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
            background-color: #007bff;
            color: #fff;
            text-align: left;
            padding-top: 12px;
            padding-bottom: 12px;
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
            border-bottom: 2px solid #007bff;
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
            color: #007bff;
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
        <section>
    <% List<Employee> employees = (List<Employee>) request.getAttribute("employeeList");
        Store store = (Store) request.getSession(false).getAttribute("store");
        String message = (String) request.getAttribute("message");
        Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
        
        if (employee != null && employee.getRole() == Role.Manager){
    %>
    <jsp:include page="sidebar.jsp"/>
    <div class="left-main">
        <div class="header">
            <h2>Employees for <%= store.getStore_name()%></h2>
            <input class="styled-input" type="text" id="searchInput" onkeyup="filterTable()" placeholder="Search for employees..">
            <% if (message != null){%><p><%= message %></p><%}%>
            <a class="btn-submit" href="EmployeeServlet?submit=getAddEmployee">Add Employee</a>
            <!-- Search Box -->
           
        </div>
              
    <% if (employees != null && !employees.isEmpty()){
        int pageSize = 10;
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int totalRows = employees.size();
        int totalPages = (int) Math.ceil((double) totalRows / pageSize);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRows); %>
        <table id="customers" class="emp-tb">
        <tr>
          <th>First Name</th>
          <th>Last Name</th>
          <th>Email</th>
          <th>Employee Id</th>
          <th>Position</th>
          <th>Action</th>
        </tr>
        <tbody>
        <% for(int i=start; i<end; i++){%>
        <tr>
            <td><%= employees.get(i).getFirstName() %></td>
            <td><%= employees.get(i).getLastName() %></td>
            <td><%= employees.get(i).getEmail() %></td>
            <td><%= employees.get(i).getEmployees_id() %></td>
            <td><%= employees.get(i).getRole() %></td>
            <td>
                <form action="EmployeeServlet" method="get" style="display:inline;">
                    <input type="hidden" name="employeeId" value="<%= employees.get(i).getEmployee_ID() %>">
                    <button type="submit" name="submit" value="edit" class="icon-button">
                        <i class='bx bx-edit icon'></i>
                    </button>
                </form>
                <form action="EmployeeServlet" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this employee?');">
                    <input type="hidden" name="employeeId" value="<%= employees.get(i).getEmployee_ID() %>">
                    <button type="submit" name="submit" value="delete" class="icon-button">
                        <i class='bx bx-trash icon'></i>
                    </button>
                </form>
    </td>
        </tr>
        <%}%>
        </tbody>
        </table>
        <div class="pagination">
            <a href="#" onclick="changePage('previous')">Previous</a>
            <% for (int i = 1; i <= totalPages; i++) {%>
            <a href="#" onclick="changePage(<%= i %>)" <%= (i == currentPage) ? "class='active'" : ""%>><%= i %></a>
            <% } %>
            <a href="#" onclick="changePage('next')">Next</a>
        </div>
    </div>

    

<script>
let currentPage = <%= currentPage %>;
const pageSize = 10;

function changePage(action) {
    if (action === 'previous' && currentPage > 1) {
        currentPage--;
    } else if (action === 'next' && currentPage < <%= totalPages %>) {
        currentPage++;
    } else if (typeof action === 'number') {
        currentPage = action;
    }
    updateTable();
}

function updateTable() {
    const table = document.getElementById('customers');
    const tr = table.getElementsByTagName('tr');
    const start = (currentPage - 1) * pageSize + 1; // Adjust for header row
    const end = Math.min(start + pageSize - 1, tr.length - 1); // Adjust for header row

    for (let i = 1; i < tr.length; i++) { // Start at 1 to skip the header row
        tr[i].style.display = (i >= start && i <= end) ? '' : 'none';
    }

    // Update pagination links
    document.querySelectorAll('.pagination a').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`.pagination a[onclick*="${currentPage}"]`).classList.add('active');
}

// Initial call to setup table on page load
updateTable();

function filterTable() {
    var input, filter, table, tr, td, i, j;
    input = document.getElementById('searchInput');
    filter = input.value.toUpperCase();
    table = document.getElementById('customers');
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

<% } }%>
        </section>
    </body>
</html>
