<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
logout page
<form action="/login/logout" method="post">
<button>[로그아웃]</button>
<input type="hidden" 
name="${_csrf.parameterName}"
value="${_csrf.token }" />
</form>
</body>
</html>
