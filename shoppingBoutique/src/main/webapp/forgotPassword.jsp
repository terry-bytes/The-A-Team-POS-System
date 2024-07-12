<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Forgot Password</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/log.css">
</head>
<body>
    <div class="container">
        <div class="forgot-password-box">
            <h3>Forgot Password</h3>
            <form action="employees" method="post">
                <div class="input-box">
                    <input type="email" placeholder='Enter your email' name='email' class='input-field' required />
                </div>
                <div class="input-submit">
                    <input name="submit" value="forgotPassword" hidden>
                    <button class="submit-btn">Send OTP</button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
