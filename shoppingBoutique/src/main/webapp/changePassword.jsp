<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Change Password</title>
</head>
<body>
    <h2>Change Password</h2>
    <form action="${pageContext.request.contextPath}/employees" method="post">
        <input type="hidden" name="submit" value="changePassword">
        <input type="hidden" name="email" value="${param.email}">
        
        <label for="newPassword">New Password:</label>
        <input type="password" id="newPassword" name="newPassword" required>
        
        <button type="submit">Change Password</button>
    </form>
    <c:if test="${not empty message}">
        <p style="color: green;">${message}</p>
    </c:if>
</body>
</html>
