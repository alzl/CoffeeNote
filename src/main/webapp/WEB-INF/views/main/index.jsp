<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="_csrf" content="${_csrf.token}" />
	<meta name="_csrf_header" content="${_csrf.headerName}" />
    <title>Coffee Note</title>
    <link rel="stylesheet" type="text/css" href="/css/index.css"/>
    <!-- FontAwesome 아이콘 라이브러리 -->
    <script  src="https://kit.fontawesome.com/427caf46c6.js" crossorigin="anonymous"></script>
    

</head>
<body>
    <!-- 로그인 모달 -->
    <div id="index_loginModal" style="display:none;">
        <div class="index_modal-content">
            <div id="index_login-options">
                <!-- 카카오 로그인 버튼 -->
                <button id="index_kakao-login">
                    <img src="https://i.ibb.co/LJh68Rb/image.png" alt="Kakao Icon" class="index_kakao-icon">
                    <span>카카오 계정으로 로그인</span>
                </button>
    
                <!-- 이메일 로그인 -->
                <button id="index_email-login" onclick="showSection('login')">이메일로 로그인</button>
            </div>
        </div>
    </div>
    
    <!-- Header 영역 -->
    <div id="index_header">
         <!--메인로고-->
        <div id="index_logo" onclick="location.href='/'" style="cursor:pointer;"> 
            <img src="/images/logo.png" width="200px" height="200px" alt="LogoImage">
        </div>
        <!--타이틀-->
        <div id="index_title">
            <h1 onclick="location.href='/'" style="cursor:pointer;">커 피 노 트</h1>
            <h2>COFFEE  NOTE</h2>
        </div>

        <!--로그인, 회원가입 버튼-->
        <div id="index_account">
        <!-- kakao login 안쓰니까 따로 modal 뜨는 거 지웠음, 되면 추가 ㄱ -->
	        <!-- 로그인 안 한 경우 -->
	        <sec:authorize access="isAnonymous()">
	        	<!-- <button id="index_account_btn1" onclick="showLoginModal()">로그인</button> -->
	            <button onclick="location.href='/login/login'">로그인</button>
	            <button onclick="location.href='/login/register'">회원가입</button>
	        </sec:authorize>
	        <!-- 로그인 한 경우 -->
	        <sec:authorize access="isAuthenticated()">
	            <button onclick="location.href='/user/home'">마이페이지</button>
	            <button onclick="location.href='/login/logout'">로그아웃</button>    
	        </sec:authorize>     
        </div> 
    </div>

    <div id="index_content">
    <!-- Navigation 버튼들을 위한 aside 영역 -->
	    <div id="index_aside">
	    
		    <div id="index_search-section">
			<!-- 폼의 action을 /map/searchmap으로 지정하고 메서드를 POST로 설정 -->
		    <form id="index_search-box" action="<c:url value='/map/searchmap'/>" method="post" accept-charset="UTF-8" onsubmit="showLoadingSpinner()">
		        <input id="index_search-txt" type="text" name="keyword" placeholder="장소를 검색해보세요." required>
		        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		        <!-- 돋보기 아이콘의 검색버튼 -->
		        <button id="index_search-button">
			    <i class="fa-solid fa-magnifying-glass"></i>
		        </button>
		    </form>
		          <button id="index_location-button" onclick="location.href='/map/mapview'">☕&nbsp; 내 주변 카페 찾기</button>
		    </div>
		    
		    <div id="index_ranking-section">
			<!-- 0923: 인기 카페 순위 리스트 영역 -->
			<h2 id="index_rank-title">인기 카페 순위</h2>
			<ul class="index_ranking-list">
			    <c:forEach var="ranking" items="${cafeRankings}" varStatus="status">
			        <li class="index_ranking-item">
			            <span class="index_rank">${status.index + 1}</span>
			            <form action="${pageContext.request.contextPath}/map/searchmap" method="post" class="index_ranking-form">
			                <input type="hidden" name="keyword" value="${ranking.cafe.placeName}"/>
			                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			                <a href="#" class="index_ranking-link" onclick="this.closest('form').submit(); return false;">
			                    <div class="index_ranking-content">
			                        <div class="index_ranking-name">${ranking.cafe.placeName}</div>
			                        <div class="index_ranking-details">
			                            <span>Score: <fmt:formatNumber value="${ranking.adjustedScore}" maxFractionDigits="3"/></span><br>
			                            <span>Average Rating: <fmt:formatNumber value="${ranking.avgRating}" maxFractionDigits="3"/></span><br>
			                            <span>Review Count: ${ranking.reviewCount}</span>
			                        </div>
			                    </div>
			                </a>
			            </form>
			        </li>
			    </c:forEach>
			</ul>
			
			<!--0923 북마크 추천 순위 리스트 영역 -->
			<h2 id="index_bookmark-rank-title">북마크 추천 순위</h2>
			<ul class="index_ranking-list">
			    <c:forEach var="bookmark" items="${topBookmarks}" varStatus="status">
			        <li class="index_bookmark-ranking-item">
			            <span class="index_rank">${status.index + 1}</span>
			            <form id="index_bookmarkForm-${bookmark.bookmarkId}" action="${pageContext.request.contextPath}/map/bookmarkdetails2" method="post" class="index_bookmark-ranking-form">
			                <input type="hidden" name="bookmarkId" value="${bookmark.bookmarkId}">
			                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
			                <a href="#" class="index_bookmark-ranking-link" onclick="document.getElementById('index_bookmarkForm-${bookmark.bookmarkId}').submit(); return false;">
			                    <div class="index_bookmark-ranking-content">
			                        <div class="index_bookmark-ranking-title">${bookmark.title}</div>
			                        <div class="index_bookmark-ranking-likes">Likes: ${bookmark.likes}</div>
			                    </div>
			                </a>
			            </form>
			        </li>
			    </c:forEach>
			</ul>
			</div>    
		    <!-- aside 버튼 -->
		    <div id="index_aside-button">
		            <button id="index_notice-button" onclick="location.href='/noticeList'">공지사항</button>
		   	</div>
		</div>  <!-- aside 끝  -->
			
	        <!-- Section 영역 -->
	    <div id="index_section">
	        <c:import url="${section}" />
	    </div>
	</div>

    <!-- Footer 영역 -->
   <div id="index_footer">
        <p>
            Copyright 2024 CoffeeNote Co. All Rights Reserved.
            <br>
            Designed by TEAM. NJK
        </p>
        <div>
            <span onclick="location.href='/storeGuide'">입점안내</span> &nbsp; | &nbsp;
            <span onclick="location.href='/comInfo'">회사소개</span> &nbsp; | &nbsp;
            <span onclick="location.href='/terms'">이용약관</span>
        </div>
    </div>
<!--<script src="/js/script.js"></script>  -->
</body>
</html>
