<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee List</title>
</head>
<body>
    <h1>Employee List</h1>
    
    <table border="1">
        <thead>
            <tr>
                <th>ID</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Store ID</th>
                <th>Employee ID</th>
                <th>Role</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="employee" items="${employees}">
                <tr>
                    <td>${employee.employee_ID}</td>
                    <td>${employee.firstName}</td>
                    <td>${employee.lastName}</td>
                    <td>${employee.store_ID}</td>
                    <td>${employee.employees_id}</td>
                    <td>${employee.role}</td>
                    <td>
                        <a href="employees?action=edit&employeeId=${employee.employee_ID}">Edit</a>
                        <a href="employees?action=delete&employeeId=${employee.employee_ID}">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
    
    <h2>Add Employee</h2>
    <form action="employees" method="post">
        First Name: <input type="text" name="firstName"><br>
        Last Name: <input type="text" name="lastName"><br>
        Store ID: <input type="number" name="storeId"><br>
        Employee ID: <input type="text" name="employeesId"><br>
        Password: <input type="password" name="password"><br>
        Role: <select name="role">
            <option value="Teller">Teller</option>
            <option value="Manager">Manager</option>
            <option value="Admin">Admin</option>
        </select><br>
        <input type="hidden" name="action" value="add">
        <input type="submit" value="Add Employee">
    </form>
</body>
</html>
