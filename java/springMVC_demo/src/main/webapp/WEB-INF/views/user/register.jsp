<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title></title>
</head>
<body>
"Hello, World"
<form action="${ctx}/homepage" method="POST">
    username：<input type="text" name="username"/><br/>
    password：<input type="password" name="password"/><br/>
    <input type="submit" value="login"/><br/>
</form>
</body>
</html>
