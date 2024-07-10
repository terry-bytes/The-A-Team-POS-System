<%-- 
    Document   : login
    Created on : Jul 10, 2024, 9:33:52 AM
    Author     : Train 01
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/log.css">
    </head>
    <body>
        <div class="container">
            <div class="login-box">
                <div class="login-header">
                    <h4>Carols Boutique</h4>
                    <h3>Login</h3>
                </div>
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
                <div class="input-submit">
                    <button class="submit-btn" id="submit">LogIn</button>
                </div>
            </div>
        </div>
    </body>
</html>
