<%@page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head><!-- main/home.jsp -->
    <title>Home</title>
    <link rel="stylesheet" type="text/css" href="/css/indexMain.css"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <meta name="_csrf" content="${_csrf.token}" />
    <meta name="_csrf_header" content="${_csrf.headerName}" />
</head>
<body>

<!-- 이미지 슬라이더 컨테이너 -->
<div class="main_slider-container">
	<!-- 카페가 빈 경우... -->
	<c:if test="${empty randomCafes}">
		<div class="main_slide">
            <img src="" alt="ranking 1">
            <div class="main_overlay"></div> <!-- 오버레이 요소 -->
            <div class="main_caption">
                <h3>카페이름</h3>
                <p>아직 홍보용 사진이 없어요😥</p>
				<p>점주님이 첫번째 홍보의 주인공이 되어주세요!</p>
            </div>
        </div>
	</c:if>
	<!-- 카페가 1개 이상 -->
	<c:if test="${not empty randomCafes}">
	    <!-- 슬라이더 이미지들 -->
	    <c:forEach var="cafe" items="${randomCafes}" varStatus="status">
	    	<form action="${pageContext.request.contextPath}/map/searchmap" method="post" class="index_ranking-form">
  				<input type="hidden" name="keyword" value="${cafe.placeName}"/>
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <a href="#" onclick="this.closest('form').submit(); return false;">
			        <div class="main_slide">
			            <img src="${cafe.imagePath}" alt="ranking ${status.index + 1}">
			            <div class="main_overlay"></div> <!-- 오버레이 요소 -->
			            <div class="main_caption">
			                <h3>${cafe.placeName}</h3>
			                <p>${cafe.roadAddressName}</p>
			                <!-- <p>${cafe.cafeId}</p>  --> <!-- cafeId 출력 -->
			            </div>
			        </div>
		        </a>
	        </form>
	    </c:forEach>
	</c:if>
    <!-- 좌우 네비게이션 버튼 -->
    <a class="main_prev" onclick="moveSlide(-1)">&#10094;</a>
    <a class="main_next" onclick="moveSlide(1)">&#10095;</a>

    <!-- 슬라이더 인디케이터 -->
    <div class="main_indicator-container">
        <c:forEach var="cafe" items="${randomCafes}" varStatus="status">
            <span class="main_indicator" onclick="currentSlide(${status.index + 1})"></span>
        </c:forEach>
    </div>
</div>

<script src="/js/indexMain.js"></script>
</body>
</html>
