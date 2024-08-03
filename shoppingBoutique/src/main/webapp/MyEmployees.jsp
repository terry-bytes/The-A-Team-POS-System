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
            Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            if (employee != null && employee.getRole() == Role.Manager){
        %>
        <jsp:include page="sidebar.jsp"/>
        <div class="left-main">
            <div class="header">
                <h4>Employees for <%= store.getStore_name()%></h4>
                <a href="EmployeeServlet?submit=getAddEmployee">Add Employee</a>
            </div>
                
                    <table id="customers">
    <tr>
      
      <th>First Name</th>
      <th>Last Name</th>
      <th>Email</th>
      <th>Employee Id</th>
      <th>Position</th>
      <th>Action</th>
    <tr>

    <% for (Employee emp : employees){%>
    <tr>
        <td><%= emp.getFirstName() %></td>
        <td><%= emp.getLastName() %></td>
        <td><%= emp.getEmail() %></td>
        <td><%= emp.getEmployees_id() %></td>
        <td><%= emp.getRole() %></td>
        <td>
            <a href="EmployeeServlet?submit=edit&employeeId=<%=emp.getEmployee_ID()%>">Update</a>
            <a href="#">Remove</a>
        </td>
    </tr>
    <%}%>

</table>
                </div>
        </div>
        <%}%>
        </section>
    </body>
</html>
