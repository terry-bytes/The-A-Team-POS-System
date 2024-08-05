<%-- 
    Document   : AdminDashboard
    Created on : 30 Jul 2024, 2:39:43 PM
    Author     : carme
--%>

<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin Dashboard</title>
        <link href='https://unpkg.com/boxicons@2.1.1/css/boxicons.min.css' rel='stylesheet'>
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
            font-weight: 600;
        }
         </style>
    </head>
    <body>
        <%  List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList"); 
        

            int pageSize = 15;
            int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int totalRows = employeeList.size();
            int totalPages = (int) Math.ceil((double) totalRows / pageSize);

            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, totalRows);
        
            %>
        
        
        <h1>Please choose an option</h1>
        <form action="AdminServlet" method="post">
        <label>View all employees</label>
        <button class="btn-submit" name="admin_switch" value="View">View</button>
        <label>Add Managers</label>
        <button class="btn-submit" name="admin_switch" value="Add Manager">Add</button>
        
        <label>View Reports</label>
        <button class="btn-submit" name="admin_switch" value="View Report">View</button>
        
        <label>Add a Store</label>
        <button class="btn-submit" name="admin_switch" value="Add Store">Add</button>
                </form>

        <!-- Display all layaways in a table -->
        <div class="table-wrapper">
            <table class="emp-tb">
            <thead>
                <tr>
                    <th>Employee Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Store ID</th>
                    <th>Role</th>
                    <th>Email</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
               <%
        if (employeeList != null && !employeeList.isEmpty()) {
            for (int i = start; i < end; i++) {
                Employee employee = employeeList.get(i);
        %>
        <tr>
            <td><%= employee.getEmployees_id() %></td>
            <td><%= employee.getFirstName() %></td>
            <td><%= employee.getLastName() %></td>
            <td><%= employee.getStore_ID() %></td>
            <td><%= employee.getRole() %></td>
            <td><%= employee.getEmail() %></td>
            <td>
                 <form action="EmployeeServlet" method="post" style="display:inline;">
                            <input type="hidden" name="employeeId" value="<%= employee.getEmployees_id() %>">
                            <button type="submit" name="submit" value="edit" class="icon-button">
                                <i class='bx bx-edit icon'></i>
                            </button>
                        </form>
                        <form action="DeleteEmployeeServlet" method="post" style="display:inline;" onsubmit="return confirm('Are you sure you want to delete this employee?');">
                            <input type="hidden" name="employeeId" value="<%= employee.getEmployees_id() %>">
                            <button type="submit" name="submit" value="delete" class="icon-button">
                                <i class='bx bx-trash icon'></i>
                            </button>
                        </form>
            </td>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="8">No Employees found.</td>
        </tr>
        <%
        }
        %>
            </tbody>
        </table>
            <div class="pagination">
                            <% if (currentPage > 1) {%>
                            <a href="?page=<%= currentPage - 1%>">Previous</a>
                            <% } %>
                            <% for (int i = 1; i <= totalPages; i++) {%>
                            <a href="?page=<%= i%>" <%= (i == currentPage) ? "class='active'" : ""%>><%= i%></a>
                            <% } %>
                            <% if (currentPage < totalPages) {%>
                            <a href="?page=<%= currentPage + 1%>">Next</a>
                            <% } %>
                        </div>
        </div> 
    </body>
</html>
