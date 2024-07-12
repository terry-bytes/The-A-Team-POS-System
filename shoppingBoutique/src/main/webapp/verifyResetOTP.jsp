<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Verify Reset OTP</title>
</head>
<body>
    <h2>Verify Password Reset OTP</h2>
    <form action="${pageContext.request.contextPath}/employees" method="post">
        <input type="hidden" name="submit" value="verifyResetOTP">
        <label for="otp">Enter OTP:</label>
        <input type="text" id="otp" name="otp" required>
        <button type="submit">Verify</button>
    </form>
    <c:if test="${not empty otpMessage}">
        <p style="color: red;">${otpMessage}</p>
    </c:if>
</body>
</html>
