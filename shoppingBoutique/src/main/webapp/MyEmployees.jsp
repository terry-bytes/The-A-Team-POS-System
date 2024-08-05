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
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/myemployees.css">
    </head>
    <body>
        <section>
    <% List<Employee> employees = (List<Employee>) request.getSession(false).getAttribute("MyEmployees");
        Store store = (Store) request.getSession(false).getAttribute("store");
        String message = (String) request.getSession(false).getAttribute("message");
        Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
        
        if (employee != null && employee.getRole() == Role.Manager){
    %>
    <jsp:include page="sidebar.jsp"/>
    <div class="left-main">
        <div class="header">
            <h4>Employees for <%= store.getStore_name()%></h4>
            <% if (message != null){%><p><%= message %></p><%}%>
            <a href="EmployeeServlet?submit=getAddEmployee">Add Employee</a>
            <!-- Search Box -->
            <input type="text" id="searchInput" onkeyup="filterTable()" placeholder="Search for employees..">
        </div>
              
    <% if (employees != null && !employees.isEmpty()){
        int pageSize = 10;
        int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
        int totalRows = employees.size();
        int totalPages = (int) Math.ceil((double) totalRows / pageSize);

        int start = (currentPage - 1) * pageSize;
        int end = Math.min(start + pageSize, totalRows); %>
        <table id="customers">
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
