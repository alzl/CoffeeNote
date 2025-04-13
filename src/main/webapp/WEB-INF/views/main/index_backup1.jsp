<!-- 0910 이전 -->
<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Main Page</title>
    <link rel="stylesheet" type="text/css" href="/css/main.css"/>
    <script>
        // window.onload 이벤트를 사용하여 페이지 로드 후 실행될 함수를 설정
        //window.onload = function() { <-- 이거 쓰면 부모페이지 - 자식페이지 간에 onload 중첩이 안됨
        window.addEventListener('load', function() {
            console.log("open index");
        	//alert("안녕 index.jsp에요");
            console.log("index.jsp onload");
        });
    </script>
    <style>
        *{ /*테스트용 테두리*/
    		border: 1px dotted #F8E0E0;
    	}
    </style>
</head>
<body>
    <!-- Header 영역 -->
    <div id="header">
        <h1>Header</h1>
        <!-- 로그인 안 한 경우 -->
        <sec:authorize access="isAnonymous()">
            <a href="/login/login">Login</a>
            <a href="/login/register">Register</a>
        </sec:authorize>
        <!-- 로그인 한 경우 -->
        <sec:authorize access="isAuthenticated()">
            <img src="${profileImg}" alt="Profile Image" style="height: 50px"/>
            <a href="/user/home">${userName}<small>(${userId})</small></a>
            <a href="/login/logout">Logout</a>
        </sec:authorize>
    </div>

    <!-- Navigation 버튼들을 위한 aside 영역 -->
    <div id="aside">
        <button onclick="location.href='/'">Home</button>
        <button onclick="location.href='/a'">Page A</button>
        <button onclick="location.href='/b'">Page B</button>
        <button onclick="location.href='/noticeList'">공지사항</button>
        <button onclick="location.href='/map/mapview'">지도보기</button>
        <button onclick="showAlert(this)">test 1</button>
        <button onclick="showAlert(this)">test 2</button>
        <br>
        <h2>Top 10 Cafes</h2>
		<ul>
		    <c:forEach var="ranking" items="${cafeRankings}">
		        <li>
		            <form action="${pageContext.request.contextPath}/map/searchmap" method="post" style="display:inline;">
		                <input type="hidden" name="keyword" value="${ranking.cafe.placeName}"/>
		                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		                <a href="#" onclick="this.closest('form').submit(); return false;">
		                    ${ranking.cafe.placeName} - Score: 
		                    <fmt:formatNumber value="${ranking.adjustedScore}" maxFractionDigits="3"/>
		                </a>
		            </form>
		            <br/>
		            Average Rating: 
		            <fmt:formatNumber value="${ranking.avgRating}" maxFractionDigits="3"/>, 
		            Review Count: ${ranking.reviewCount} <!-- 추가된 부분 -->
		        </li>
		    </c:forEach>
		</ul>
		
		<h2>Top 10 Bookmarks</h2>
		<ul>
		    <c:forEach var="bookmark" items="${topBookmarks}">
		        <li>
		            <a href="${pageContext.request.contextPath}/map/mapview/${bookmark.bookmarkId}">
		                ${bookmark.title} - Likes: ${bookmark.likes}
		            </a>
		        </li>
		    </c:forEach>
		</ul>
    </div>

    <!-- Section 영역 -->
    <div id="section">
        <c:import url="${section}" />
    </div>

    <!-- Footer 영역 -->
    <div id="footer">
        <p>Footer</p>
    </div>
<!--<script src="/js/script.js"></script>  -->
</body>
</html>
