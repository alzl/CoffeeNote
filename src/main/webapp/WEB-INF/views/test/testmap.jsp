<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script type="text/javascript"
	src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=98bcbc7e9ebc254c6d4372a911d9d03b&libraries=services"></script>
</head>
<body>
<ul>
	<c:forEach var="cafe" items="${cafeList}">
		<li>
			id: ${cafe.cafeId}, x : ${cafe.x}, y : ${cafe.y}, name : ${cafe.placeName}
		</li>
	</c:forEach>
</ul>

<div id="map" style="width: 100%; height: 350px;"></div>
<script>
// 전역 변수로 positions 배열 초기화
var positions = [];

window.onload = function() {
	// LatLngBounds 객체 생성
	var bounds = new kakao.maps.LatLngBounds();

	// JSTL로 리스트 순회하며 positions 배열과 LatLngBounds 계산
	<c:forEach var="cafe" items="${cafeList}">
	    // X, Y 좌표를 실수로 변환
	    var x = parseFloat('${cafe.x}');
	    var y = parseFloat('${cafe.y}');
	    
	    // positions 배열에 추가
	    var latlng = new kakao.maps.LatLng(y, x);
	    positions.push({
	        title: '${cafe.placeName}',
	        latlng: latlng
	    });

	    // LatLngBounds 객체에 좌표 추가
	    bounds.extend(latlng);
	</c:forEach>
	
    var mapContainer = document.getElementById('map'), // 지도를 표시할 div
    mapOption = { 
        center: new kakao.maps.LatLng(0, 0), // 임의의 중심을 설정 (나중에 setBounds로 대체)
        level: 3 // 지도의 확대 레벨
    };

    var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

    // 마커 이미지의 이미지 주소입니다
    var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
        
    for (var i = 0; i < positions.length; i ++) {
        // 마커 이미지의 이미지 크기입니다
        var imageSize = new kakao.maps.Size(24, 35); 
        
        // 마커 이미지를 생성합니다    
        var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize); 
        
        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            map: map, // 마커를 표시할 지도
            position: positions[i].latlng, // 마커를 표시할 위치
            title : positions[i].title, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            image : markerImage // 마커 이미지 
        });
    }

    // 지도를 LatLngBounds에 맞게 확대/축소
    map.setBounds(bounds);
}
</script>
</body>
</html>