<%-- 
    Document   : MyEmployees
    Created on : 30 Jul 2024, 22:48:58
    Author     : T440
--%>

<%@page import="java.util.List"%>
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
    <style>
        #customers {
  border-collapse: collapse;
  width: 100%;
}

#customers td, #customers th {
  border: 1px solid #ddd;
  padding: 8px;
}

#customers tr:nth-child(even){background-color: #3498db;}

#customers tr:hover {background-color: #ddd;}

#customers th {
  padding-top: 12px;
  padding-bottom: 12px;
  text-align: left;
  background-color: #04AA6D;
  color: white;
}
    </style>
    <body>
        <% List<Employee> employees = (List<Employee>) request.getSession(false).getAttribute("MyEmployees");%>
        <jsp:include page="sidebar.jsp"/>
        <div class="menu-content">
            
            
            <div class="table-wrapper">
                <h4>My Employees</h4>
                <form action="EmployeeServlet" method="Get">
                    
                    <button name="submit" type="submit" value="getAddEmployee" class="submit-btn">Add Employee</button>
                </form>
                <div>
                    <table id="customers">
    <tr>
      
      <th>First Name</th>
      <th>Last Name</th>
      <th>Email</th>
      <th>Employee Id</th>
    <tr>

    <% for (Employee employee : employees){%>
    <tr>
        <td><%= employee.getFirstName() %></td>
        <td><%= employee.getLastName() %></td>
        <td><%= employee.getEmail() %></td>
        <td><%= employee.getEmployees_id() %></td>
    </tr>
    <%}%>

</table>
                </div>
            </div>
        </div>
    </body>
</html>
