<%-- 
    Document   : editEmployee
    Created on : 02 Aug 2024, 15:43:54
    Author     : T440
--%>

<%@page import="ateam.Models.Store"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="ateam.Models.Role"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <% Employee manager = (Employee) request.getSession(false).getAttribute("Employee");
           List<Store> stores = (List<Store>) request.getSession(false).getAttribute("Stores");
           Employee emp = (Employee) request.getAttribute("employee");
           String message = (String) request.getSession(false).getAttribute("message");
            if (manager != null && manager.getRole() == Role.Manager){%>
            <div class="manager-container">
            <div >
                <jsp:include page="sidebar.jsp"/>
            </div>
            <div class="main">
                <div class="login-box">
                    <div class="login-header">
                        <h3>Add Employee</h3>
                    </div>
                    <form action="EmployeeServlet" method="post">
                        <div class="two-forms">
                            <div class="input-box">
                                <input type="text"
                                       placeholder='First Name'
                                       name='firstName'
                                       class='input-field'
                                       value="<%= emp.getFirstName() %>"
                                       autocomplete="off" required
                                       />
                                 <i class="bx bx-user"></i>
                            </div>
                            <div class="input-box">
                                <input type="text"
                                       placeholder='Last Name'
                                       name='lastName'
                                       class='input-field'
                                       value="<%= emp.getLastName() %>"
                                       autocomplete="off" required
                                       />
                                 <i class="bx bx-user"></i>
                            </div>
                        </div>

                        <div class="input-box">
                            <input type="email"
                                   placeholder='Email'
                                   name='email'
                                   class='input-field'
                                   value="<%= emp.getEmail() %>"
                                   autocomplete="off" required
                                   />
                            <i class="bx bx-envelope"></i>
                        </div>

                        <div class="input-box">
                            <input type="password"
                                   placeholder='Password'
                                   name='password'
                                   class="input-field"
                                   autocomplete="off" required
                                   />
                            <i class="bx bx-lock-alt"></i>
                        </div>
                        <div class="select-container">
                            <label>Role</label>
                            <select class="select-box" name="role" id="roleSelector">
                                <option value="Teller">Teller</option>
                                <option value="Manager">Manager</option>
                            </select>
                        </div>
                        <div class="select-container" id="storeSelector" style="display: none;">
                            <label>Store</label>

                            <select class="select-box" name="storeId">
                                <% if(stores != null) {
                                    for(Store store : stores) { %>
                                    <option value="<%=store.getStore_ID() %>"><%=store.getStore_name() %></option>
                                    <% } } %>
                            </select>
                        </div>
                        
                        <% if(message != null) { %>

                        <p><%=message%></p>
                        <% } %>
                        <div class="input-submit">
                            <input name="submit" value="update" hidden>

                            <button class="submit-btn" id="submit">Add Employee</button>

                        </div>
                    </form>
                </div>
            </div>
        </div>
        <% } else { %>
        <jsp:include page="unauthorized.jsp"/>
        <% } %>
        
    </body>
</html>
