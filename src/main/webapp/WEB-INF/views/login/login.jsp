<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인 페이지</title>
    <script>
        function validateLoginForm() {
            return true;  // 폼 제출 허용
        }
    </script>
	<!--<link rel="stylesheet" type="text/css" href="/css/main.css"/>
	<script src="/js/script.js"></script>-->
	<link rel="stylesheet" type="text/css" href="/css/login.css"/>
</head>
<body>
<div class="login-container">
    <h1 id="login-title">로그인</h1>
    <form action="<c:url value='/login/loginAttempt'/>" method="post"  id="login-form" onsubmit="return validateLoginForm();">
        <!-- ID, Password 필드 -->
        <div class="login-input-group">
			<input type="text" id="login-email" name="user_id" placeholder="아이디(이메일)">
			<span class="login-error-message" id="login-email-error" style="display: none;">아이디(이메일)를 입력해주세요.</span>
        </div>
        <div class="login-input-group">
			<input type="password" id="login-password" name="password" placeholder="비밀번호">
			<span class="login-error-message" id="login-password-error" style="display: none;">비밀번호를 입력해주세요.</span>
        </div>
        <!-- CSRF Token 설정 -->
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        
	
	<!-- 로그인 상태를 유지하는 체크박스 입니다. -->
	<!-- 그냥 안쓸래용
        <div class="login-checkbox-group">
            <input type="checkbox" id="login-stayLoggedIn">
            <label for="login-stayLoggedIn">로그인 상태 유지</label>
        </div> 
    -->
        
        <!-- 지우면 안됨 로그인 에러메시지임 -->
        <c:if test="${not empty error}">	
        <p style="color: red;">${error}</p>
    	</c:if>
		
        <input type="submit" value="로그인" id="login-button">
    </form>

	<div class="login-footer">
    <a href="/login/findId">아이디 찾기</a>
    <span class="login-separator">|</span>
    <a href="/login/findPassword">패스워드 찾기</a>
    <span class="login-separator">|</span>
    <a href="/login/register">회원가입</a>
    </div>
</body>
</html>
