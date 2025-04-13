<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register</title>
    <link rel="stylesheet" type="text/css" href="/css/register.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
	<div class="signUp-container">
    <h1 id="signUp-title">회원가입</h1>
    <form id="registerForm" action="<c:url value='/login/register'/>" method="post">
        <div>
            <label>아이디</label>
            <input type="text" id="userId" name="userId" placeholder="cafelover1" required>
            <button type="button" id="checkDuplicateId">중복 체크</button>
            <span id="idError" class="error-message"></span>
            <span id="idSuccess" class="success-message"></span>
        </div>
        <div>
            <label>닉네임</label>
            <input type="text" id="userName" name="userName" placeholder="카페러버"> <!-- required속성을 제거하여 빈칸 허용 -->
        </div>
        <div>
            <label>비밀번호</label>
            <input type="password" id="password" name="password" required>
            <span id="passwordError" class="error-message"></span>
        </div>
        <div>
            <label>비밀번호 재입력</label>
            <input type="password" id="confirmPassword" required>
            <span id="confirmPasswordError" class="error-message"></span>
        </div>
        <div>
            <label>이메일</label>
            <input type="email" id="email" name="email" placeholder="cafelover1@naver.com" required>
            <button type="button" id="checkDuplicateEmail">중복 체크</button>
            <span id="emailError" class="error-message"></span>
            <span id="emailSuccess" class="success-message"></span>
        </div>
        <div>
	    <!-- <label>사용자 타입(테스트용도, 제거해야함)</label>  -->
	    	<input type="hidden" name="userType" value="USER"/>
	    	<!--  
		    <select name="userType" required>
		        <option value="USER">USER</option>
		        <option value="BUSINESS">BUSINESS</option>
		        <option value="ADMIN">ADMIN</option>
		    </select>
		    -->
		</div>
        <div class="profileImage">
            <label>프로필 이미지</label>
            <img id="selectedProfileImg" src="/profile_img/img1.png" class="profile-img" alt="Profile Image">
            <input type="hidden" id="profileImg" name="profileImg" value="/profile_img/img1.png"> <!-- 숨겨진 필드 추가 -->
            <button type="button" id="changeProfile">프로필 변경하기</button>
        </div>
        <div>
            <input type="submit" value="회원가입">
            <!-- CSRF 보호를 위한 hidden input -->
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </div>
    </form>
    <a id="login-link" href="/login/login">로그인으로 돌아가기</a>

    <!-- Modal for Profile Image Selection -->
    <div id="profileModal" class="modal">
        <div class="modal-content">
        	<span class="close">&times;</span> <!-- 닫기 버튼 -->
            <h2>프로필 이미지를 선택하세요</h2>
            <div class="profile-options">
                <img src="/profile_img/img1.png" alt="Profile Image 1">
                <img src="/profile_img/img2.png" alt="Profile Image 2">
                <img src="/profile_img/img3.png" alt="Profile Image 3">
                <img src="/profile_img/img4.png" alt="Profile Image 4">
                <img src="/profile_img/img5.png" alt="Profile Image 5">
                <img src="/profile_img/img6.png" alt="Profile Image 6">
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="/js/register.js"></script>
</body>
</html>