<%-- 
    Document   : MyEmployees
    Created on : 30 Jul 2024, 22:48:58
    Author     : T440
--%>

<%@page import="java.util.Set"%>
<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Employee</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/report.css">
    </head>
    <body>
        <% Set<Employee> employees = (Set<Employee>) request.getSession(false).getAttribute("MyEmployees");%>
        <jsp:include page="sidebar.jsp"/>
        <div class="menu-content">
            
            
            <div class="table-wrapper">
                <h4>My Employees</h4>
                <div>
                    <table class="fl-table">
  <caption>My Employees</caption>
  <thead>
    <tr>
      <th><!-- Intentionally Blank --></th>
      <th>First Name</th>
      <th>Last Name</th>
      <th>Email</th>
      <th>Employee Id</th>
    <tr>
  </thead>
  <tbody>
    <% for (Employee employee : employees){%>
    <tr>
        <td><%= employee.getFirstName() %></td>
        <td><%= employee.getLastName() %></td>
        <td><%= employee.getEmail() %></td>
        <td><%= employee.getEmployees_id() %></td>
    </tr>
    <%}%>
  </tbody>
</table>
                </div>
            </div>
        </div>
    </body>
</html>
