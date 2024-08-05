<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login</title>

        <style>
            @import url('https://fonts.googleapis.com/css2?family=Poppins:wght@300;400;500;600;700&display=swap');
            @import url('https://fonts.googleapis.com/css2?family=Pacifico&display=swap');
        *{
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Poppins', sans-serif;
        }
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            background-image: linear-gradient(to right, #277af6, #0098f7, #48b2ef, #85c7e5, #bfd9e0);
        }

        .login-wrapper {
            background: rgba(254, 254, 254, 1);
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 350px; 
            text-align: center;
        }

        .login-wrapper h4, .login-wrapper h3 {
            margin: 10px 0;
        }
        
        .login-wrapper p{
             font-family: "Pacifico", cursive;
  font-weight: 400;
  font-style: normal;
        }
        .input-field {
            margin: 20px 0;
            position: relative;
        }

        .input-field input {
            width: 100%;
            padding: 15px;
            box-sizing: border-box;
            border: 2px solid #ccc;
            border-radius: 5px;
            transition: .3s ease;
        }
        .input-field input:focus{
            border-color: #3498db; 
            outline: none;
        }

        .input-field label {
            position: absolute;
            top: -10px;
            left: 10px;
            background-color: white;
            padding: 0 5px;
            font-size: 12px;
        }

        .input-submit {
            margin: 15px 0;
        }

        .input-submit button {
            width: 100%;
            padding: 10px;
            background-color: #3498db;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .input-submit button:hover {
            background-color: #2980b9;
        }

        .forgot-password {
            margin-top: 10px;
        }

        .forgot-password a {
            color: #007bff;
            text-decoration: none;
        }

        .forgot-password a:hover {
            text-decoration: underline;
        }
    </style>
        
    </head>
    <body>
        <% String message = (String) request.getAttribute("message");%>
  
            <div class="login-wrapper">
                <h2>Carols Boutique</h2>
                <p>Where Comfort meets style</p>
                <h3>Login</h3>

                <form action="EmployeeServlet" method="post">
                    <div class='input-field'>
                        <input type="text" name='employeeId' autocomplete="off" required />
                        <label>Enter your employee Id</label>
                    </div>
                    <div class='input-field'>
                        <input type="password" name='password' autocomplete="off" required />
                        <label>Enter your password</label>
                    </div>
                    <% if (message != null) { %>
                    <p style="color: red;"><%= message %></p>
                    <% } %>
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