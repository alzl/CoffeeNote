<!-- 유저 정보 변경에 예외처리 추가했으나 사업자 추가는 논리상 맞지않음, 카페랑 같이 추가해야함 -->
<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Home Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<h1>Welcome, ${user.userName} <small>(${user.userId})</small></h1>

<!-- 프로필 이미지 표시 -->
<img src="${user.profileImg}" alt="Profile Image" style="width: 100px; height: 100px;"/>

<p>Your profile details:</p>

<ul>
    <li>ID: ${user.userId}</li>

    <!-- Name 수정 폼 -->
    <li>
    <form id="nameUpdateForm" action="<c:url value='/user/updateUserName'/>" method="post">
        Name : 
        <input type="text" id="userName" name="userName" value="${user.userName}" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="button" id="updateNameBtn">수정</button>
    </form>
    </li>
    <li>
    <form id="emailUpdateForm" action="<c:url value='/user/updateUserEmail'/>" method="post">
       Email: 
        <input type="text" id="userEmail" name="userEmail" value="${user.email}" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="button" id="updateEmailBtn">수정</button>
    </form>
    </li>
    <li>User Type: ${user.userType}</li>
        <!-- 0828 사업주 등록폼 부분 -->
	<!-- 추가된 부분: 사업자 등록번호 입력 및 수정 폼 -->
    <c:if test="${user.userType == 'BUSINESS'}">  <!-- 사업자만 이 부분을 볼 수 있음 -->
        <li>
        <form id="businessRegForm" action="<c:url value='/user/updateBusinessReg'/>" method="post">
            사업자 등록번호 수정:
            <input type="text" id="businessRegNum" name="businessRegNum" value="${user.businessRegNum}" required>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="button" id="updateBusinessRegBtn">수정</button>
        </form>
        </li>
    </c:if>
    <!-- 추가된 부분: USER만 볼 수 있는 사업자 등록번호 입력 폼 -->
    <c:if test="${user.userType == 'USER'}">
        <li>
        <form id="registerBusinessForm" action="<c:url value='/user/registerBusiness'/>" method="post">
            사업자 등록번호 등록:
            <input type="text" id="registerBusinessRegNum" name="businessRegNum" required>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <button type="button" id="registerBusinessBtn">등록</button>
        </form>
        </li>
    </c:if>
</ul>

<!-- Name 비밀번호 확인 모달 창 -->
<div id="namePasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="namePassword" name="password" required>
    <button type="button" id="nameConfirmPasswordBtn">확인</button>
    <span id="namePasswordError" style="color:red;"></span>
</div>

<!-- Email 비밀번호 확인 모달 창 -->
<div id="emailPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="emailPassword" name="password" required>
    <button type="button" id="emailConfirmPasswordBtn">확인</button>
    <span id="emailPasswordError" style="color:red;"></span>
</div>

<!-- 사업자 등록번호 비밀번호 확인 모달 창 (BUSINESS 사용자 수정용) -->
<div id="businessRegPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="businessRegPassword" name="password" required>
    <button type="button" id="businessRegConfirmPasswordBtn">확인</button>
    <span id="businessRegPasswordError" style="color:red;"></span>
</div>

<!-- 사업자 등록번호 비밀번호 확인 모달 창 (USER 등록용) -->
<div id="registerBusinessPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="registerBusinessPassword" name="password" required>
    <button type="button" id="registerBusinessConfirmPasswordBtn">확인</button>
    <span id="registerBusinessPasswordError" style="color:red;"></span>
</div>
<!-- 이름, 이메일, 등록번호 제출 성공 후에... -->
<!-- 성공 메시지를 표시하는 영역 -->
<c:if test="${not empty successMessage}">
    <div id="successMessage" style="color:green;">${successMessage}</div>
</c:if>
<!-- 오류 메시지를 표시하는 영역 -->
<c:if test="${not empty errorMessage}">
    <div id="errorMessage" style="color:red;">${errorMessage}</div>
</c:if>

<a href="<c:url value='/'/>">main page</a>

<script>
    // 이름칸의 '수정' 버튼 클릭 시 비밀번호 입력 창 표시
    $('#updateNameBtn').click(function() {
        $('#namePasswordModal').show();
    });

    // 비밀번호 확인 버튼 클릭 시
    $('#nameConfirmPasswordBtn').click(function() {
        const password = $('#namePassword').val();

        if (password === '') {
            $('#namePasswordError').text('비밀번호를 입력하세요.');
            return;
        }

        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',  // 비밀번호 검증을 위한 URL
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    // 비밀번호가 일치하면 폼 제출
                    document.getElementById('nameUpdateForm').submit();
                } else {
                    $('#namePasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.log("Error response: ", xhr.responseText);
                $('#namePasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
    
	 // 이메일칸의 '수정' 버튼 클릭 시 비밀번호 입력 창 표시
    $('#updateEmailBtn').click(function() {
        $('#emailPasswordModal').show();
    });
	
    // 비밀번호 확인 버튼 클릭 시
    $('#emailConfirmPasswordBtn').click(function() {
        const password = $('#emailPassword').val();
        const email = $('#userEmail').val(); // 이메일 입력값 가져오기
        if (password === '') {
            $('#emailPasswordError').text('비밀번호를 입력하세요.');
            return;
        }
        //이메일 유효성 검사
        if (!validateEmail(email)) {
	        $('#emailPasswordError').text('유효하지 않은 이메일 형식입니다.');
	        return;
	    }
		
        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',  // 비밀번호 검증을 위한 URL
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    // 비밀번호가 일치하면 폼 제출
                    document.getElementById('emailUpdateForm').submit();
                } else {
                    $('#emailPasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.log("Error response: ", xhr.responseText);
                $('#emailPasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
    
 	// 0828 사업자 등록번호 수정 클릭 시
    // 이름 수정, 이메일 수정과 유사하게 사업자 등록번호 수정 시 비밀번호 입력 창 표시
    $('#updateBusinessRegBtn').click(function() {
        $('#businessRegPasswordModal').show();
    });
 	// 사업자 등록번호 비밀번호 확인 버튼 클릭 시
    $('#businessRegConfirmPasswordBtn').click(function() {
        const password = $('#businessRegPassword').val();
        const businessRegNum = $('#businessRegNum').val(); // 사업자 등록번호 입력값 가져오기

        if (password === '') {
            $('#businessRegPasswordError').text('비밀번호를 입력하세요.');
            return;
        }

        if (!validateBusinessReg(businessRegNum)) {
            $('#businessRegPasswordError').text('유효한 사업자 등록번호를 입력하세요.');
            return;
        }

        // 비밀번호 확인 Ajax 요청
        $.ajax({
            url: '/user/verifyPassword',  // 비밀번호 검증을 위한 URL (기존 사용)
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    // 비밀번호가 일치하면 폼 제출
                    document.getElementById('businessRegForm').submit();
                } else {
                    $('#businessRegPasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                console.log("Error response: ", xhr.responseText);
                $('#businessRegPasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
 	
    // 사업자 등록번호 등록 (USER 사용자) 비밀번호 확인 모달
    $('#registerBusinessBtn').click(function() {
        $('#registerBusinessPasswordModal').show();
    });
    // 비밀번호 확인 버튼 클릭 시
    $('#registerBusinessConfirmPasswordBtn').click(function() {
        const password = $('#registerBusinessPassword').val();
        const businessRegNum = $('#registerBusinessRegNum').val();

        if (password === '') {
            $('#registerBusinessPasswordError').text('비밀번호를 입력하세요.');
            return;
        }

        if (!validateBusinessReg(businessRegNum)) {
            $('#registerBusinessPasswordError').text('유효한 사업자 등록번호를 입력하세요.');
            return;
        }

        $.ajax({
            url: '/user/verifyPassword',
            method: 'GET',
            data: { password: password },
            success: function(response) {
                if (response.match) {
                    document.getElementById('registerBusinessForm').submit();
                } else {
                    $('#registerBusinessPasswordError').text('비밀번호가 일치하지 않습니다.');
                }
            },
            error: function(xhr, status, error) {
                $('#registerBusinessPasswordError').text('서버 오류가 발생했습니다. 다시 시도해주세요.');
            }
        });
    });
 
    // 0828 사업자 등록번호 유효성 검사
    function validateBusinessReg(businessRegNum) {
        // 사업자 등록번호는 10자리 숫자로 되어있다고 가정
        const regEx = /^[0-9]{10}$/;
        return regEx.test(businessRegNum);
    }
    
    // 이메일 유효성 검사 함수
    function validateEmail(email) {
        const re = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        return re.test(email);
    }
    
</script>
</body>
</html>
