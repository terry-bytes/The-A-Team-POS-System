<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/signin.css">
    </head>
    <body>
        <% String message = (String) request.getAttribute("message");%>
  
            <div class="login-wrapper">
                
                    <h3>Login</h3>
                

                <form action="EmployeeServlet" method="post">
                    <div class='input-field'>
                        <input type="text"
                               
                               name='employeeId'
                               
                               autocomplete="off" required
                               />
                        <label>Enter your employee Id</label>
                    </div>
                    <div class='input-field'>
                        <input type="password"
                    
                               name='password'
                              
                               autocomplete="off" required
                               />
                        <label>Enter your password</label>
                    </div>
                    <%if(message != null){%>
                    <p style="color: red;"><%=message%></p>
                    <%}%>
                    <div class="input-submit">
                        <input name="submit" value="login" hidden>
                        <button class="submit-btn" id="submit">LogIn</button>
                    </div>
                </form>
                <div class="forgot-password">
                    <a href="${pageContext.request.contextPath}/forgotPassword.jsp">Forgot Password?</a>
                </div>
            </div>
 
    </body>
</html>
