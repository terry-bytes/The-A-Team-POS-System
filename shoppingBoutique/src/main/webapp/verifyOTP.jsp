<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Verify OTP</title>
    </head>
    <body>
        <h3>Verify OTP</h3>
        <form action="${pageContext.request.contextPath}/verifyOTP" method="post">
            <label for="otp">Enter OTP:</label>
            <input type="text" id="otp" name="otp" required>
            <button type="submit">Verify</button>
        </form>

    </body>
</html>
