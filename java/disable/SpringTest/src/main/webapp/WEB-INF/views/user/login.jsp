<%--
  Created by IntelliJ IDEA.
  User: hp
  Date: 14-6-3
  Time: 下午7:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title></title>
</head>
<body>
<form action="${ctx}/login" method="POST">
    username：<input type="text" name="username"/><br/>
    password：<input type="password" name="password"/><br/>
    <input type="submit" value="Login"/><br/>
</form>
<br/>
<a href="${ctx}/register"><h2>register</h2></a>
<br/>
<br/>
<a href="${ctx}/list"><h2>Show_all_users</h2></a>
<br/>
</body>
</html>
