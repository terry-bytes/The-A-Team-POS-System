<%-- 
    Document   : myStore
    Created on : 01 Aug 2024, 17:38:40
    Author     : T440
--%>

<%@page import="ateam.Models.Employee"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% Employee employee = (Employee) request.getSession(false).getAttribute("Employee"); %>
        
        <section>
            <jsp:include page="sidebar.jsp"></jsp:include>
            <div class="main">
                <div class="summary">
                    <div class="products">
                        <h4>Products</h4>

                    </div>
                    <div class="">
                        <h4>Employees</h4>
                    </div>
                    <div>
                        <a href="StoreServlet?submit=getAddStore">Add Store</a>
                        <a href="EmployeeServlet?submit=getAddEmployee">Add Employee</a>
                    </div>
                </div>
                
                <div>
                    
                </div>
            </div>
        </section>
    </body>
</html>
