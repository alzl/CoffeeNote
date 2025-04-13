<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>지도 검색 - 카페</title>
<style>
    /* 검색창과 버튼을 지도의 좌측 상단에 배치하는 스타일 */
    #searchForm {
        position: absolute;
        top: 5px;
        left: 5px;
        z-index: 1; /* 지도 위에 표시되도록 함 */
        background-color: white;
        padding: 5px;
        border-radius: 3px;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
    }
</style>
<!-- Kakao Maps API 스크립트 -->
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=67caf0129419f4cf4108be62dbe104d3&libraries=services"></script>
</head>
<body>
<div id="map" style="width: 100%; height: 350px; position: relative;"></div>

<!-- 검색창과 버튼을 포함하는 폼 -->
<div id="searchForm">
    <form action="/test/searchmap2" method="post" accept-charset="UTF-8">
        <input type="text" name="keyword" placeholder="Search..." style="padding: 5px;" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit" style="padding: 5px;">Search</button>
    </form>
</div>

<script>
window.onload = function() {
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = { 
        center: new kakao.maps.LatLng(37.556008, 126.972341), // 서울역 중심 좌표
        level: 3 // 지도의 확대 레벨
    };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 검색 결과가 있을 때만 마커를 표시합니다
    var positions = [];

    <c:if test="${not empty cafeList}">
        var bounds = new kakao.maps.LatLngBounds();
        <c:forEach var="cafe" items="${cafeList}">
            var latlng = new kakao.maps.LatLng(${cafe.y}, ${cafe.x});
            positions.push({
                title: '${cafe.placeName}',
                latlng: latlng
            });
            bounds.extend(latlng);
        </c:forEach>

        // 마커 생성
        for (var i = 0; i < positions.length; i++) {
            var marker = new kakao.maps.Marker({
                map: map,
                position: positions[i].latlng,
                title: positions[i].title
            });
        }

        // 지도를 마커가 모두 보이도록 조정
        map.setBounds(bounds);
    </c:if>
}
</script>
</body>
</html>
