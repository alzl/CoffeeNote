<!-- 사업자 추가 개선, 이제 추가된 카페의 정보를 제대로 불러오면 됨 -->
<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Home Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
	    
    <style>
 	#cafeListForm, #registeredCafeList {
            margin-top: 20px;
            background-color: white;
            padding: 10px;
            border: 1px solid #ccc;
            width: 400px;
            max-height: 300px;
            overflow-y: auto;
        }

        .cafe-item {
            padding: 5px;
            cursor: pointer;
            border-bottom: 1px solid #eee;
        }

        .cafe-item.selected {
            background-color: #e0e0e0; /* 선택된 항목 회색 배경 */
        }

        .dropdown-content {
            display: none;
            margin-top: 20px;
            padding: 10px;
            border: 1px solid #ccc;
            width: 400px;
        }

        .dropdown-btn {
            background-color: #4CAF50;
            color: white;
            padding: 10px;
            border: none;
            cursor: pointer;
        }

        .dropdown-btn:hover {
            background-color: #3e8e41;
        }
    </style>
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
</ul>
<!-- 사업자 등록 폼 드롭다운 -->
<button class="dropdown-btn">사업장 등록</button>
<div class="dropdown-content">
	<!-- 카페 검색 폼 -->
	<div id="searchCafeForm">
	    <h3>사업장 등록하기</h3>
	    <input type="text" id="searchKeyword" placeholder="카페 검색..." />
	    <button type="button" id="searchCafeBtn">검색</button>
	</div>
	<!-- 카페 검색 결과 리스트 -->
	<div id="cafeListForm">
	    <h3>검색된 카페</h3>
	    <ul id="cafeList">
	        <!-- 검색 결과를 동적으로 추가 -->
	    </ul>
	</div>
	
	<!-- 사업자 등록번호 입력 및 사업장 추가 -->
	<div id="businessRegForm" style="display:none;">
	    <h3>선택된 카페: <span id="selectedCafeName"></span></h3>
	    <form id="registerBusinessForm" action="<c:url value='/user/registerBusiness'/>" method="post">
	        사업자 등록번호: <input type="text" id="businessRegNum" name="businessRegNum" required>
	        <input type="hidden" name="cafeId" id="selectedCafeId">
	        <input type="hidden" name="cafeX" id="selectedCafeX">
	        <input type="hidden" name="cafeY" id="selectedCafeY">
	        <input type="hidden" name="placeName" id="selectedCafePlaceName">
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	        <button type="button" id="registerBusinessBtn">사업장 추가</button>
	    </form>
	</div>
</div>
<!-- 등록된 카페 리스트 -->
<div id="registeredCafeList">
    <h3>등록된 사업장</h3>
    <ul>
        <c:forEach var="cafe" items="${registeredCafes}">
            <li>${cafe.cafeId}</li>
        </c:forEach>
    </ul>
</div>

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

<!-- 사업장 추가용 비밀번호 확인 모달 창 -->
<div id="registerBusinessPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="registerBusinessPassword" name="password" required>
    <button type="button" id="registerBusinessConfirmPasswordBtn">확인</button>
    <span id="registerBusinessPasswordError" style="color:red;"></span>
</div>

<!-- 사업자 등록번호 비밀번호 확인 모달 창 (BUSINESS 사용자 수정용) -->

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
	// 드롭다운 버튼 클릭 시 폼 열기/닫기
	$('.dropdown-btn').click(function() {
	    $('.dropdown-content').toggle();
	});
	
	// 검색된 카페 항목 클릭 시 선택 이벤트 추가
	$('.cafe-item').click(function() {
	    $(this).toggleClass('selected');
	    const selectedCafeName = $(this).text();
	    const selectedCafeId = $(this).data('id');
	    $('#selectedCafeName').text(selectedCafeName);
	    $('#selectedCafeId').val(selectedCafeId);
	    $('#businessRegForm').show();
	});

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
	// 사업장 추가 버튼 클릭 시 비밀번호 확인 모달 창 표시
    $('#registerBusinessBtn').click(function() {
        $('#registerBusinessPasswordModal').show();
    });

    // 사업장 추가 비밀번호 확인 버튼 클릭 시
    $('#registerBusinessConfirmPasswordBtn').click(function() {
        const password = $('#registerBusinessPassword').val();
        const businessRegNum = $('#businessRegNum').val();

        if (password === '') {
            $('#registerBusinessPasswordError').text('비밀번호를 입력하세요.');
            return;
        }

        if (!validateBusinessReg(businessRegNum)) {
            $('#registerBusinessPasswordError').text('유효한 사업자 등록번호를 입력하세요.');
            return;
        }

        // 비밀번호 확인 Ajax 요청
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
    
    //0829 카페 검색  추가
	$(document).ready(function() {
	    // CSRF 토큰을 전역 Ajax 설정에 추가
	    var token = $("meta[name='_csrf']").attr("content");
	    var header = $("meta[name='_csrf_header']").attr("content");
	
	    $.ajaxSetup({
	        beforeSend: function(xhr) {
	            xhr.setRequestHeader(header, token);
	        }
	    });
	
	    // 검색 버튼 클릭 시 카페 검색
	    $('#searchCafeBtn').click(function() {
	        const keyword = $('#searchKeyword').val();
	
	        if (keyword.trim() === '') {
	            alert('검색어를 입력하세요.');
	            return;
	        }
	
	        // Ajax 요청으로 카페 검색 수행
	        $.ajax({
	            url: '/user/searchmap',  // 검색 처리할 URL (CN_UserController에서 처리)
	            method: 'POST',
	            data: { keyword: keyword },
	            beforeSend: function(xhr) {
	                xhr.setRequestHeader(header, token); // CSRF 토큰 추가
	            },
	            success: function(response) {
	                console.log("Response from server:", response);  // response 출력하여 디버깅
	                if (Array.isArray(response)) {
	                    $('#cafeList').empty();  // 기존 검색 결과 초기화
	                    if (response.length > 0) {
	                        $('#cafeListForm').show();  // 검색 결과 섹션 표시
	                        response.forEach(function(cafe) {
	                        	// 상세 정보를 포함한 카페 리스트 항목 생성
	                        	$('#cafeList').append(
	                        	    '<li class="cafe-item" data-id="' + cafe.id + '" data-x="' + cafe.x + '" data-y="' + cafe.y + '" data-place-name="' + cafe.place_name + '">' +
	                        	    '<strong>' + cafe.place_name + '</strong><br>' +
	                        	    '주소: ' + cafe.address_name + '<br>' +
	                        	    '도로명 주소: ' + cafe.road_address_name + '<br>' +
	                        	    '전화번호: ' + (cafe.phone ? cafe.phone : '전화번호 정보 없음') + '<br>' +
	                        	    '<a href="' + cafe.place_url + '" target="_blank">상세 정보 보기</a>' +
	                        	    '</li>'
	                        	);
	                        });
	
	                        // 검색된 카페 항목 클릭 시 선택 이벤트 추가
	                        $('.cafe-item').click(function() {
	                            $(this).toggleClass('selected');
	                            const selectedCafeName = $(this).data('place-name');
	                            const selectedCafeId = $(this).data('id');
	                            const selectedCafeX = $(this).data('x');
	                            const selectedCafeY = $(this).data('y');
	                            
	                            $('#selectedCafeName').text(selectedCafeName);
	                            $('#selectedCafeId').val(selectedCafeId);
	                            $('#selectedCafeX').val(selectedCafeX);
	                            $('#selectedCafeY').val(selectedCafeY);
	                            $('#selectedCafePlaceName').val(selectedCafeName);
	                            $('#businessRegForm').show();
	                        });
	                    } else {
	                        $('#cafeListForm').hide();
	                        alert('검색된 카페가 없습니다.');
	                    }
	                } else {
	                    console.error("Expected an array but got:", response);
	                    alert('서버 응답이 올바르지 않습니다.');
	                }
	            },
	            error: function(xhr, status, error) {
	                console.error("Error occurred while searching cafes: ", xhr.responseText);
	                alert('카페 검색 중 오류가 발생했습니다.');
	            }
	        });
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
