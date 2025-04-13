<!-- 카페 검색하고, 리스트 보이고, 리스트의 카페 누르면 그 카페로 이동하고, 현재 위치로 이동하는 버튼 추가함 -->
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

    /* 현재 위치 버튼을 우측 상단에 배치하는 스타일 */
    #currentLocationButton {
        position: absolute;
        top: 10px;
        right: 10px;
        z-index: 1;
        background-color: white;
        border-radius: 50%;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
        cursor: pointer;
    }

    /* 검색 결과 리스트를 표시하는 스타일 */
    #listForm {
        position: absolute;
        top: 70px; /* 검색창 아래에 위치 */
        left: 5px;
        z-index: 1; /* 지도 위에 표시되도록 함 */
        background-color: white;
        padding: 5px;
        border-radius: 3px;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.3);
        max-height: 200px; /* 최대 높이 설정, 필요시 조정 */
        overflow-y: auto; /* 내용이 많을 때 스크롤 가능하게 설정 */
        width: 300px; /* 리스트의 너비 설정 */
    }

    /* 지도의 크기를 조정할 스타일 */
    html, body {
        height: 100%;
        margin: 0;
        padding: 0;
    }

    #map {
        width: 100%;
        height: 100vh; /* 화면 높이와 동일하게 설정 */
        position: relative;
    }

    .cafe-item {
        cursor: pointer; /* 클릭 가능한 커서 표시 */
        margin-bottom: 10px;
        border-bottom: 1px solid black;
    }
</style>
<!-- Kakao Maps API 스크립트 -->
<script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=67caf0129419f4cf4108be62dbe104d3&libraries=services"></script>
</head>
<body>
<div id="map"></div>

<!-- 현재 위치 버튼 -->
<div id="currentLocationButton" onclick="moveToCurrentLocation()">
    📍
</div>

<!-- 검색창과 버튼을 포함하는 폼 -->
<div id="searchForm">
    <form action="/map/searchmap" method="post" accept-charset="UTF-8">
        <input type="text" name="keyword" id="keywordInput" placeholder="Search..." style="padding: 5px;" required>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        <button type="submit" style="padding: 5px;">Search</button>
    </form>
</div>

<!-- 검색된 카페 리스트를 표시하는 폼 -->
<div id="listForm">
    <c:choose>
        <c:when test="${not empty cafeDetailsList}">
            <ul style="list-style: none; padding: 0;">
                <c:forEach var="cafe" items="${cafeDetailsList}" varStatus="status">
                    <li class="cafe-item" onclick="moveToMarker(${status.index})">
                        <strong>${cafe.placeName}</strong><br>
                        주소: ${cafe.addressName}<br>
                        전화번호: ${cafe.phone}<br>
                        <a href="${cafe.placeUrl}" target="_blank">자세히 보기</a>
                    </li>
                </c:forEach>
            </ul>
        </c:when>
        <c:otherwise>
            <p>검색된 내용이 없습니다</p>
        </c:otherwise>
    </c:choose>
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
    var markers = []; // 생성된 마커들을 저장할 배열

    <c:if test="${not empty cafeDetailsList}">
        var bounds = new kakao.maps.LatLngBounds();
        <c:forEach var="cafe" items="${cafeDetailsList}">
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
            markers.push(marker); // 생성된 마커를 배열에 추가
        }

        // 지도를 마커가 모두 보이도록 조정
        map.setBounds(bounds);
    </c:if>

    // 특정 마커의 위치로 지도를 부드럽게 이동하는 함수
    window.moveToMarker = function(index) {
        var position = markers[index].getPosition();
        map.panTo(position); // 부드럽게 이동
    };

    // 현재 위치로 지도를 부드럽게 이동하고 주소로 검색하는 함수
    window.moveToCurrentLocation = function() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                var lat = position.coords.latitude; // 현재 위도
                var lon = position.coords.longitude; // 현재 경도

                var locPosition = new kakao.maps.LatLng(lat, lon); 
                map.panTo(locPosition); // 부드럽게 현재 위치로 이동

                // 현재 위치를 주소로 변환하기 위해 Kakao API 호출
                var geocoder = new kakao.maps.services.Geocoder();
                geocoder.coord2Address(lon, lat, function(result, status) {
                    if (status === kakao.maps.services.Status.OK) {
                        var address = result[0].address.address_name;
                        
                        // 변환된 주소를 키워드로 사용하여 검색 요청
                        document.getElementById('keywordInput').value = address;
                        document.getElementById('searchForm').submit();
                    } else {
                        alert("주소를 찾을 수 없습니다.");
                    }
                });
            });
        } else {
            alert("GPS를 지원하지 않는 브라우저입니다.");
        }
    };
}
</script>
</body>
</html>