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
            body {
                font-family: Arial, sans-serif;
                margin: 20px;
                background-color: #f4f4f4;
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
                background-color: #007bff;
                color: white;
                border: none;
                padding: 10px 15px;
                margin: 5px 0;
                cursor: pointer;
                border-radius: 5px;
                font-size: 16px;
            }
            button:hover {
                background-color: #0056b3;
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
        </style>
    </head>
    <body>
        <h1>Please choose an option</h1>
        <form action="AdminServlet" method="post">
        <label>View all employees</label>
        <button name="admin_switch" value="View">View</button>
        <!-- Display all layaways in a table -->
        <table border="1">
            <thead>
                <tr>
                    <th>Employee ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Store ID</th>
                    <th>Role</th>
                    <th>Email</th>
                    
                </tr>
            </thead>
            <tbody>
                <% 
                List<Employee> employeeList = (List<Employee>) request.getAttribute("employeeList");
                if (employeeList != null && !employeeList.isEmpty()) {
                    for (Employee employees : employeeList) {
                %>
                <tr>
                    
                    <td><%= employees.getEmployee_ID() %></td>
                    <td><%= employees.getFirstName() %></td>
                    <td><%= employees.getLastName() %></td>
                    <td><%= employees.getStore_ID() %></td>
                    <td><%= employees.getRole() %></td>
                    <td><%= employees.getEmail() %></td>
                    
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
        
        <label>Add Managers</label>
        <button name="admin_switch" value="Add Manager">Add</button>
        
        <label>View Reports</label>
        <button name="admin_switch" value="View Report">View</button>
        
        <label>Add a Store</label>
        <button name="admin_switch" value="Add Store">Add</button>
        
        </form>
    </body>
</html>
