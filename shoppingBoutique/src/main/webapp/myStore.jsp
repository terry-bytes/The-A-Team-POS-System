<%@page import="ateam.Models.Store"%>
<%@page import="ateam.Models.Employee"%>
<%@page import="java.util.List"%>
<%@page import="ateam.Models.Role"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Store</title>

          <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/editEmp.css">
        <link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
    </head>
    <body>
        <%  
            Role[] roles = Role.values();
            String message = (String) request.getAttribute("message");
            Employee employee = (Employee) request.getSession(false).getAttribute("Employee");
            List<Store> stores = (List<Store>) request.getSession(false).getAttribute("stores");
            String storeName;
        %>
        <% if(employee != null && employee.getRole() != Role.Teller) { %>
        
        <div class="manager-container">
            <jsp:include page="sidebar.jsp"></jsp:include>
        <div class="left-main">
            <div class="login-box">
                <div class="login-header">
                    <h3>Add Store</h3>
                <% if(message != null){%><p><%=message%></p><%}%>
                </div>
                <form action="StoreServlet" method="post">
                    <div class="input-box">
                            <label for="firstName">Store Name</label>
                            <input type="text"
                                   id="firstName"
                                   placeholder='Store name'
                                   name='storeName'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class='bx bx-store icon' ></i>
                        </div>
                    <div class="input-box">
                            <label for="firstName">Store Address</label>
                            <input type="text"
                                   id="firstName"
                                   placeholder='Store Address'
                                   name='storeAddress'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class='bx bx-location-plus'></i>
                        </div>
                    <div class="input-box">
                            <label for="firstName">Store City</label>
                            <input type="text"
                                   id="firstName"
                                   placeholder='Store Address'
                                   name='storeCity'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class='bx bx-location-plus'></i>
                        </div>
                    <div class="two-forms">
                        <div class="input-box">
                            <label for="firstName">Province</label>
                            <input type="text"
                                   id="firstName"
                                   placeholder='Province'
                                   name='storeProvince'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             <i class='bx bx-location-plus'></i>
                        </div>
                        <div class="input-box">
                            <label for="lastName">Zipcode</label>
                            <input type="text"
                                   id="lastName"
                                   placeholder='Zipcode'
                                   name='storeZipcode'
                                   class='input-field'
                                   autocomplete="off" required
                                   />
                             
                        </div>
                    </div>

                    <div class="input-box">
                        <label for="email">Email</label>
                        <input type="email"
                               id="email"
                               placeholder='Store Email Address'
                               name='storeEmailAddress'
                               class='input-field'
                               autocomplete="off" required
                               />
                        <i class="bx bx-envelope"></i>
                    </div>

                    <div class="input-box">
                        <label for="password">Store Contact Number</label>
                        <input type="number"
                               id="password"
                               placeholder='Contact number'
                               name='storePhone'
                               class="input-field"
                               autocomplete="off" required
                               />
                        <i class='bx bx-phone' ></i>
                    </div>
                    
                    
                    <div class="input-submit">
                        <input name="submit" value="Submit_Store" hidden>
                        <button class="submit-btn" id="submit">Add Store</button>
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
