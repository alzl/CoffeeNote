<!-- 사업자 등록 기능 -->
<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Home Page</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
	<link rel="stylesheet" type="text/css" href="/css/mypage.css"/>
	 
</head>
<body>
<div id="mypage-container">
<!-- 로딩 이미지 -->
<img id="loadingSpinner" src="/images/buffering.gif" alt="Loading...">
<h1 id="mypage-title">환영합니다, ${user.userName}<small>(${user.userId})</small>님</h1>

<!-- 프로필 이미지 표시 -->
<img id="selectedProfileImg" src="${user.profileImg}" alt="Profile Image" style="cursor: pointer;"/>

<!-- 0909추가 - 프로필 이미지 변경 모달 -->
<div id="profileModal" class="modal">
    <div class="modal-content">
        <span class="close">&times;</span> <!-- 닫기 버튼 -->
        <h2>프로필 이미지를 선택하세요</h2>
        <div class="profile-options">
            <img src="/profile_img/img1.png" alt="Profile Image 1">
            <img src="/profile_img/img2.png" alt="Profile Image 2">
            <img src="/profile_img/img3.png" alt="Profile Image 3">
            <img src="/profile_img/img4.png" alt="Profile Image 4">
            <img src="/profile_img/img5.png" alt="Profile Image 4">
            <img src="/profile_img/img6.png" alt="Profile Image 4">
        </div>
    </div>
</div>

<p>${user.userId}님의 프로필정보</p>

<ul id="profile-ullist">
    <li>
    	아이디 : ${user.userId}
    </li>
    <!-- Name 수정 폼 -->
    <li>
    <form id="nameUpdateForm" action="<c:url value='/user/updateUserName'/>" method="post">
        이름 &nbsp;&nbsp;&nbsp;:
        <input type="text" id="userName" class="inPutBox" name="userName" value="${user.userName}" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="button" id="updateNameBtn" class="mypage-btncss3">수정</button>
    </form>
    </li>
    <li>
    <form id="emailUpdateForm" action="<c:url value='/user/updateUserEmail'/>" method="post">
        이메일 : 
        <input type="text" id="userEmail" name="userEmail" class="inPutBox" value="${user.email}" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="button" id="updateEmailBtn" class="mypage-btncss3">수정</button>
    </form>
    </li>
    <li>사용자 권한: 
    <c:if test="${user.userType eq 'USER' }"> 
    일반 사용자
    </c:if>
    <c:if test="${user.userType eq 'BUSINESS' }"> 
    사업자
    </c:if>
    <c:if test="${user.userType eq 'ADMIN' }"> 
    관리자
    </c:if>
    </li>
</ul>
<!-- 사업자 등록 폼 드롭다운 -->
<button id="dropdown-btn" class="mypage-btncss2">카페 등록</button>
<div id="dropdown-content">
<div id="dropdown-container">
	<!-- 카페 검색 폼 -->
	<div id="searchCafeForm">
	    <h3>사업장 등록하기</h3>
	    <input type="text" id="searchKeyword" class="inPutBox" placeholder="카페 검색..." />
	    <button type="button" id="searchCafeBtn" class="mypage-btncss3">검색</button>
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
	        사업자 등록번호: <input type="text" id="businessRegNum" name="businessRegNum" class="inPutBox" required>
	        <input type="hidden" name="cafeId" id="selectedCafeId">
	        <input type="hidden" name="cafeX" id="selectedCafeX">
	        <input type="hidden" name="cafeY" id="selectedCafeY">
	        <input type="hidden" name="placeName" id="selectedCafePlaceName">
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	        <button type="button" id="registerBusinessBtn" class="mypage-btncss3">카페 등록</button>
	    </form>
	</div>
</div>
</div>

<!-- 등록된 카페 리스트 -->
<h3 id="cafeToggle" onclick="toggleRegisteredCafeList()" style="cursor: pointer;">
    등록된 사업장 리스트
    <span id="cafeToggleIcon">▼</span>
</h3>
<c:if test="${not empty cafeDetailsList}"> 
<div id="registeredCafeList" style="display:none;">
        <c:forEach var="cafe" items="${cafeDetailsList}">
            <li class="registered-cafe-item" data-cafe-id="${cafe.cafeId}">
                <strong id="mypage-strong">${cafe.placeName}</strong>
                <button class="mypage-btncss" onclick="confirmDeleteRegisteredCafe('${cafe.cafeId}')">X</button>
                <br>
                주소: ${cafe.addressName}<br>
                도로명 주소: ${cafe.roadAddressName}<br>
                전화번호: ${cafe.phone != null ? cafe.phone : '전화번호 정보 없음'}<br>
                <a id ="aLingStyle" href="${cafe.placeUrl}" target="_blank">상세 정보 보기</a>

                <!-- 광고 리스트 -->
                <c:if test="${not empty cafe.advertisings}">
                    <div class="registered-cafe-item">
                    	<h4 id="nameTitle">홍보 목록</h4>
                        <ul>
                            <c:forEach var="advertising" items="${cafe.advertisings}">
                               <li class="advertising-item">
								    <div class="advertising-header">
								        <strong id="mypage-strong">제목: ${advertising.title}</strong>
								        <button class="mypage-btncss" onclick="confirmDeleteAdvertising(${advertising.advertisingId})">X</button>
								    </div>
								    <div class="advertising-content">
								        <p>내용: ${advertising.content}</p>
								        <p>작성일: <fmt:formatDate value="${advertising.createdDate}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
								        
								        <!-- 홍보 이미지 리스트 -->
								        <c:if test="${not empty advertising.advertisingImages}">
								            <div class="advertising-images">
								                <h5 id="nameTitle">홍보 이미지</h5>
								                <c:forEach var="image" items="${advertising.advertisingImages}">
								                    <img src="${image.imagePath}" alt="홍보 이미지" class="advertising-image"/>
								                </c:forEach>
								            </div>
								        </c:if>
								    </div>
								</li>
                            </c:forEach>
                        </ul>
                    </div>
                </c:if>
            </li>
        </c:forEach>
</div>
</c:if>

<!-- 홍보 작성 폼 -->
<div id="advertisingForm">
<div id="advertisingForm-subcontainer">
    <h3 id="nameTitle">
    <strong id="currentCafe"></strong>홍보 작성하기
    <button id="cancelButton" class="mypage-btncss" type="button">X</button>
    </h3>
    <form id="createAdvertisingForm" action="<c:url value='/user/createAdvertising?${_csrf.parameterName}=${_csrf.token}'/>" method="post" enctype="multipart/form-data" onsubmit="return validateAdvertisingForm();">
        <input type="hidden" name="cafeId" id="advertisingCafeId">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        제목 : <input type="text" name="title" class="textBox1" required><br>
        <div>
		    <label for="myTextarea" id="textarealabel">설명 : </label>
		    <!--<input type="text" id="myTextarea" name="content" class="textBox2"/>-->
		    <textarea id="myTextarea" name="content" class="textBox2"></textarea>
		</div> 
		<div id="mypage-btncss4-container">
		<div id="file-input">
	    사진 업로드 :&nbsp;<label for="imageInput" class="mypage-btncss5">파일 선택</label>
         <input type="file" name="images" multiple id="imageInput" style="display: none;">
	    </div>
        <button type="submit" class="mypage-btncss4">홍보 등록</button>
        </div>
    </form>
    </div>
</div>

<!-- 내가 추가한 북마크 리스트 --> 
<h3 id ="mypage-toogle-addbookmarklist" onclick="toggleBookmarkList()" style="cursor: pointer;">
    내가 추가한 북마크 리스트
    <span id="mypage-toggleIcon">▼</span>
</h3>
<c:if test="${not empty userBookmarks}">
<div id="userBookmarkList" style="display:none;">
        <c:forEach var="bookmark" items="${userBookmarks}">
            <li id="listStyle">
                <strong id="mypage-strong">${bookmark.title} by ${bookmark.userName} (${bookmark.userId})</strong>
                <br>내용: ${bookmark.content}
                <br>좋아요 수: ${bookmark.likes}
                <br>공개여부: ${bookmark.isPublic}
                <!-- 수정된 부분 -->
                <form id="userBookmarkForm-${bookmark.bookmarkId}" action="${pageContext.request.contextPath}/map/bookmarkdetails2" method="post" style="display:none;">
                    <input type="hidden" name="bookmarkId" value="${bookmark.bookmarkId}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>  
                <button class="mypage-btncss3" onclick="document.getElementById('userBookmarkForm-${bookmark.bookmarkId}').submit(); return false;">상세보기</button>
                <button class="mypage-btncss" onclick="confirmDeleteBookmark(${bookmark.bookmarkId})">X</button>
            </li>
        </c:forEach>
    </ul>
</div>
</c:if>

<!-- 내가 좋아요한 북마크 리스트 -->
<h3 id ="mypage-toogle-addbookmarklist" onclick="toggleLikedBookmarkList()" style="cursor: pointer;">
	내가 좋아요한 북마크 리스트
	<span id="mypage-likedToggleIcon">▼</span>
</h3>
<c:if test="${not empty likedBookmarks}"> 
<div id="likedBookmarkList" style="display:none;">
        <c:forEach var="likedBookmark" items="${likedBookmarks}">
            <li id="listStyle">
                <strong id="mypage-strong">${likedBookmark.title} by ${likedBookmark.userName} (${likedBookmark.userId})</strong>
                <br>내용: ${likedBookmark.content}
                <br>좋아요 수: ${likedBookmark.likes}
                <br>공개여부: ${likedBookmark.isPublic}
                <!-- 수정된 부분 -->
                <form id="likedBookmarkForm-${likedBookmark.bookmarkId}" action="${pageContext.request.contextPath}/map/bookmarkdetails2" method="post" style="display:none;">
                    <input type="hidden" name="bookmarkId" value="${likedBookmark.bookmarkId}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
                <button class="mypage-btncss3" onclick="document.getElementById('likedBookmarkForm-${likedBookmark.bookmarkId}').submit(); return false;">상세보기</button>
            </li>
        </c:forEach>
    </ul>
</div>
</c:if>

<!-- Name 비밀번호 확인 모달 창 -->
<div id="namePasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="namePassword" name="password" required>
    <br>
    <span id="namePasswordError" style="color:red;"></span>
    <br>
    <button type="button" id="nameConfirmPasswordBtn" class="mypage-btncss3">확인</button>
    <button type="button" onclick="closePasswordPopup()" class="mypage-btncss3">닫기</button>
</div>

<!-- Email 비밀번호 확인 모달 창 -->
<div id="emailPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="emailPassword" name="password" required>
    <br>
    <span id="emailPasswordError" style="color:red;"></span>
    <br>
    <button type="button" id="emailConfirmPasswordBtn" class="mypage-btncss3">확인</button>
    <button type="button" onclick="closePasswordPopup()" class="mypage-btncss3">닫기</button>
</div>

<!-- 사업장 추가용 비밀번호 확인 모달 창 -->
<div id="registerBusinessPasswordModal" style="display:none;">
    <label for="password">비밀번호를 입력하세요:</label>
    <input type="password" id="registerBusinessPassword" name="password" required>
    <br>
    <span id="registerBusinessPasswordError" style="color:red;"></span>
    <br>
    <button type="button" id="registerBusinessConfirmPasswordBtn" class="mypage-btncss3">확인</button>
    <button type="button" onclick="closePasswordPopup()" class="mypage-btncss3">닫기</button>
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

</div>
</body>
<script src="/js/mypage.js"></script>
</html>
