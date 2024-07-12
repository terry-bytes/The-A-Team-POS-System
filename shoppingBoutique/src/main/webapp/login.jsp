<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/log.css">
    </head>
    <body>
        <% String message = (String) request.getAttribute("message");%>
        <div class="container">
            <div class="login-box">
                <div class="login-header">
                    <h4>Carols Boutique</h4>
                    <h3>Login</h3>
                </div>
                <form action="employees" method="post">
                    <div class="input-box">
                        <input type="text"
                               placeholder='Employee Id'
                               name='employeeId'
                               class='input-field'
                               autocomplete="off" required
                               />
                    </div>
                    <div class="input-box">
                        <input type="password"
                               placeholder='Password'
                               name='password'
                               class="input-field"
                               autocomplete="off" required
                               />
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
        </div>
    </body>
</html>
