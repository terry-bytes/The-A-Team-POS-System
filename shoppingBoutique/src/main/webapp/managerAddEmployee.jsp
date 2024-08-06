<%-- 
    Document   : managerAddEmployee
    Created on : 05 Aug 2024, 15:30:27
    Author     : T440
--%>

<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Role"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Employee</title>

          <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editEmp.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>
        <%  
            Role[] roles = Role.values();
            String message = (String) request.getAttribute("addEmployeeMessage");
            Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            List<Store> stores = (List<Store>) request.getSession(false).getAttribute("stores");
            String storeName;
        %>
        <% if(employee != null && employee.getRole() != Role.Teller) { %>
        
        <div class="manager-container">
            <jsp:include page="managerSidebar.jsp"></jsp:include>
        <div class="left-main">
            <div class="login-box">
                <div class="login-header">
                    <h3>Add Employee</h3>
                </div>
                <form action="EmployeeServlet" method="post">
                    <div class="two-forms">
                        <div class="input-box">
                            <label for="firstName">First Name</label>
                            <input type="text"
                                   id="firstName"
                                   placeholder='First Name'
                                   name='firstName'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class="bx bx-user"></i>
                        </div>
                        <div class="input-box">
                            <label for="lastName">Last Name</label>
                            <input type="text"
                                   id="lastName"
                                   placeholder='Last Name'
                                   name='lastName'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class="bx bx-user"></i>
                        </div>
                    </div>

                    <div class="input-box">
                        <label for="email">Email</label>
                        <input type="email"
                               id="email"
                               placeholder='Email'
                               name='email'
                               class='input-field'
                               autocomplete="off" required
                               />
                        <i class="bx bx-envelope"></i>
                    </div>

                    <div class="input-box">
                        <label for="password">Password</label>
                        <input type="password"
                               id="password"
                               placeholder='Password'
                               name='password'
                               class="input-field"
                               autocomplete="off" required
                               />
                        <i class="bx bx-lock-alt"></i>
                    </div>
                    
                    <div class="select-container">
                        <label for="roleSelector">Role</label>
                        <select class="select-box" name="role" id="roleSelector">
                            <option value="Manager">Manager</option>
                        </select>
                    </div>
                    <div class="select-container" id="storeSelector">
                        <label for="managerStoreId">Store</label>

                        <select class="select-box" name="managerStoreId" id="managerStoreId">
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
                        <input name="submit" value="add" hidden>
                        <button class="submit-btn" id="submit">Add Employee</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
        <% } else { %>
        <jsp:include page="unauthorized.jsp"/>
        <% } %>
        <script>
            var selectedRole = document.getElementById("roleSelector");
            var storeSelection = document.getElementById("storeSelector");
            
            selectedRole.addEventListener('change', function() {
                var role = selectedRole.value;
                

                if(role === 'Manager') {

                    storeSelection.style.display = 'block';
                } else {
                    storeSelection.style.display = 'none';
                }
            });
        </script>
    </body>
</html>
