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
         <style>
            *{
            margin: 0;
            padding: 0;
            font-family: 'Poppins', sans-serif;
            box-sizing: border-box;
        }
        body{
            background: #aecdf0;
            display: flex;
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
            button {
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
            button:hover {
                background-color: #2980b9;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
                background-color: #fff;
            }
            th, td {
                padding: 10px;
                text-align: left;
                border-bottom: 1px solid #ddd;
            }
            th {
                background-color: #f2f2f2;
                color: #333;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #f1f1f1;
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
        </style>
    </head>
    <body>
        <%  List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList"); 
        

            int pageSize = 20;
            int currentPage = request.getParameter("page") != null ? Integer.parseInt(request.getParameter("page")) : 1;
            int totalRows = employeeList.size();
            int totalPages = (int) Math.ceil((double) totalRows / pageSize);

            int start = (currentPage - 1) * pageSize;
            int end = Math.min(start + pageSize, totalRows);
        
            %>
        
        
        <h1>Please choose an option</h1>
        <form action="AdminServlet" method="post">
        <label>View all employees</label>
        <button name="admin_switch" value="View">View</button>
        <!-- Display all layaways in a table -->
        <table border="1">
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
                    for (Employee employees : employeeList) {
                %>
                <tr>
                    
                    <td><%= employees.getEmployees_id() %></td>
                    <td><%= employees.getFirstName() %></td>
                    <td><%= employees.getLastName() %></td>
                    <td><%= employees.getStore_ID() %></td>
                    <td><%= employees.getRole() %></td>
                    <td><%= employees.getEmail() %></td>
                    <td>
                        <a href="EmployeeServlet?submit=edit">Update</a>
                        <a href="#">Delete</a>
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
        
        <label>Add Managers</label>
        <button name="admin_switch" value="Add Manager">Add</button>
        
        <label>View Reports</label>
        <button name="admin_switch" value="View Report">View</button>
        
        <label>Add a Store</label>
        <button name="admin_switch" value="Add Store">Add</button>
        
        </form>
    </body>
</html>
