<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Qunar图书馆系统平台</title>
<link rel="stylesheet" href="http://freshzz.corp.qunar.com/QunarLibrary/src/styles/application.css" />
<link rel="stylesheet" href="http://freshzz.corp.qunar.com/QunarLibrary/src/styles/bootstrap.css" />
</head>
<body bgcolor="#FFFFFF">
	<script
		src="https://qsso.corp.qunar.com/lib/qsso-auth.js?t=<?=rand()?>">
	</script>
	<div align="center" style="margin-top:8%">
	<table style="text-align: center;">
		<tr>
			<td colspan="2" height="100px">
					<img src="http://sources.corp.qunar.com/QunarLibrary/logo.png" border="0" alt="" />
			</td>
		</tr>
		<tr>
			<td height="40px"
				style="font-size: 23px; text-align: center; font-weight: bolder; letter-spacing: 2px;" >Qunar图书馆系统平台</td>
		</tr>
		<tr>
			<td style="font-size: 15px; text-align: center;">
				<button id="qsso-login" class="qsso_button">QSSO登录</button>
			</td>
		</tr>
	</table>
	</div>
	<script>
		QSSO.attach('qsso-login', '<%=request.getContextPath()%>/login.do');
	</script>
</body>
</html>
